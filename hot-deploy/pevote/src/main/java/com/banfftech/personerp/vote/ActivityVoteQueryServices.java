package main.java.com.banfftech.personerp.vote;

import java.util.*;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.entity.condition.EntityConditionList;
import org.apache.ofbiz.entity.GenericEntity;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

/**
 * ActivityVote QueryService
 * Created by S.Y.L on 2017/3/21.
 */
public class ActivityVoteQueryServices {

    public static final String module = ActivityVoteQueryServices.class.getName();


    /**
     * Query ActivityVote List
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @author S.Y.L
     */
    public static Map<String, Object> queryActivityVoteList(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        Map<String, Object> listMap = new HashMap<String, Object>();


        //Scope Param
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = (String) userLogin.get("partyId");
        String workEffortId = (String) context.get("workEffortId");


        //QueryLogic Block
        EntityCondition findConditionsToVoteList = EntityCondition
                .makeCondition(UtilMisc.toMap("workEffortId", workEffortId));

        List<GenericValue> voteList = delegator.findList("ActivityVotes", findConditionsToVoteList, UtilMisc.toSet("surveyId", "fromDate", "surveyName"),
                null, null, false);


        //Service Foot
        listMap.put("activityPollQuestionsTitle", voteList);
        listMap.put("resultMsg", UtilProperties.getMessage("PeVoteUiLabels", "success", locale));
        resultMap.put("resultMap", listMap);
        return resultMap;
    }


    /**
     * Query ActivityVote Questions And Answer
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @author S.Y.L
     */
    public static Map<String, Object> queryActivityVoteQuestions(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        Map<String, Object> inputMap = new HashMap<String, Object>();

        //Scope Prarm
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        //voteId
        String surveyId = (String) context.get("surveyId");


        //QueryLogic Block
        EntityCondition findConditionsToVoteList =  EntityCondition
                .makeCondition(UtilMisc.toMap("surveyId", surveyId));
        List<GenericValue> questionList = delegator.findList("SurveyQuestionAndAppl", findConditionsToVoteList, UtilMisc.toSet("surveyId", "question", "surveyQuestionId"),
                null, null, false);

        //Questions And Answer Count Map
        Map<String, List<Map<String, Object>>> fatMethodReturnMap = new HashMap<String, List<Map<String, Object>>>();

        //Query Answer List
        if (null != questionList && questionList.size() > 0) {
            fatMethodReturnMap = countResponseAnswer(delegator, dispatcher, userLogin, questionList);
        }


        //Service Foot
        inputMap.put("questions", fatMethodReturnMap.get("questions"));
        inputMap.put("partyResponceAnswer", fatMethodReturnMap.get("partyResponceAnswer"));
        inputMap.put("resultMsg", UtilProperties.getMessage("PeVoteUiLabels", "success", locale));
        resultMap.put("resultMap", inputMap);
        return resultMap;
    }





    /**
     * Query Vote Response Answer & Count
     *
     * :( Bad Method
     *
     * @param delegator
     * @param dispatcher
     * @param userLogin
     * @param questions
     * @param questionList
     * @param responceAnswer
     * @author S.Y.L
     */
    private static Map<String,List<Map<String,Object>>> countResponseAnswer(Delegator delegator, LocalDispatcher dispatcher, GenericValue userLogin,  List<GenericValue> questionList) throws GenericEntityException {

        //用户实际想看的问题及问题的回答数
        List<Map<String,Object>> questions           = new ArrayList<Map<String, Object>>();

        //对问题回答了的用户列表
        List<Map<String,Object>> partyResponceAnswer = new ArrayList<Map<String, Object>>();

        //将上述两个List存放在此用于返回
        Map<String,List<Map<String,Object>>> fatMethodReturnMap = new HashMap<String, List<Map<String, Object>>>();

        //又开始遍历、这一次为的是组装投票项和他的已投数量
        EntityCondition findConditionsToAnswerList = null;

        for(GenericValue question : questionList){

            //用户实际想要看的Questions
            Map<String,Object> realQuestion = new HashMap<String, Object>();

            findConditionsToAnswerList = EntityCondition
                    .makeCondition(UtilMisc.toMap("surveyQuestionId",question.get("surveyQuestionId")));

            List<GenericValue> answerList = delegator.findList("SurveyResponseAndAnswerAndPerson",findConditionsToAnswerList,null,
                    null, null, false);

            //该问题投票数
            realQuestion.put("answerCount",answerList.size()+"");

            //该问题的名称
            realQuestion.put("question",question.get("question"));

            //问题的主键
            realQuestion.put("surveyQuestionId",question.get("surveyQuestionId"));

            //投票标题的主键
            realQuestion.put("surveyId",question.get("surveyId"));

            //组装好的问题及回答数放入list
            questions.add(realQuestion);

            //关于投票问题和已投票的人的列表的获得
            if(null != answerList){
                partyResponceAnswer.add(forAddPersonToView(answerList));
            }

        }

        //投票问题、已回答人数
        fatMethodReturnMap.put("questions",questions);

        //投票问题和实际回答的人
        fatMethodReturnMap.put("partyResponceAnswer",partyResponceAnswer);

        return fatMethodReturnMap;
    }


    /**
     * Query Person Answer
     *
     * @param answerList
     * @return
     * @author S.Y.L
     */
    private static Map<String, Object> forAddPersonToView(List<GenericValue> answerList) {
        //Answer List
        for(GenericValue gv :answerList){
            Map<String, Object> questionAndUser = new HashMap<String, Object>();
            questionAndUser.put("question", gv.get("question"));
            questionAndUser.put("nickName",gv.get("nickname"));
            questionAndUser.put("partyId",gv.get("partyId"));
            return questionAndUser;
        }
        return null;
    }

}
