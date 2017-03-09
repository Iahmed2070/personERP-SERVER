package com.banfftech.personerp;

import java.util.*;

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
import sun.net.www.content.text.Generic;

public class PersonErpQueryService {
    public static final String module = PersonErpQueryService.class.getName();


    /**
     * 活动的账务列表
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     */
    public static Map<String, Object> findActivityPayment(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        Map<String, Object> inputMap = new HashMap<String, Object>();
        //workEffortId
        String workEffortId = (String) context.get("workEffortId");

        //查询投票项目列表
        EntityCondition findConditionsToPayList  = EntityCondition
                .makeCondition(UtilMisc.toMap("workEffortId", workEffortId));
        List<GenericValue> payList = delegator.findList("PartyPaymentInfoAndPerson", findConditionsToPayList, null,
                null, null, false);

        inputMap.put("payList",payList);
        inputMap.put("resultMsg", UtilProperties.getMessage("PersonContactsUiLabels", "success", locale));
        resultMap.put("resultMap", inputMap);
        return resultMap;
    }


        /**
         * 查询投票项列表
         * @param dctx
         * @param context
         * @return
         * @throws GenericEntityException
         */
    public static Map<String, Object> findActivityPollQuestions(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        GenericValue userLogin = (GenericValue)context.get("userLogin");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        Map<String, Object> inputMap = new HashMap<String, Object>();
        //surveyId
        String surveyId = (String) context.get("surveyId");

        //查询投票项目列表
        EntityCondition findConditionsToVoteList = null;
        findConditionsToVoteList = EntityCondition
                .makeCondition(UtilMisc.toMap("surveyId",surveyId));
        List<GenericValue> questionList = delegator.findList("SurveyQuestionAndAppl",findConditionsToVoteList,UtilMisc.toSet("surveyId","question","surveyQuestionId"),
                null, null, false);

        //问题及问题已投票数量
        Map<String,List<Map<String,Object>>> fatMethodReturnMap = new HashMap<String, List<Map<String, Object>>>();//方法返回Map.历史最胖变量
        //查询投票项目的回答列表
        if(null!=questionList && questionList.size()>0){
            fatMethodReturnMap =  countResponceAnswer(delegator,dispatcher,userLogin,questionList);
        }


        inputMap.put("questions",fatMethodReturnMap.get("questions"));
        inputMap.put("partyResponceAnswer",fatMethodReturnMap.get("partyResponceAnswer"));

        inputMap.put("resultMsg", UtilProperties.getMessage("PersonContactsUiLabels", "success", locale));
        resultMap.put("resultMap", inputMap);
        return resultMap;
    }

