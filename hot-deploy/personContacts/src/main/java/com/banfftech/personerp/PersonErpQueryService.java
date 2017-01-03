package com.banfftech.personerp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

public class PersonErpQueryService {
	public static final String module = PersonErpQueryService.class.getName();
	
	/**
	 * 查询用户信息
	 * @param dctx
	 * @param context
	 * @return Map
	 * @throws GenericEntityException 
	 */
	public static Map<String, Object> findPersonInfo(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException {
		LocalDispatcher dispatcher = dctx.getDispatcher();
		Delegator delegator = dispatcher.getDelegator();
		Locale locale = (Locale) context.get("locale");
		String partyId = (String) context.get("partyId");
		Map<String, Object> resultMap = ServiceUtil.returnSuccess();
		Map<String, Object> inputMap = new HashMap<String, Object>();
		GenericValue person = delegator.findOne("Person", false, UtilMisc.toMap("partyId", partyId));
		if(UtilValidate.isEmpty(person)){
			resultMap = new HashMap<String, Object>();
			inputMap.put("resultMsg", UtilProperties.getMessage("PersonContactsUiLabels", "userDoesNotExist", locale));
			resultMap.put("resultMap", inputMap);
			return resultMap;
			
		}
		//判断用户是否被禁用
		GenericValue party = delegator.findOne("Party", UtilMisc.toMap("partyId", partyId), false);
		if("PARTY_DISABLED".equals(party.get("statusId"))){
			resultMap = new HashMap<String, Object>();
			inputMap.put("resultMsg", UtilProperties.getMessage("PersonContactsUiLabels", "userDisabled", locale));
			resultMap.put("resultMap", inputMap);
			return resultMap;
		}
		//获取姓名
		inputMap.put("personName", ""+person.get("lastName")+person.get("firstName"));
		//获取性别
		String gender = "" ;
		if(UtilValidate.isNotEmpty(person.get("gender")))gender = "M".equals(person.get("gender"))?"男":"女";
		inputMap.put("gender", gender);
		//获取电话号码
		GenericValue telecomNumber = EntityUtil.getFirst(delegator.findByAnd("findTelecomNumberByPaytyId", UtilMisc.toMap("partyId", partyId, "contactMechPurposeTypeId", "PHONE_MOBILE", "contactMechTypeId", "TELECOM_NUMBER"), null, false));
		if(UtilValidate.isNotEmpty(telecomNumber))inputMap.put("contactNumber", telecomNumber.getString("contactNumber"));
		//获取email
		GenericValue emailAddress = EntityUtil.getFirst(delegator.findByAnd("findEmailByPartyId", UtilMisc.toMap("partyId", partyId, "contactMechPurposeTypeId", "PRIMARY_EMAIL", "contactMechTypeId", "EMAIL_ADDRESS"), null, false));
		if(UtilValidate.isNotEmpty(emailAddress))inputMap.put("email", emailAddress.getString("infoString"));
		//获取地址
		GenericValue postalAddress = EntityUtil.getFirst(delegator.findByAnd("findPostalAddressByPaytyId", UtilMisc.toMap("partyId", partyId, "contactMechPurposeTypeId", "PRIMARY_LOCATION", "contactMechTypeId", "POSTAL_ADDRESS"), null, false));
		if(UtilValidate.isNotEmpty(postalAddress))inputMap.put("contactAddress", ""+postalAddress.get("geoName")+" "+postalAddress.get("city")+" "+postalAddress.get("address2")+" "+postalAddress.get("address1"));
		if(UtilValidate.isNotEmpty(emailAddress))inputMap.put("email", emailAddress.getString("infoString"));
		
		inputMap.put("resultMsg", UtilProperties.getMessage("PersonContactsUiLabels", "success", locale));
		resultMap.put("resultMap", inputMap);
		return resultMap;
	}
	/**
	 * 查询联系人信息
	 * @param dctx
	 * @param context
	 * @return Map
	 * @throws GenericEntityException 
	 * @throws GenericServiceException 
	 */
	public static Map<String, Object> findContectsInfo(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException{
		LocalDispatcher dispatcher = dctx.getDispatcher();
		Delegator delegator = dispatcher.getDelegator();
		Locale locale = (Locale) context.get("locale");
		String partyId = (String) context.get("partyId");
		//查询联系人partyId
		EntityCondition findConditions=null;
		findConditions=EntityCondition.makeCondition(UtilMisc.toMap("partyIdTo",partyId));
		List<GenericValue> contactPartyIdList=null;
		contactPartyIdList = delegator.findList("PartyRelationship", findConditions, UtilMisc.toSet("partyIdFrom"), null, null, false);
		List<String> partyIdList = new ArrayList<String>();
		if(UtilValidate.isNotEmpty(contactPartyIdList)){
			for(GenericValue contactParty:contactPartyIdList){
				Map<String, Object> inputFieldMap = UtilMisc.toMap(); 
				inputFieldMap.put("partyId", contactParty.get("partyIdFrom"));
				Map<String, Object> findResult = null;
				findResult=dispatcher.runSync("findPersonInfo", inputFieldMap);
				partyIdList.add(findResult.toString());
			}
		}
		Map<String, Object> result = ServiceUtil.returnSuccess();
		result.put("contactList", partyIdList);
		return result;
	}
	/**
	 * 查询用户拥有的标签
	 * @param dctx
	 * @param context
	 * @return Map
	 * @throws GenericEntityException 
	 */
	public static Map<String, Object> findLable(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException{
		LocalDispatcher dispatcher = dctx.getDispatcher();
		Delegator delegator = dispatcher.getDelegator();
		Locale locale = (Locale) context.get("locale");
		String userLoginId = (String) context.get("userLoginId");
		
		List<GenericValue> lableList = new ArrayList<GenericValue>();
		EntityCondition findConditions=null;
		findConditions=EntityCondition.makeCondition(UtilMisc.toMap("createdByUserLogin",userLoginId,"partyTypeId","PARTY_GROUP"));
		lableList=delegator.findList("PartyAndGroup",findConditions,UtilMisc.toSet("groupName"),null,null,false);
		List<String> lables = new ArrayList<String>();
		if(UtilValidate.isNotEmpty(lableList)){
			for(GenericValue lable:lableList){
				lables.add(lable.getString("groupName").toString());
			}
		}
		Map<String, Object> result = ServiceUtil.returnSuccess();
		result.put("lableNames", lables.toString());
		return result;
	}
	/**
	 * 查询标签内成员
	 * @param dctx
	 * @param context
	 * @return Map
	 * @throws GenericEntityException 
	 * @throws GenericServiceException 
	 */
	public static Map<String, Object> findLablePerson(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException{
		LocalDispatcher dispatcher = dctx.getDispatcher();
		Delegator delegator = dispatcher.getDelegator();
		Locale locale = (Locale) context.get("locale");
		String partyId=(String) context.get("partyId");
		List<GenericValue> lableList = new ArrayList<GenericValue>();
		EntityCondition findConditions=null;
		findConditions=EntityCondition.makeCondition(EntityCondition.makeCondition(UtilMisc.toMap("partyIdFrom",partyId)),EntityUtil.getFilterByDateExpr());
		lableList=delegator.findList("PartyRelationship",findConditions,UtilMisc.toSet("partyIdTo"),null,null,false);
		List<String> lables = new ArrayList<String>();
		if(UtilValidate.isNotEmpty(lableList)){
			for(GenericValue lable:lableList){
				Map<String, Object> inputFieldMap = UtilMisc.toMap(); 
				inputFieldMap.put("partyId", lable.get("partyIdTo"));
				Map<String, Object> findResult = null;
				findResult=dispatcher.runSync("findPersonInfo", inputFieldMap);
				lables.add(findResult.toString());
			}
		}
		Map<String, Object> result = ServiceUtil.returnSuccess();
		result.put("lablePersons", lables.toString());
		return result;
	}
}
