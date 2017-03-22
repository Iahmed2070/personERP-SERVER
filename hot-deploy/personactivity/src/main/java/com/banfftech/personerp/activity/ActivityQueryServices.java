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

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by S.Y.L on 2017/3/22.
 */
public class ActivityQueryServices {

    public static final String module = ActivityQueryServices.class.getName();



    /**
     * 活动的账务列表
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @author S.Y.L
     */
    public static Map<String, Object> queryActivityPayment(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException {


        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        Map<String, Object> inputMap = new HashMap<String, Object>();




        //Scope Param
        String workEffortId = (String) context.get("workEffortId");
        GenericValue userLogin = (GenericValue) context.get("userLogin");



        //Query PartyPaymentInfoAndPerson
        EntityCondition findConditionsToPayList  = EntityCondition
                .makeCondition(UtilMisc.toMap("workEffortId", workEffortId));
        List<GenericValue> payList = delegator.findList("PartyPaymentInfoAndPerson", findConditionsToPayList, null,
                null, null, false);






        //Service Foot
        inputMap.put("payList",payList);
        inputMap.put("resultMsg", UtilProperties.getMessage("PersonContactsUiLabels", "success", locale));
        resultMap.put("resultMap", inputMap);
        return resultMap;
    }






    /**
     * Query Activity List
     *
     * @param dctx
     * @param context
     * @return Map
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @author S.Y.L
     */
    public static Map<String, Object> queryMyEvent(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> inputMap = new HashMap<String, Object>();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();



        //Scope Param
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = (String) userLogin.get("partyId");
        String roleTypeId = (String) context.get("roleTypeId");




        //Query Activity Conditions
        EntityCondition findConditions  = EntityCondition
                .makeCondition(UtilMisc.toMap("partyId", partyId,"roleTypeId",roleTypeId,"workEffortTypeId","Event"));

        EntityCondition findConditions2 =  EntityCondition
                .makeCondition("workEffortParentId", EntityOperator.EQUALS, GenericEntity.NULL_FIELD);

        EntityConditionList<EntityCondition> activityConditions = EntityCondition
                .makeCondition(findConditions,findConditions2);



        List<GenericValue> partyEventsList = null;
        //Select Fields
        Set<String> seletField = new HashSet<String>();
        seletField.add("partyId");
        seletField.add("workEffortName");
        seletField.add("actualStartDate");
        seletField.add("description");
        seletField.add("locationDesc");
        seletField.add("estimatedCompletionDate");
        seletField.add("workEffortId");
        partyEventsList = delegator.findList("WorkEffortAndPartyAssign", activityConditions,seletField,UtilMisc.toList("-createdDate"), null, false);




        //Service Foot
        inputMap.put("partyEventsList",partyEventsList);
        inputMap.put("resultMsg", UtilProperties.getMessage("PersonActivityUiLabels", "success", locale));
        resultMap.put("resultMap", inputMap);
        return resultMap;
    }







    /**
     * Query Event Detail Info
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @author S.Y.L
     */
    public static Map<String, Object> queryMyEventDetail(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> inputMap = new HashMap<String, Object>();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();


        //Scope Param
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = (String) userLogin.get("partyId");
        String workEffortId = (String) context.get("workEffortId");
        GenericValue createPerson = delegator.findOne("Person",UtilMisc.toMap("partyId",partyId),false);



        //Conditions
        EntityCondition findConditions = null;
        findConditions = EntityCondition
                .makeCondition(UtilMisc.toMap("workEffortId", workEffortId));
        List<GenericValue> eventsDetail = null;
        //Select Fields
        Set<String> fieldSet = new HashSet<String>();
        fieldSet.add("workEffortId");
        fieldSet.add("workEffortName");
        fieldSet.add("actualStartDate");
        fieldSet.add("description");
        fieldSet.add("locationDesc");
        fieldSet.add("estimatedCompletionDate");
        fieldSet.add("specialTerms");
        eventsDetail = delegator.findList("WorkEffort", findConditions, fieldSet,
                null, null, false);
        //Activity Person List
        EntityCondition findConditionsToPartyContent  = EntityCondition
                .makeCondition(UtilMisc.toMap("workEffortId",workEffortId));
        List<GenericValue> partyJoinEventsList = delegator.findList("WorkEffortPartyAssignmentAndJoinParty",findConditionsToPartyContent,UtilMisc.toSet("workEffortId","partyId","nickname","lastName","firstName"),
                null, null, false);




        //Query Person Content
        List<Map<String,Object>> partyContent = new ArrayList<Map<String, Object>>();
        if(null!=partyJoinEventsList)
            for(GenericValue gv : partyJoinEventsList){
                Map<String,Object> partyContentMap = new HashMap<String, Object>();
                partyContentMap.put("partyId",
                        gv.get("partyId"));
                partyContentMap.put("nickName",
                        gv.get("nickname"));
                partyContentMap.put("lastName",
                        gv.get("lastName"));
                partyContentMap.put("firstName",
                        gv.get("firstName"));

                //TODO OSS Logic : partyContentMap.put("headPortrait","http://localhost:3400/personContacts/control/stream?contentId=" + gv.get("contentId"));

                partyContent.add(partyContentMap);
            }

        //Sub Activity List
        List<GenericValue> childActivityList = delegator.findList("WorkEffort",EntityCondition
                        .makeCondition(UtilMisc.toMap("workEffortParentId",workEffortId)),null,
                null, null, false);


        //Check Permission
        EntityCondition findPermissionConditions   = EntityCondition
                .makeCondition(UtilMisc.toMap("partyId", partyId,"workEffortId",workEffortId));

        //Select Fields
        Set<String> seletField = new HashSet<String>();
        seletField.add("partyId");
        seletField.add("workEffortId");

        List<GenericValue> whoAmI = delegator.findList("WorkEffortAndPartyAssign", findPermissionConditions,seletField,null, null, false);

        if(whoAmI!=null && whoAmI.size()>1){
            inputMap.put("iAmAdmin","Y");
        }else{
            inputMap.put("iAmAdmin","N");
        }
        List<GenericValue> personList = new ArrayList<GenericValue>();
        personList.add(createPerson);





        //Service Foot
        inputMap.put("createPersonInfoList", personList);
        inputMap.put("eventDetail", eventsDetail);
        inputMap.put("partyJoinEventsList",partyContent);
        inputMap.put("childActivityList",childActivityList);
        inputMap.put("partyId",partyId);
        inputMap.put("resultMsg", UtilProperties.getMessage("PersonActivityUiLabels", "success", locale));
        resultMap.put("resultMap", inputMap);
        return resultMap;
    }





    /**
     * Query Activity Projects
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @author S.Y.L
     */
    public static Map<String, Object> queryActivityProjects(DispatchContext dctx, Map<String, Object> context)
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


}
