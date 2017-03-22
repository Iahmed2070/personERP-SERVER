package main.java.com.banfftech.personerp.activity;


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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by S.Y.L on 2017/3/14.
 */
public class ActivityServices {


    public static final String module = ActivityServices.class.getName();




    /**
     * Create PaymentInfo
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws InterruptedException
     */
    public static Map<String, Object> createPartyPaymentInfo(DispatchContext dctx, Map<String, ? extends Object> context)
            throws  GenericEntityException, GenericServiceException, InterruptedException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        Map<String, Object> inputMap = new HashMap<String, Object>();



        //Scope Param
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        //组织者
        String partyIdTo = (String) userLogin.get("partyId");
        //参与人
        String partyIdFrom = (String) context.get("partyIdFrom");
        //应付
        String invoiceApplied =   context.get("invoiceApplied")==null?"0":(String) context.get("invoiceApplied");
        //活动
        String workEffortId = (String) context.get("workEffortId");
        //实缴
        String amountApplied =  context.get("amountApplied")==null?"0":(String) context.get("amountApplied");
        // 时间
        String payDate = (String) context.get("payDate");



        //Do Insert
        GenericValue newPay = delegator.makeValue("PartyPaymentInfo");
        newPay.set("tmpId", delegator.getNextSeqId("PartyPaymentInfo"));
        newPay.set("partyIdTo", partyIdTo);
        newPay.set("partyIdFrom", partyIdFrom);
        newPay.set("invoiceApplied", new BigDecimal(invoiceApplied) );
        newPay.set("workEffortId", workEffortId);
        newPay.set("amountApplied", new BigDecimal(amountApplied));
        newPay.set("payDate",payDate);
        newPay.create();



