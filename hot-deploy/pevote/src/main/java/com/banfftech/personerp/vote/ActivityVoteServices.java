package main.java.com.banfftech.personerp.vote;

import java.sql.Timestamp;
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
 * Vote Service
 * Created by S.Y.L on 2017/3/21.
 */
public class ActivityVoteServices {



    public static final String module = ActivityVoteServices.class.getName();




    /**
     * User Do Answer
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws InterruptedException
     * @author S.Y.L
     */
    public static Map<String, Object> doPollQuestion(DispatchContext dctx, Map<String, ? extends Object> context)
            throws  GenericEntityException, GenericServiceException, InterruptedException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        Map<String, Object> listMap = new HashMap<String, Object>();


        //Scope Param
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        //Anwser User
        String partyId = (String) userLogin.get("partyId");
        //Vote Question ID
        String surveyQuestionId = (String) context.get("surveyQuestionId");
        //Vote ID
        String surveyId = (String) context.get("surveyId");




        //Create Survey Response

        Map<String,Object> answersMap = new HashMap<String, Object>();

        answersMap.put("answers","{"+surveyQuestionId+"=Y}");

        Map<String, Object> createSurveyInMap =
                UtilMisc.toMap("userLogin", userLogin,
                        "answers",answersMap,
                        "partyId",partyId,
                        "surveyId",surveyId);

        Map<String, Object> createSurveyReturnMap = dispatcher.runSync("createSurveyResponse", createSurveyInMap);

        //Response ID
        String surveyResponseId = (String) createSurveyReturnMap.get("surveyResponseId");
        //Survey Multi RespCol ID
        String surveyMultiRespColId = (String) createSurveyReturnMap.get("surveyMultiRespColId");

        //Query Vote List
        EntityCondition findConditionsToVoteList = EntityCondition
                .makeCondition(UtilMisc.toMap("surveyResponseId", surveyResponseId,
                        "surveyQuestionId", surveyQuestionId, "surveyMultiRespColId", "_NA_"));

        //Update Survey Response Answer
        GenericValue surveyResponseAnswer =  EntityUtil.getFirst(delegator.findList("SurveyResponseAnswer",findConditionsToVoteList , null, null, null, false));

        surveyResponseAnswer.set("surveyQuestionId", surveyQuestionId);

        surveyResponseAnswer.set("surveyResponseId", surveyResponseId);

        surveyResponseAnswer.set("booleanResponse","Y");

        surveyResponseAnswer.store();


        //Service Foot
        listMap.put("resultMsg", UtilProperties.getMessage("PeVoteUiLabels", "success", locale));
        resultMap.put("resultMap", listMap);
        return resultMap;
    }




    /**
     * Create Vote Title & Questions
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws InterruptedException
     * @author S.Y.L
     */
    public static Map<String, Object> createSurveyAndQuestions(DispatchContext dctx, Map<String, ? extends Object> context)
            throws  GenericEntityException, GenericServiceException, InterruptedException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        Locale locale = (Locale) context.get("locale");


        //Scope Param
        GenericValue userLogin = (GenericValue) context.get("userLogin");

        //Create By
        String partyId = (String) userLogin.get("partyId");

        //Activity ID
        String workEffortId = (String) context.get("workEffortId");

        //Vote Title
        String surveyName = (String) context.get("surveyName");

        //允许匿名投票,默认'否'N
        String isAnonymous =  context.get("isAnonymous")==null?"N":(String)context.get("isAnonymous");

        //允许重复投票,默认'是'N
        String allowMultiple = context.get("allowMultiple")==null?"N":(String)context.get("allowMultiple");

        //允许投票被更新,默认'是'Y
        String allowUpdate = context.get("allowUpdate")==null?"Y":(String)context.get("allowUpdate");

        //QuestionsName (Array)
        String questions = (String) context.get("questions");

        //Question Category Id ;Default Value = Poll Questions , ID =  1002
        String surveyQuestionCategoryId = "1002";

        //Question Type Id ; Default Value :Boolean
        String surveyQuestionTypeId   = "BOOLEAN";

        //Split Array
        String [] questionsArray = questions.split("&");


        //Step 1: Create Vote Title
        Map<String, Object> createSurveyInMap =
                UtilMisc.toMap("userLogin", userLogin,
                        "partyId", partyId,
                        "surveyName",surveyName,
                        "isAnonymous",isAnonymous,
                        "allowMultiple",allowMultiple,
                        "allowUpdate",allowUpdate);
        Map<String, Object> createSurveyResultMap = dispatcher.runSync("createSurvey", createSurveyInMap);
        //Vote ID
        String surveyId = (String)createSurveyResultMap.get("surveyId");


        //Step 2: Create Vote Questions
        forEachCreateQuestionsToSurvey(surveyQuestionTypeId,surveyQuestionCategoryId,userLogin,surveyId,delegator,dispatcher,questionsArray);


        //Step 3: ASSOC Vote To Activity
        Map<String, Object> createSurveyAppMap =
                UtilMisc.toMap("userLogin", userLogin,
                        "surveyId", surveyId,
                        "workEffortId",workEffortId ,
                        "fromDate",new Timestamp(new Date().getTime())
                );
        dispatcher.runSync("createWorkEffortSurveyAppl", createSurveyAppMap);



        //Service Foot
        Map<String, Object> inputMap = new HashMap<String, Object>();
        Map<String, Object> result = ServiceUtil.returnSuccess();
        result.put("resultMap", inputMap);
        inputMap.put("resultMsg", UtilProperties.getMessage("PeVoteUiLabels", "success", locale));

        return result;
    }








    /**
     * ForEach Create Questions To Vote
     *
     * @param surveyQuestionTypeId
     * @param surveyQuestionCategoryId
     * @param userLogin
     * @param surveyId
     * @param delegator
     * @param dispatcher
     * @param questionsArray
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @author S.Y.L
     */
    private static void forEachCreateQuestionsToSurvey(String surveyQuestionTypeId,String surveyQuestionCategoryId,GenericValue userLogin,String surveyId, Delegator delegator, LocalDispatcher dispatcher, String[] questionsArray)throws  GenericEntityException, GenericServiceException {
        if(questionsArray!=null && questionsArray.length>0){
            for(int index =0;index<questionsArray.length;index++){
                Map<String, Object> createSurveyQuestionsInMap =
                        UtilMisc.toMap("userLogin", userLogin,
                                "surveyId",surveyId,
                                "question",questionsArray[index],
                                "surveyQuestionCategoryId",surveyQuestionCategoryId,
                                "surveyQuestionTypeId",surveyQuestionTypeId);
                dispatcher.runSync("createSurveyQuestion", createSurveyQuestionsInMap);
            }
        }
    }
}