    /**
     * 查询投票项目的回答列表
     * @param delegator
     * @param dispatcher
     * @param userLogin
     * @param questions
     * @param questionList
     * @param responceAnswer
     */
    private static Map<String,List<Map<String,Object>>> countResponceAnswer(Delegator delegator, LocalDispatcher dispatcher, GenericValue userLogin,  List<GenericValue> questionList) throws GenericEntityException {

        //用户实际想看的问题及问题的回答数
        List<Map<String,Object>> questions           = new ArrayList<Map<String, Object>>();
        //对问题回答了的用户列表
        List<Map<String,Object>> partyResponceAnswer = new ArrayList<Map<String, Object>>();

        //将上述两个List存放在此用于返回
        //TODO FIX BUG
        Map<String,List<Map<String,Object>>> fatMethodReturnMap = new HashMap<String, List<Map<String, Object>>>();

        //又开始遍历、这一次为的是组装投票项和他的已投数量
         EntityCondition findConditionsToAnswerList = null;
         for(GenericValue question : questionList){
             //用户实际想要看的question
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
     * 对该问题投票过的用户剥离出来用于显示
     * @param answerList
     * @return
     */
    private static Map<String, Object> forAddPersonToView(List<GenericValue> answerList) {
        //已投票的用户列表

        for(GenericValue gv :answerList){
            Map<String, Object> questionAndUser = new HashMap<String, Object>();
            questionAndUser.put("question", gv.get("question"));
            questionAndUser.put("userName",gv.get("lastName")+""+gv.get("firstName")+"");
            //问题
            return questionAndUser;

            //Content
        }
            return null;
    }


    /**
     * 查询投票标题列表
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     */
    public static Map<String, Object> findActivityPollQuestionsTitle(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = (String) userLogin.get("partyId");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        Map<String, Object> inputMap = new HashMap<String, Object>();
        //活动ID
        String workEffortId = (String) context.get("workEffortId");

        EntityCondition findConditionsToVoteList = null;
        findConditionsToVoteList = EntityCondition
                .makeCondition(UtilMisc.toMap("workEffortId",workEffortId));
        List<GenericValue> voteList = delegator.findList("ActivityVotes",findConditionsToVoteList,UtilMisc.toSet("surveyId","fromDate","surveyName"),
                null, null, false);


        inputMap.put("activityPollQuestionsTitle",voteList);
        inputMap.put("resultMsg", UtilProperties.getMessage("PersonContactsUiLabels", "success", locale));
        resultMap.put("resultMap", inputMap);
        return resultMap;
    }
    /**
     * 查询用户信息
     *
     * @param dctx
     * @param context
     * @return Map
     * @throws GenericEntityException
     * @author zhangwenwen
     */
    public static Map<String, Object> findPersonInfo(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        String partyId = (String) context.get("partyId");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        Map<String, Object> inputMap = new HashMap<String, Object>();
        GenericValue person = delegator.findOne("Person", false, UtilMisc.toMap("partyId", partyId));
        if (UtilValidate.isEmpty(person)) {
            resultMap = new HashMap<String, Object>();
            inputMap.put("resultMsg", UtilProperties.getMessage("PersonContactsUiLabels", "userDoesNotExist", locale));
            resultMap.put("resultMap", inputMap);
            return resultMap;
        }
        // 判断用户是否被禁用
        GenericValue party = delegator.findOne("Party", UtilMisc.toMap("partyId", partyId), false);
        if ("PARTY_DISABLED".equals(party.get("statusId"))) {
            resultMap = new HashMap<String, Object>();
            inputMap.put("resultMsg", UtilProperties.getMessage("PersonContactsUiLabels", "userDisabled", locale));
            resultMap.put("resultMap", inputMap);
            return resultMap;
        }
        // 获取姓名
        inputMap.put("personName", "" + person.get("lastName") + person.get("firstName"));
        // 获取性别
        String gender = "";
        if (UtilValidate.isNotEmpty(person.get("gender")))
            gender = "M".equals(person.get("gender")) ? "男" : "女";
        inputMap.put("gender", gender);

        // 获取头像
        EntityCondition findConditions = EntityCondition.makeCondition(
                EntityCondition.makeCondition(UtilMisc.toMap("partyId", partyId)),
                EntityCondition.makeCondition(UtilMisc.toMap("partyContentTypeId", "LGOIMGURL")),
                EntityUtil.getFilterByDateExpr());

        GenericValue partyContent = EntityUtil.getFirst(
                delegator.findList("PartyContent", findConditions, null, UtilMisc.toList("-fromDate"), null, false));
        if (UtilValidate.isNotEmpty(partyContent)) {
            String contentId = partyContent.getString("contentId");
            inputMap.put("headPortrait",
                    "http://localhost:3400/personContacts/control/stream?contentId=" + contentId);
        }

        // 获取电话号码
        GenericValue telecomNumber = EntityUtil.getFirst(delegator.findByAnd("findTelecomNumberByPartyId",
                UtilMisc.toMap("partyId", partyId, "contactMechPurposeTypeId", "PHONE_MOBILE", "contactMechTypeId",
                        "TELECOM_NUMBER"),
                null, false));
        if (UtilValidate.isNotEmpty(telecomNumber))
            inputMap.put("contactNumber", telecomNumber.getString("contactNumber"));
        // 获取email
        GenericValue emailAddress = EntityUtil.getFirst(
                delegator.findByAnd("findEmailByPartyId", UtilMisc.toMap("partyId", partyId, "contactMechPurposeTypeId",
                        "PRIMARY_EMAIL", "contactMechTypeId", "EMAIL_ADDRESS"), null, false));
        if (UtilValidate.isNotEmpty(emailAddress))
            inputMap.put("email", emailAddress.getString("infoString"));
        // 获取公司
        GenericValue company = EntityUtil.getFirst(delegator.findByAnd("PartyAttribute",
                UtilMisc.toMap("partyId", partyId, "attrName", "Company"), null, false));
        if (UtilValidate.isNotEmpty(company))
            inputMap.put("company", company.get("attrValue"));
        // 获取标签
        GenericValue lable = EntityUtil.getFirst(delegator.findByAnd("PartyRelationship",
                UtilMisc.toMap("partyIdTo", partyId, "partyRelationshipTypeId", "GROUP_ROLLUP"), null, false));
        if (UtilValidate.isNotEmpty(lable))
            inputMap.put("lable", lable.get("partyIdFrom"));
        // 获取地址
        GenericValue postalAddress = EntityUtil.getFirst(delegator.findByAnd("findPostalAddressByPartyId",
                UtilMisc.toMap("partyId", partyId, "contactMechPurposeTypeId", "PRIMARY_LOCATION", "contactMechTypeId",
                        "POSTAL_ADDRESS"),
                null, false));
        if (UtilValidate.isNotEmpty(postalAddress)) {
            /*
			 * inputMap.put("contactAddress", "" + postalAddress.get("geoName")
			 * + " " + postalAddress.get("city") + " " +
			 * postalAddress.get("address2") + " " +
			 * postalAddress.get("address1"));
			 */
            inputMap.put("geoName", postalAddress.get("geoName"));
            inputMap.put("city", postalAddress.get("city"));
            inputMap.put("address1", postalAddress.get("address1"));
            inputMap.put("address2", postalAddress.get("address2"));
            inputMap.put("partyId", partyId);

        }
        inputMap.put("resultMsg", UtilProperties.getMessage("PersonContactsUiLabels", "success", locale));
//        inputMap.put("partyId", partyId);
        resultMap.put("resultMap", inputMap);
        return resultMap;
    }

    /**
     * 查询联系人信息
     *
     * @param dctx
     * @param context
     * @return Map
     * @throws GenericEntityException
     * @author zhangwenwen
     */

    public static Map<String, Object> findContactPerson(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        String partyId = (String) context.get("partyId");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        Map<String, Object> inputMap = new HashMap<String, Object>();
        GenericValue person = delegator.findOne("Person", false, UtilMisc.toMap("partyId", partyId));
        if (UtilValidate.isEmpty(person)) {
            resultMap = new HashMap<String, Object>();
            inputMap.put("resultMsg", UtilProperties.getMessage("PersonContactsUiLabels", "userDoesNotExist", locale));
            resultMap.put("resultMap", inputMap);
            return resultMap;
        }
        // 判断用户是否被禁用
        GenericValue party = delegator.findOne("Party", UtilMisc.toMap("partyId", partyId), false);
        if ("PARTY_DISABLED".equals(party.get("statusId"))) {
            resultMap = new HashMap<String, Object>();
            inputMap.put("resultMsg", UtilProperties.getMessage("PersonContactsUiLabels", "userDisabled", locale));
            resultMap.put("resultMap", inputMap);
            return resultMap;
        }
        // 获取姓名
        inputMap.put("personName", "" + person.get("lastName") + person.get("firstName"));

        // 获取性别
        if (UtilValidate.isNotEmpty(person.get("gender")))
            inputMap.put("gender", person.get("gender"));
        // 获取头像
        EntityCondition findConditions = EntityCondition.makeCondition(
                EntityCondition.makeCondition(UtilMisc.toMap("partyId", partyId)),
                EntityCondition.makeCondition(UtilMisc.toMap("partyContentTypeId", "LGOIMGURL")),
                EntityUtil.getFilterByDateExpr());

        GenericValue partyContent = EntityUtil.getFirst(
                delegator.findList("PartyContent", findConditions, null, UtilMisc.toList("-fromDate"), null, false));
        if (UtilValidate.isNotEmpty(partyContent)) {
            String contentId = partyContent.getString("contentId");
            inputMap.put("headPortrait",
                    "http://localhost:3400/personContacts/control/stream?contentId=" + contentId);
        }

        // 获取电话号码
        GenericValue telecomNumber = EntityUtil.getFirst(delegator.findByAnd("findTelecomNumberByPartyId",
                UtilMisc.toMap("partyId", partyId, "contactMechPurposeTypeId", "PHONE_MOBILE", "contactMechTypeId",
                        "TELECOM_NUMBER"),
                null, false));
        if (UtilValidate.isNotEmpty(telecomNumber))
            inputMap.put("contactNumber", telecomNumber.getString("contactNumber"));
        // 获取email
        GenericValue emailAddress = EntityUtil.getFirst(
                delegator.findByAnd("findEmailByPartyId", UtilMisc.toMap("partyId", partyId, "contactMechPurposeTypeId",
                        "PRIMARY_EMAIL", "contactMechTypeId", "EMAIL_ADDRESS"), null, false));
        if (UtilValidate.isNotEmpty(emailAddress))
            inputMap.put("email", emailAddress.getString("infoString"));
        // 获取公司
        GenericValue company = EntityUtil.getFirst(delegator.findByAnd("PartyAttribute",
                UtilMisc.toMap("partyId", partyId, "attrName", "Company"), null, false));
        if (UtilValidate.isNotEmpty(company))
            inputMap.put("company", company.get("attrValue"));
        // 获取标签
        GenericValue lable = EntityUtil.getFirst(delegator.findByAnd("PartyRelationship",
                UtilMisc.toMap("partyIdTo", partyId, "partyRelationshipTypeId", "GROUP_ROLLUP"), null, false));
        if (UtilValidate.isNotEmpty(lable))
            inputMap.put("lable", lable.get("partyIdFrom"));
        // 获取地址
        GenericValue postalAddress = EntityUtil.getFirst(delegator.findByAnd("findPostalAddressByPartyId",
                UtilMisc.toMap("partyId", partyId, "contactMechPurposeTypeId", "PRIMARY_LOCATION", "contactMechTypeId",
                        "POSTAL_ADDRESS"),
                null, false));
        if (UtilValidate.isNotEmpty(postalAddress)) {
            inputMap.put("geoName", postalAddress.get("stateProvinceGeoId"));
            inputMap.put("city", postalAddress.get("geoIdCity"));
            inputMap.put("address1", postalAddress.get("address1"));
            inputMap.put("address2", postalAddress.get("geoIdArea"));
        }
        inputMap.put("resultMsg", UtilProperties.getMessage("PersonContactsUiLabels", "success", locale));
        inputMap.put("partyId", partyId);
        resultMap.put("resultMap", inputMap);
        return resultMap;
    }

    /**
     * 用户地址编辑页面的查询
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @author zhangwenwen
     */
    public static Map<String, Object> showPersonAddress(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        String partyId = (String) context.get("partyId");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        Map<String, Object> inputMap = new HashMap<String, Object>();
        GenericValue person = delegator.findOne("Person", false, UtilMisc.toMap("partyId", partyId));
        if (UtilValidate.isEmpty(person)) {
            resultMap = new HashMap<String, Object>();
            inputMap.put("resultMsg", UtilProperties.getMessage("PersonContactsUiLabels", "userDoesNotExist", locale));
            resultMap.put("resultMap", inputMap);
            return resultMap;
        }
        // 判断用户是否被禁用
        GenericValue party = delegator.findOne("Party", UtilMisc.toMap("partyId", partyId), false);
        if ("PARTY_DISABLED".equals(party.get("statusId"))) {
            resultMap = new HashMap<String, Object>();
            inputMap.put("resultMsg", UtilProperties.getMessage("PersonContactsUiLabels", "userDisabled", locale));
            resultMap.put("resultMap", inputMap);
            return resultMap;
        }

        // 获取地址
        GenericValue postalAddress = EntityUtil.getFirst(delegator.findByAnd("findPostalAddressByPartyId",
                UtilMisc.toMap("partyId", partyId, "contactMechPurposeTypeId", "PRIMARY_LOCATION", "contactMechTypeId",
                        "POSTAL_ADDRESS"),
                null, false));

        if (UtilValidate.isNotEmpty(postalAddress)) {
            inputMap.put("address1", postalAddress.getString("address1"));
            inputMap.put("stateProvinceGeoId", postalAddress.getString("stateProvinceGeoId"));
            inputMap.put("geoIdCity", postalAddress.getString("geoIdCity"));
            inputMap.put("geoIdArea", postalAddress.getString("geoIdArea"));
        }

        // 获取电话号码
        GenericValue telecomNumber = EntityUtil.getFirst(delegator.findByAnd("findTelecomNumberByPartyId",
                UtilMisc.toMap("partyId", partyId, "contactMechPurposeTypeId", "PHONE_MOBILE", "contactMechTypeId",
                        "TELECOM_NUMBER"),
                null, false));
        if (UtilValidate.isNotEmpty(telecomNumber))
            inputMap.put("contactNumber", telecomNumber.getString("contactNumber"));
        // 获取email
        GenericValue emailAddress = EntityUtil.getFirst(
                delegator.findByAnd("findEmailByPartyId", UtilMisc.toMap("partyId", partyId, "contactMechPurposeTypeId",
                        "PRIMARY_EMAIL", "contactMechTypeId", "EMAIL_ADDRESS"), null, false));
        if (UtilValidate.isNotEmpty(emailAddress))
            inputMap.put("email", emailAddress.getString("infoString"));

        List<GenericValue> geoList = EntityQuery.use(delegator).from("Geo")
                .where(EntityCondition.makeCondition("geoId", EntityOperator.LIKE, "CN%"), EntityCondition
                        .makeCondition("geoTypeId", EntityOperator.IN, UtilMisc.toList("PROVINCE", "MUNICIPALITY")))
                .cache().queryList();
        // 处理省市区数据
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        for (GenericValue gv : geoList) {
            Map<String, Object> province = new HashMap<String, Object>();
            String geoId = gv.getString("geoId");

            province.put("geoId", geoId);
            province.put("geoName", gv.getString("geoName"));

            List<GenericValue> listGeoAssocAndGeoTo = delegator.findByAnd("GeoAssocAndGeoTo",
                    UtilMisc.toMap("geoIdFrom", geoId, "geoAssocTypeId", "PROVINCE_CITY"), null, true);
            List<Map<String, Object>> provinceList = new ArrayList<Map<String, Object>>();
            if (UtilValidate.isNotEmpty(listGeoAssocAndGeoTo)) {
                for (GenericValue gvga : listGeoAssocAndGeoTo) {
                    Map<String, Object> city = new HashMap<String, Object>();
                    String geoIds = gvga.getString("geoId");

                    city.put("geoId", geoIds);
                    city.put("geoName", gvga.getString("geoName"));

                    List<GenericValue> listGeoAssocAndGeoToTwo = delegator.findByAnd("GeoAssocAndGeoTo",
                            UtilMisc.toMap("geoIdFrom", geoIds, "geoAssocTypeId", "CITY_COUNTY"), null, true);
                    List<Map<String, Object>> cityList = new ArrayList<Map<String, Object>>();
                    for (GenericValue gvw : listGeoAssocAndGeoToTwo) {
                        Map<String, Object> county = new HashMap<String, Object>();
                        county.put("geoId", gvw.getString("geoId"));
                        county.put("geoName", gvw.getString("geoName"));
                        cityList.add(county);
                    }
                    city.put("child", cityList);
                    provinceList.add(city);
                }
            }
            province.put("child", provinceList);
            dataList.add(province);
        }
        inputMap.put("addressSelectData", dataList);

        resultMap.put("resultMap", inputMap);
        return resultMap;
    }

    /**
     * 编辑客户地址和联系信息
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @author zhangwenwen
     */
    public static Map<String, Object> editPersonAddress(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();

        // 用来执行service的登陆
        GenericValue userLogin = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", "admin"), true);

        // 获取context中的数据
        Locale locale = (Locale) context.get("locale");
        String partyId = (String) context.get("partyId");

        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        Map<String, Object> inputMap = new HashMap<String, Object>();
        GenericValue person = delegator.findOne("Person", false, UtilMisc.toMap("partyId", partyId));
        if (UtilValidate.isEmpty(person)) {
            resultMap = new HashMap<String, Object>();
            inputMap.put("resultMsg", UtilProperties.getMessage("PersonContactsUiLabels", "userDoesNotExist", locale));
            resultMap.put("resultMap", inputMap);
            return resultMap;
        }
        // 判断用户是否被禁用
        GenericValue party = delegator.findOne("Party", UtilMisc.toMap("partyId", partyId), false);
        if ("PARTY_DISABLED".equals(party.get("statusId"))) {
            resultMap = new HashMap<String, Object>();
            inputMap.put("resultMsg", UtilProperties.getMessage("PersonContactsUiLabels", "userDisabled", locale));
            resultMap.put("resultMap", inputMap);
            return resultMap;
        }
        // 创建和修改地址
        createOrUpdatePostalAddress(dispatcher, delegator, context, userLogin, "PRIMARY_LOCATION");

        // 创建和修改电话号码
        createOrUpdateTelecomNumber(dispatcher, delegator, context, userLogin, "PHONE_MOBILE");

        createOrUpdateEmailAddress(dispatcher, delegator, context, userLogin, "PRIMARY_EMAIL");

        resultMap = new HashMap<String, Object>();
        inputMap.put("resultMsg", UtilProperties.getMessage("PersonContactsUiLabels", "success", locale));
        resultMap.put("resultMap", inputMap);
        return resultMap;
    }

    private static void createOrUpdateEmailAddress(LocalDispatcher dispatcher, Delegator delegator,
                                                   Map<String, Object> context, GenericValue userLogin, String contactMechPurposeTypeId)
            throws GenericEntityException, GenericServiceException {
        String partyId = (String) context.get("partyId");

        // TODO 创建和修改邮件地址
        String email = (String) context.get("email");
        // 获取email
        GenericValue emailAddress = EntityUtil.getFirst(
                delegator.findByAnd("findEmailByPartyId", UtilMisc.toMap("partyId", partyId, "contactMechPurposeTypeId",
                        contactMechPurposeTypeId, "contactMechTypeId", "EMAIL_ADDRESS"), null, false));
        // 如果这里的emailAddress是不是空 ，如果不是空的就进行修改，如果是空的话就添加
        if (UtilValidate.isNotEmpty(emailAddress)) {
            String infoString = emailAddress.getString("infoString");
            if (!infoString.equals(email)) {
                String contactMechId = emailAddress.getString("contactMechId");
                dispatcher.runSync("updatePartyEmailAddress", UtilMisc.toMap("partyId", partyId, "contactMechId",
                        contactMechId, "contactMechTypeId", "EMAIL_ADDRESS", "emailAddress", email, "userLogin", userLogin));
            }
        } else {
            Map<String, Object> createEmailAddress = dispatcher.runSync("createPartyEmailAddress",
                    UtilMisc.toMap("partyId", partyId, "contactMechTypeId", "EMAIL_ADDRESS", "preContactMechTypeId",
                            "EMAIL_ADDRESS", "emailAddress", email, "userLogin", userLogin));
            String contactMechId = (String) createEmailAddress.get("contactMechId");
            // 创建联系目的   "useValues", true,
            dispatcher.runSync("createPartyContactMechPurpose",
                    UtilMisc.toMap("contactMechId", contactMechId, "partyId", partyId,
                            "contactMechPurposeTypeId", contactMechPurposeTypeId, "userLogin", userLogin));
        }
    }

    /**
     * 创建或者修改电话号码
     *
     * @param dispatcher
     * @param delegator
     * @param context
     * @param userLogin
     * @param contactMechPurposeTypeId
     * @throws GenericServiceException
     * @throws GenericEntityException
     * @author zhangwenwen
     */
    private static void createOrUpdateTelecomNumber(LocalDispatcher dispatcher, Delegator delegator,
                                                    Map<String, Object> context, GenericValue userLogin, String contactMechPurposeTypeId)
            throws GenericServiceException, GenericEntityException {

        String partyId = (String) context.get("partyId");

        // TODO 创建或者修改电话号码
        String contactNumber = (String) context.get("contactNumber");
        // 获取电话号码
        GenericValue telecomNumber = EntityUtil.getFirst(delegator.findByAnd("findTelecomNumberByPartyId",
                UtilMisc.toMap("partyId", partyId, "contactMechPurposeTypeId", contactMechPurposeTypeId,
                        "contactMechTypeId", "TELECOM_NUMBER"),
                null, false));
        // 这里判断是不是一件存在电话号码了，如果存在电话的话就进行修改，如果没有就进行创建
        if (UtilValidate.isNotEmpty(telecomNumber)) {
            String gvContactNumber = telecomNumber.getString("contactNumber");
            if (!gvContactNumber.equals(contactNumber)) {
                dispatcher.runSync("updatePartyTelecomNumber",
                        UtilMisc.toMap("contactMechId", telecomNumber.getString("contactMechId"), "partyId", partyId,
                                "contactMechTypeId", "TELECOM_NUMBER", "contactNumber", contactNumber, "userLogin",
                                userLogin));
            }
        } else {
            Map<String, Object> contactMap = dispatcher.runSync("createPartyTelecomNumber",
                    UtilMisc.toMap("partyId", partyId, "contactMechTypeId", "TELECOM_NUMBER", "preContactMechTypeId",
                            "TELECOM_NUMBER", "contactNumber", contactNumber, "userLogin", userLogin));

            String contactMechId = (String) contactMap.get("contactMechId");
            if (UtilValidate.isNotEmpty(contactMechId)) {
            }
            // , "useValues", true
            dispatcher.runSync("createPartyContactMechPurpose",
                    UtilMisc.toMap("partyId", partyId, "contactMechId", contactMechId,
                            "contactMechPurposeTypeId", contactMechPurposeTypeId, "userLogin", userLogin));
        }
    }

    /**
     * 创建和修改过地址
     *
     * @param dispatcher
     * @param delegator
     * @param context
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @author zhangwenwen
     */
    private static void createOrUpdatePostalAddress(LocalDispatcher dispatcher, Delegator delegator,
                                                    Map<String, Object> context, GenericValue userLogin, String contactMechPurposeTypeId)
            throws GenericEntityException, GenericServiceException {
        // TODO 创建和修改地址

        String partyId = (String) context.get("partyId");
        String stateProvinceGeoId = (String) context.get("stateProvinceGeoId");
        String geoIdCity = (String) context.get("geoIdCity");
        String geoIdArea = (String) context.get("geoIdArea");
        String address1 = (String) context.get("address1");
        String toName = (String) context.get("toName");
        String attnName = (String) context.get("attnName");
        String postalCode = (String) context.get("postalCode");

        // 获取地址
        GenericValue postalAddress = EntityUtil.getFirst(delegator.findByAnd("findPostalAddressByPartyId",
                UtilMisc.toMap("partyId", partyId, "contactMechPurposeTypeId", contactMechPurposeTypeId,
                        "contactMechTypeId", "POSTAL_ADDRESS"),
                null, false));
        // 这里判断地址是不是空的，如果是空的话就是创建地址，如果不是空的话就说明是修改
        if (UtilValidate.isNotEmpty(postalAddress)) {
            String gvStateProvinceGeoId = postalAddress.getString("stateProvinceGeoId");
            String gvGeoIdCity = postalAddress.getString("geoIdCity");
            String gvGeoIdArea = postalAddress.getString("geoIdArea");
            String gvAddress1 = postalAddress.getString("address1");
            // 判断是否被修改过了，如果被修改过了就进行修改，如果没有修改过的话，就说明都不动
            if (!stateProvinceGeoId.equals(gvStateProvinceGeoId) || !geoIdCity.equals(gvGeoIdCity)
                    || !geoIdArea.equals(gvGeoIdArea) || !address1.equals(gvAddress1)) {
                String contactMechId = postalAddress.getString("contactMechId");
                String gvToName = postalAddress.getString("toName");
                // 因为[市][区]在地址中保存的时候是以文本模式保存的，所以要将ID转换成文本
                GenericValue geoCity = delegator.findOne("Geo", UtilMisc.toMap("geoId", geoIdCity), true);
                String city = geoCity.getString("geoName");
                GenericValue geoArea = delegator.findOne("Geo", UtilMisc.toMap("geoId", geoIdArea), true);
                String address2 = geoArea.getString("geoName");

                if (UtilValidate.isNotEmpty(toName))
                    toName = gvToName;
                if (UtilValidate.isNotEmpty(attnName))
                    attnName = postalAddress.getString("attnName");
                if (UtilValidate.isNotEmpty(postalCode))
                    postalCode = postalAddress.getString("postalCode");

                Map<String, Object> inputService = new HashMap<String, Object>();
                inputService.put("contactMechId", contactMechId);
                inputService.put("contactMechTypeId", "POSTAL_ADDRESS");
                inputService.put("partyId", partyId);
                inputService.put("address1", address1);
                inputService.put("address2", address2);
                inputService.put("city", city);
                inputService.put("stateProvinceGeoId", stateProvinceGeoId);
                inputService.put("countryGeoId", postalAddress.getString("countryGeoId"));
                inputService.put("userLogin", userLogin);

                if (UtilValidate.isNotEmpty(toName))
                    inputService.put("toName", toName);
                if (UtilValidate.isNotEmpty(attnName))
                    inputService.put("attnName", attnName);
                if (UtilValidate.isNotEmpty(postalCode))
                    inputService.put("postalCode", postalCode);
                else
                    inputService.put("postalCode", "111111");

                dispatcher.runSync("updatePartyPostalAddress", inputService);
            }
        } else {
            GenericValue geoCity = delegator.findOne("Geo", UtilMisc.toMap("geoId", geoIdCity), true);
            String city = geoCity.getString("geoName");
            GenericValue geoArea = delegator.findOne("Geo", UtilMisc.toMap("geoId", geoIdArea), true);
            String address2 = geoArea.getString("geoName");
            Map<String, Object> inputService = new HashMap<String, Object>();
            inputService.put("contactMechTypeId", "POSTAL_ADDRESS");
            inputService.put("partyId", partyId);
            inputService.put("userLogin", userLogin);
            inputService.put("preContactMechTypeId", "POSTAL_ADDRESS");
            inputService.put("address1", address1);
            inputService.put("address2", address2);
            inputService.put("city", city);
            inputService.put("stateProvinceGeoId", stateProvinceGeoId);
            inputService.put("countryGeoId", "CHN");
            if (UtilValidate.isNotEmpty(toName))
                inputService.put("toName", toName);
            if (UtilValidate.isNotEmpty(attnName))
                inputService.put("attnName", attnName);
            if (UtilValidate.isNotEmpty(postalCode))
                inputService.put("postalCode", postalCode);
            else
                inputService.put("postalCode", "111111");
            Map<String, Object> resultMap = dispatcher.runSync("createPartyPostalAddress", inputService);
            // 创建联系目的  , "useValues", true
            String contactMechId = (String) resultMap.get("contactMechId");
            if (UtilValidate.isNotEmpty(contactMechId)) {
                dispatcher.runSync("createPartyContactMechPurpose",
                        UtilMisc.toMap("partyId", partyId, "contactMechId", contactMechId,
                                "contactMechPurposeTypeId", contactMechPurposeTypeId, "userLogin", userLogin));
            }
        }
    }

    /**
     * 查询联系人列表
     *
     * @param dctx
     * @param context
     * @return Map
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> findContactsInfo(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = userLogin==null? (String)context.get("partyId"):(String)userLogin.get("partyId");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        Map<String, Object> inputMap = new HashMap<String, Object>();
        // 查询联系人partyId
        EntityCondition findConditions = null;
        findConditions = EntityCondition
                .makeCondition(UtilMisc.toMap("partyIdTo", partyId, "partyRelationshipTypeId", "CONTACT_REL"));
        List<GenericValue> contactPartyIdList = null;
        contactPartyIdList = delegator.findList("PartyRelationship", findConditions, UtilMisc.toSet("partyIdFrom"),
                UtilMisc.toList("-fromDate"), null, false);
        List<Object> contactList = new ArrayList<Object>();
        // 遍历获得的联系人partyId，同步findPersonInfo获得联系人信息
        if (UtilValidate.isNotEmpty(contactPartyIdList)) {
            for (GenericValue contactParty : contactPartyIdList) {
                Map<String, Object> inputFieldMap = UtilMisc.toMap();
                inputFieldMap.put("partyId", contactParty.get("partyIdFrom"));
                Map<String, Object> catactMap = new HashMap<String, Object>();
                catactMap = dispatcher.runSync("findPersonInfo", inputFieldMap);
                contactList.add(catactMap.get("resultMap"));
            }
        }
        inputMap.put("contact", contactList);
        resultMap.put("resultMap", inputMap);
        return resultMap;
    }




    /**
     * 联系人公开的列表
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     */
    public static Map<String, Object> findContactsInfoActivity(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String userLoginId = (String) userLogin.get("userLoginId");



//        Map<String, Object> findContactResult = UtilMisc.toMap("userLogin", userLogin, "partyId", partyId, "roleTypeId", "ACTIVITY_MEMBER", "statusId", "PRTYASGN_ASSIGNED", "workEffortId", workEffortId);
//
//        dispatcher.runSync("findContactsInfo", findContactResult);

        return null;
    }


    /**
     * 查询用户拥有的标签
     *
     * @param dctx
     * @param context
     * @return Map
     * @throws GenericEntityException
     */
    public static Map<String, Object> findLable(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        String userLoginId = (String) context.get("userLoginId");
        Map<String, Object> result = ServiceUtil.returnSuccess();
        Map<String, Object> inputMap = new HashMap<String, Object>();
        List<GenericValue> lableList = new ArrayList<GenericValue>();
        // 通过userLoginId，查询用户创建的PARTY_GROUP，返回groupName
        EntityCondition findConditions = null;
        findConditions = EntityCondition
                .makeCondition(UtilMisc.toMap("createdByUserLogin", userLoginId, "partyTypeId", "PARTY_GROUP"));
        lableList = delegator.findList("PartyAndGroup", findConditions,
                UtilMisc.toSet("partyId", "groupName", "statusId"), null, null, false);
        List<GenericValue> contactList = new ArrayList<GenericValue>();
        if (UtilValidate.isNotEmpty(lableList)) {
            for (GenericValue lable : lableList) {
                // 判断标签是否被禁用，如果禁用忽略。
                if (!"PARTY_DISABLED".equals(lable.get("statusId"))) {
                    contactList.add(lable);
                }
            }
        }
        inputMap.put("lable", contactList);
        result.put("resultMap", inputMap);
        return result;
    }

    /**
     * 查询标签内成员
     *
     * @param dctx
     * @param context
     * @return Map
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> findLablePerson(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        String partyId = (String) context.get("partyId");
        Map<String, Object> result = ServiceUtil.returnSuccess();
        Map<String, Object> inputMap = new HashMap<String, Object>();
        List<GenericValue> lableList = new ArrayList<GenericValue>();
        // 查询标签内成员的partyId
        EntityCondition findConditions = null;
        findConditions = EntityCondition.makeCondition(
                EntityCondition.makeCondition(
                        UtilMisc.toMap("partyIdFrom", partyId, "partyRelationshipTypeId", "GROUP_ROLLUP")),
                EntityUtil.getFilterByDateExpr());
        lableList = delegator.findList("PartyRelationship", findConditions, UtilMisc.toSet("partyIdTo"), null, null,
                false);
        List<Object> lables = new ArrayList<Object>();
        // 遍历查到的partyId，同步findPersonInfo获得成员信息
        if (UtilValidate.isNotEmpty(lableList)) {
            for (GenericValue lable : lableList) {
                Map<String, Object> inputFieldMap = UtilMisc.toMap();
                inputFieldMap.put("partyId", lable.get("partyIdTo"));
                Map<String, Object> findResult = null;
                findResult = dispatcher.runSync("findPersonInfo", inputFieldMap);
                lables.add(findResult.get("resultMap"));
            }
        }
        inputMap.put("person", lables);
        result.put("resultMap", inputMap);
        return result;
    }


    /**
     * 查询我的活动项列表
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     */
    public static Map<String, Object> findActivityProjects(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> inputMap = new HashMap<String, Object>();
        String workEffortId = (String) context.get("workEffortId");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        //查询活动项关系
        EntityCondition findConditions = null;
        findConditions = EntityCondition
                .makeCondition(UtilMisc.toMap("workEffortIdTo", workEffortId));
        List<GenericValue> projectList = null;

        projectList = delegator.findList("ActivityAndProject", findConditions, UtilMisc.toSet("workEffortIdTo", "workEffortName", "actualStartDate", "locationDesc"),
                null, null, false);
        inputMap.put("projectList",projectList);
        inputMap.put("resultMsg", UtilProperties.getMessage("PersonContactsUiLabels", "success", locale));
        resultMap.put("resultMap", inputMap);
        return resultMap;
    }


    /**
     * 我的活动详情
     * @Author
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     */
    public static Map<String, Object> findMyEventDetail(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = (String) userLogin.get("partyId");
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> inputMap = new HashMap<String, Object>();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        String workEffortId = (String) context.get("workEffortId");

        GenericValue createPerson = delegator.findOne("Person",UtilMisc.toMap("partyId",partyId),false);
        //活动详情
        EntityCondition findConditions = null;
        findConditions = EntityCondition
                .makeCondition(UtilMisc.toMap("workEffortId", workEffortId));
        List<GenericValue> eventsDetail = null;

        eventsDetail = delegator.findList("WorkEffort", findConditions, UtilMisc.toSet("workEffortId","workEffortName","actualStartDate","description","locationDesc","estimatedCompletionDate"),
                null, null, false);

        //参与的人员其实是头像列表
        EntityCondition findConditionsToPartyContent = null;
        findConditionsToPartyContent = EntityCondition
                .makeCondition(UtilMisc.toMap("workEffortId",workEffortId));
        List<GenericValue> partyJoinEventsList = null;
        partyJoinEventsList = delegator.findList("WorkEffortPartyAssignmentAndJoinParty",findConditionsToPartyContent,UtilMisc.toSet("workEffortId","partyId","contentId"),
                null, null, false);
        List<Map<String,Object>> partyContent = new ArrayList<Map<String, Object>>();
        if(null!=partyJoinEventsList)
        for(GenericValue gv : partyJoinEventsList){
            Map<String,Object> partyContentMap = new HashMap<String, Object>();
            partyContentMap.put("partyId",
                    gv.get("partyId"));
            partyContentMap.put("headPortrait",
                    "http://localhost:3400/personContacts/control/stream?contentId=" + gv.get("contentId"));
            partyContent.add(partyContentMap);
        }

        //子活动列表
        List<GenericValue> childActivityList = delegator.findList("WorkEffort",EntityCondition
                .makeCondition(UtilMisc.toMap("workEffortParentId",workEffortId)),null,
                null, null, false);


        //TODO 优化鉴权 ,我是不是组织者
        EntityCondition findPermissionConditions   = EntityCondition
                .makeCondition(UtilMisc.toMap("partyId", partyId,"workEffortId",workEffortId));

        List<GenericValue> whoAmI = null;
        Set<String> seletField = new HashSet<String>();
        seletField.add("partyId");
        seletField.add("workEffortId");
        whoAmI = delegator.findList("WorkEffortAndPartyAssign", findPermissionConditions,seletField,null, null, false);
        if(whoAmI!=null && whoAmI.size()>1){
            inputMap.put("iAmAdmin","Y");
        }else{
            inputMap.put("iAmAdmin","N");
        }
        List<GenericValue> personList = new ArrayList<GenericValue>();
        personList.add(createPerson);
        inputMap.put("createPersonInfoList", personList);
        inputMap.put("eventDetail", eventsDetail);
        inputMap.put("partyJoinEventsList",partyContent);
        inputMap.put("childActivityList",childActivityList);
        inputMap.put("partyId",partyId);
        inputMap.put("resultMsg", UtilProperties.getMessage("PersonContactsUiLabels", "success", locale));
        resultMap.put("resultMap", inputMap);
        return resultMap;
    }







    /**
     * 查询我的活动
     *
     * @param dctx
     * @param context
     * @return Map
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @author S
     */
    public static Map<String, Object> findMyEvent(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> inputMap = new HashMap<String, Object>();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = (String) userLogin.get("partyId");
        String roleTypeId = (String) context.get("roleTypeId");

        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        //GenericValue person = delegator.findOne("Person", false, UtilMisc.toMap("partyId", partyId));
        // 查询联系人partyId
        EntityCondition findConditions = null;

            findConditions = EntityCondition
                    .makeCondition(UtilMisc.toMap("partyId", partyId,"roleTypeId",roleTypeId,"workEffortTypeId","Event"));

        List<GenericValue> partyEventsList = null;
//        partyEventsList = delegator.findList("MarketingCampaignAndRole", findConditions, UtilMisc.toSet("partyId","campaignName","startDate"),
//                UtilMisc.toList("-startDate"), null, false);
        Set<String> seletField = new HashSet<String>();
        seletField.add("partyId");
        seletField.add("workEffortName");
        seletField.add("actualStartDate");
        seletField.add("description");
        seletField.add("locationDesc");
        seletField.add("estimatedCompletionDate");
        seletField.add("workEffortId");
        partyEventsList = delegator.findList("WorkEffortAndPartyAssign", findConditions,seletField,UtilMisc.toList("-actualStartDate"), null, false);

        inputMap.put("partyEventsList",partyEventsList);
        inputMap.put("resultMsg", UtilProperties.getMessage("PersonContactsUiLabels", "success", locale));
        resultMap.put("resultMap", inputMap);
        return resultMap;
    }
}