        //Service Foot
        inputMap.put("resultMsg", UtilProperties.getMessage("PersonActivityUiLabels", "success", locale));
        resultMap.put("resultMap",inputMap);
        return resultMap;
    }




    /**
     * Create Activity Project
     *
     * @param dctx
     * @param context
     * @return
     * @throws IOException
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws InterruptedException
     */
    public static Map<String, Object> createActivityProject(DispatchContext dctx, Map<String, ? extends Object> context)
            throws IOException, GenericEntityException, GenericServiceException, InterruptedException {


        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        Locale locale = (Locale) context.get("locale");




        //Scope Param
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        //主活动的Id
        String workEffortId = (String) context.get("workEffortId");
        //活动名称
        String workEffortName = (String) context.get("workEffortName");
        //简介
        String description = (String) context.get("description");
        //地址
        String locationDesc = (String) context.get("locationDesc");
        //活动时间
        String actualStartDate = (String) context.get("actualStartDate");
        //预计完成时间
        String estimatedCompletionDate = (String) context.get("estimatedCompletionDate");



        Timestamp tm = null;
        Timestamp tmend = null;
        if (actualStartDate != null) {
            tm = Timestamp.valueOf(actualStartDate);
        }
        if (estimatedCompletionDate != null) {
            tmend = Timestamp.valueOf(estimatedCompletionDate);
        }

        Map<String, Object> createWorkEffortMap = null;
        createWorkEffortMap = UtilMisc.toMap("userLogin", userLogin, "currentStatusId", "CAL_IN_PLANNING", "workEffortName", workEffortName, "workEffortTypeId", "EVENT", "description", description, "actualStartDate", actualStartDate, "locationDesc", locationDesc);
        Map<String, Object> serviceResultByCreateWorkEffortMap = dispatcher.runSync("createWorkEffort", createWorkEffortMap);
        //NEW WORKEFFORT_ID
        String activityProjectId = (String) serviceResultByCreateWorkEffortMap.get("workEffortId");

        //与主活动进行关联	WORK_EFF_DEPENDENCY
        Map<String, Object> createWorkEffortAssocMap = UtilMisc.toMap("userLogin", userLogin,"workEffortAssocTypeId","WORK_EFF_DEPENDENCY","workEffortIdFrom",activityProjectId,"workEffortIdTo",workEffortId);
        dispatcher.runSync("createWorkEffortAssoc", createWorkEffortAssocMap);


        Map<String, Object> inputMap = new HashMap<String, Object>();
        inputMap.put("projectId", activityProjectId);
        inputMap.put("workEffortName", workEffortName);
        inputMap.put("actualStartDate", actualStartDate);

        Map<String, Object> result = ServiceUtil.returnSuccess();
        result.put("resultMap", inputMap);
        inputMap.put("resultMsg", UtilProperties.getMessage("PersonActivityUiLabels", "success", locale));
        return result;
    }




    /**
     * Create New Activity
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     *
     */
    public static Map<String, Object> createNewEvent(DispatchContext dctx, Map<String, ? extends Object> context)
            throws IOException, GenericEntityException, GenericServiceException, InterruptedException {


        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> inputMap = new HashMap<String, Object>();

        //Scope Param
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        //创建人
        String partyId = (String) userLogin.get("partyId");
        //活动名称
        String workEffortName = (String) context.get("workEffortName");
        //简介
        String description = (String) context.get("description");
        //地址
        String locationDesc = (String) context.get("locationDesc");
        //活动时间
        String actualStartDate = (String) context.get("actualStartDate");
        //预计完成时间
        String estimatedCompletionDate = (String) context.get("estimatedCompletionDate");
        //父活动Id
        String  parentId = (String)  context.get("parentId");
        //可见范围
        String scopeEnumId = (String) context.get("scopeEnumId");
        if (scopeEnumId == null) {
            scopeEnumId = "WES_PUBLIC";
        }

        //经纬度(临时使用)
        String specialTerms = (String) context.get("specialTerms");

        Timestamp tm = null;
        Timestamp tmend = null;

        if (actualStartDate != null && !actualStartDate.trim().equals("")) {
            tm = Timestamp.valueOf(actualStartDate);
        }
        if (estimatedCompletionDate != null && !estimatedCompletionDate.trim().equals("")) {
            tmend = Timestamp.valueOf(estimatedCompletionDate);
        }

        Map<String, Object> createWorkEffortMap = null;
        if(parentId!=null){//代表这是一个子活动
            createWorkEffortMap = UtilMisc.toMap("userLogin", userLogin,"workEffortParentId",parentId, "currentStatusId", "CAL_IN_PLANNING", "workEffortName", workEffortName, "workEffortTypeId", "EVENT", "description", description, "locationDesc", locationDesc,"specialTerms",specialTerms);
        }else {
            createWorkEffortMap = UtilMisc.toMap("userLogin", userLogin, "currentStatusId", "CAL_IN_PLANNING", "workEffortName", workEffortName, "workEffortTypeId", "EVENT", "description", description,"locationDesc",locationDesc, "scopeEnumId", scopeEnumId,"specialTerms",specialTerms);
        }
        if(tm!=null){
            createWorkEffortMap.put("actualStartDate", actualStartDate);
        }
        if(tmend!=null){
            createWorkEffortMap.put("estimatedCompletionDate", tmend);
        }


        Map<String, Object> serviceResultByCreateWorkEffortMap = dispatcher.runSync("createWorkEffort", createWorkEffortMap);

        //NEW WORKEFFORT_ID
        String workEffortId = (String) serviceResultByCreateWorkEffortMap.get("workEffortId");

        //Create Party Role
        GenericValue isExsitsAdmin = delegator.findOne("PartyRole", UtilMisc.toMap("partyId", partyId, "roleTypeId", "ACTIVITY_ADMIN"), false);
        GenericValue isExsitsMember = delegator.findOne("PartyRole", UtilMisc.toMap("partyId", partyId, "roleTypeId", "ACTIVITY_MEMBER"), false);

        Map<String, Object> createPartyRoleAdminMap = UtilMisc.toMap("userLogin", userLogin, "partyId", partyId, "roleTypeId", "ACTIVITY_ADMIN");
        Map<String, Object> createPartyRoleMemberMap = UtilMisc.toMap("userLogin", userLogin, "partyId", partyId, "roleTypeId", "ACTIVITY_MEMBER");
        if (null == isExsitsAdmin) {
            dispatcher.runSync("createPartyRole", createPartyRoleAdminMap);
        }
        if (null == createPartyRoleMemberMap) {
            dispatcher.runSync("createPartyRole", createPartyRoleMemberMap);
        }


        //4.assignPartyToWorkEffort
        Map<String, Object> createAdminAssignPartyMap = UtilMisc.toMap("userLogin", userLogin, "partyId", partyId, "roleTypeId", "ACTIVITY_ADMIN", "statusId", "PRTYASGN_ASSIGNED", "workEffortId", workEffortId);
        Map<String, Object> createMemberAssignPartyMap = UtilMisc.toMap("userLogin", userLogin, "partyId", partyId, "roleTypeId", "ACTIVITY_MEMBER", "statusId", "PRTYASGN_ASSIGNED", "workEffortId", workEffortId);
        dispatcher.runSync("assignPartyToWorkEffort", createAdminAssignPartyMap);
        dispatcher.runSync("assignPartyToWorkEffort", createMemberAssignPartyMap);





        //Service Foot
        inputMap.put("workEffortId", workEffortId);
        Map<String, Object> result = ServiceUtil.returnSuccess();
        result.put("resultMap", inputMap);
        inputMap.put("resultMsg", UtilProperties.getMessage("PersonActivityUiLabels", "success", locale));
        return result;
    }


    /**
     * Party Join
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws UnsupportedEncodingException
     * @author S.Y.L
     */
    public static Map<String, Object> newPartyJoinActivity(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, UnsupportedEncodingException,GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Locale locale = (Locale) context.get("locale");
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();


        //Scope Param
        GenericValue userLogin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
        String workEffortId = (String) context.get("workEffortId");
        String tel = (String) context.get("tel");
        String nickName = (String) context.get("nickName");
        //JoinParty
        GenericValue joinParty = null;


        //1.Check USER IS EXSITS
        GenericValue userLoginExsit = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", tel));

        //IF NOT EXSITS DO CREATE
        if(null==userLoginExsit){
            //创建Party Person
            Map<String, Object> createPartyInMap =
                    UtilMisc.toMap("userLogin", userLogin,
                            "nickname",nickName,
                            "firstName","设置",
                            "lastName","未");
            Map<String, Object> createPerson = null;
            createPerson = dispatcher.runSync("createUpdatePerson", createPartyInMap);
            String partyId = (String) createPerson.get("partyId");

            //CreateUserLogin
            Map<String, Object> createUserLoginInMap =
                    UtilMisc.toMap("userLogin", userLogin,
                            "userLoginId",tel,
                            "partyId",partyId,
                            "currentPassword","ofbiz","currentPasswordVerify","ofbiz"
                    );

            Map<String, Object> createUserLogin = null;
            createUserLogin = dispatcher.runSync("createUserLogin", createUserLoginInMap);

            //2.GRANT ABOUT PE SOME PERMISSION
            Map<String, Object> createPartyRoleMemberMap = UtilMisc.toMap("userLogin", userLogin, "partyId", partyId, "roleTypeId", "ACTIVITY_MEMBER");
            dispatcher.runSync("createPartyRole", createPartyRoleMemberMap);
            createPartyRoleMemberMap =  UtilMisc.toMap("userLogin", userLogin, "partyId", partyId, "roleTypeId", "ACTIVITY_ADMIN");
            dispatcher.runSync("createPartyRole", createPartyRoleMemberMap);
            createPartyRoleMemberMap =  UtilMisc.toMap("userLogin", userLogin, "partyId", partyId, "roleTypeId", "ACTIVITY_INVITATION");
            dispatcher.runSync("createPartyRole", createPartyRoleMemberMap);
            Map<String, Object> createPartyRoleSecurityGroupMap = UtilMisc.toMap("userLogin", userLogin, "userLoginId",tel, "groupId", "FULLADMIN");
            dispatcher.runSync("addUserLoginToSecurityGroup", createPartyRoleSecurityGroupMap);
            joinParty = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", tel));
        }else{
            joinParty = userLoginExsit;
        }

        //Join Activity
        Map<String, Object> translationActivityMap = UtilMisc.toMap("userLogin", joinParty, "workEffortId", workEffortId);
        Map<String, Object> transaltionResult = dispatcher.runSync("translationActivity", translationActivityMap);


        //Service Foot
        resultMap.put("workEffortId",workEffortId);
        return resultMap;
    }
}
