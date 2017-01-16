package com.banfftech.personerp;

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

public class PersonErpService {
	public static final String module = PersonErpQueryService.class.getName();

	/**
	 * 添加联系人
	 * 
	 * @param dctx
	 * @param context
	 * @return Map
	 * @throws GenericEntityException
	 * @throws GenericServiceException
	 */
	public static Map<String, Object> addContects(DispatchContext dctx, Map<String, Object> context)
			throws GenericEntityException, GenericServiceException {
		LocalDispatcher dispatcher = dctx.getDispatcher();
		Delegator delegator = dispatcher.getDelegator();
		Locale locale = (Locale) context.get("locale");
		String firstName = (String) context.get("firstName");
		String lastName = (String) context.get("lastName");
		String gender = (String) context.get("gender");
		String contactNumber = (String) context.get("contactNumber");
		String contactAddress1 = (String) context.get("contactAddress1");
		String contactCity = (String) context.get("contactCity");
		String contactPostalCode = (String) context.get("contactPostalCode");
		String contactGeoName = (String) context.get("contactGeoName");
		String contactAddress2 = (String) context.get("contactAddress2");
		String contactEmail = (String) context.get("contactEmail");
		String contactGroup = (String) context.get("contactGroup");
		String contactCompany = (String) context.get("contactCompany");
		String partyId = (String) context.get("partyId");
		// 模拟一个用户登录信息
		String userLoginId = "admin";
		GenericValue userLogin;
		userLogin = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", userLoginId), false);
		// 创建联系人partyId
		Map<String, Object> inputFieldMap = new HashMap<String, Object>();
		inputFieldMap.put("firstName", firstName);
		inputFieldMap.put("lastName", lastName);
		inputFieldMap.put("gender", gender);
		Map<String, Object> createPerson = null;
		createPerson = dispatcher.runSync("createUpdatePerson", inputFieldMap);
		// 添加联系人手机号码
		if (UtilValidate.isNotEmpty(createPerson) && contactNumber != "") {
			Map<String, Object> inputTelecom = new HashMap<String, Object>();
			inputTelecom.put("partyId", createPerson.get("partyId"));
			inputTelecom.put("contactNumber", contactNumber);
			inputTelecom.put("contactMechTypeId", "TELECOM_NUMBER");
			inputTelecom.put("contactMechPurposeTypeId", "PHONE_MOBILE");
			inputTelecom.put("userLogin", userLogin);
			Map<String, Object> createTelecom = null;
			createTelecom = dispatcher.runSync("createPartyTelecomNumber", inputTelecom);
		}
		// 添加联系人email
		if (UtilValidate.isNotEmpty(createPerson) && UtilValidate.isNotEmpty(contactEmail)) {
			Map<String, Object> inputEmail = new HashMap<String, Object>();
			inputEmail.put("partyId", createPerson.get("partyId"));
			inputEmail.put("emailAddress", contactEmail);
			inputEmail.put("contactMechTypeId", "EMAIL_ADDRESS");
			inputEmail.put("contactMechPurposeTypeId", "PRIMARY_EMAIL");
			inputEmail.put("userLogin", userLogin);
			Map<String, Object> createEmail = null;
			createEmail = dispatcher.runSync("createPartyEmailAddress", inputEmail);
		}
		// 添加联系人地址
		if (UtilValidate.isNotEmpty(createPerson)) {
			Map<String, Object> inputElcAddress = new HashMap<String, Object>();
			inputElcAddress.put("partyId", createPerson.get("partyId"));
			inputElcAddress.put("address1", contactAddress1);
			inputElcAddress.put("city", contactCity);
			inputElcAddress.put("postalCode", contactPostalCode);
			inputElcAddress.put("stateProvinceGeoId", contactGeoName);
			inputElcAddress.put("address2", contactAddress2);
			inputElcAddress.put("contactMechTypeId", "POSTAL_ADDRESS");
			inputElcAddress.put("contactMechPurposeTypeId", "PRIMARY_LOCATION");
			inputElcAddress.put("userLogin", userLogin);
			Map<String, Object> createElsAddress = null;
			createElsAddress = dispatcher.runSync("createPartyPostalAddress", inputElcAddress);
		}
		// 添加联系人标签(属于哪个party_Group)
		if (UtilValidate.isNotEmpty(createPerson) && UtilValidate.isNotEmpty(contactGroup)) {
			Map<String, Object> inputLable = new HashMap<String, Object>();
			inputLable.put("partyIdFrom", contactGroup);
			inputLable.put("partyIdTo", createPerson.get("partyId").toString());
			inputLable.put("partyRelationshipTypeId", "GROUP_ROLLUP");
			inputLable.put("userLogin", userLogin);
			Map<String, Object> createLable = null;
			createLable = dispatcher.runSync("createPartyRelationship", inputLable);
		}
		// 添加联系人所在公司(属性)
		if (UtilValidate.isNotEmpty(createPerson) && UtilValidate.isNotEmpty(contactCompany)) {
			Map<String, Object> inputCompany = new HashMap<String, Object>();
			inputCompany.put("attrName", "Company");
			inputCompany.put("attrValue", contactCompany);
			inputCompany.put("partyId", createPerson.get("partyId").toString());
			inputCompany.put("userLogin", userLogin);
			Map<String, Object> createCompany = null;
			createCompany = dispatcher.runSync("createPartyAttribute", inputCompany);
		}
		// 添加到当前用户联系人列表
		if (UtilValidate.isNotEmpty(createPerson)) {
			Map<String, Object> inputLable = new HashMap<String, Object>();
			inputLable.put("partyIdFrom", createPerson.get("partyId").toString());
			inputLable.put("partyIdTo", partyId);
			inputLable.put("userLogin", userLogin);
			// inputLable.put("roleTypeIdTo", "SUBSCRIBER");
			inputLable.put("partyRelationshipTypeId", "CONTACT_REL");
			Map<String, Object> createLable = null;
			createLable = dispatcher.runSync("createPartyRelationship", inputLable);
		}
		Map<String, Object> result = ServiceUtil.returnSuccess();
		Map<String, Object> inputMap = new HashMap<String, Object>();
		String personId = (String) createPerson.get("partyId");
		inputMap.put("partyId", personId);
		inputMap.put("resultMsg", UtilProperties.getMessage("PersonContactsUiLabels", "success", locale));
		result.put("resultMap", inputMap);
		return result;
	}

	/**
	 * 更新联系人信息
	 * 
	 * @param dctx
	 * @param context
	 * @return Map
	 * @throws GenericEntityException
	 * @throws GenericServiceException
	 */
	public static Map<String, Object> updateContects(DispatchContext dctx, Map<String, Object> context)
			throws GenericEntityException, GenericServiceException {
		LocalDispatcher dispatcher = dctx.getDispatcher();
		Delegator delegator = dispatcher.getDelegator();
		Locale locale = (Locale) context.get("locale");
		String firstName = (String) context.get("firstName");
		String lastName = (String) context.get("lastName");
		String gender = (String) context.get("gender");
		String contactNumber = (String) context.get("contactNumber");
		String contactAddress1 = (String) context.get("contactAddress1");
		String contactCity = (String) context.get("contactCity");
		String contactPostalCode = (String) context.get("contactPostalCode");
		String contactGeoName = (String) context.get("contactGeoName");
		String contactAddress2 = (String) context.get("contactAddress2");
		String contactEmail = (String) context.get("contactEmail");
		String contactGroup = (String) context.get("contactGroup");
		String contactCompany = (String) context.get("contactCompany");
		String partyId = (String) context.get("partyId");
		// 模拟一个用户登录信息
		String userLoginId = "admin";
		GenericValue userLogin;
		userLogin = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", userLoginId), false);
		// 更新人员基本信息
		Map<String, Object> inputFieldMap = new HashMap<String, Object>();
		inputFieldMap.put("partyId", partyId);
		inputFieldMap.put("gender", gender);
		inputFieldMap.put("firstName", firstName);
		inputFieldMap.put("lastName", lastName);
		inputFieldMap.put("userLogin", userLogin);
		Map<String, Object> updatePerson = null;
		updatePerson = dispatcher.runSync("updatePerson", inputFieldMap);
		// 更新手机号码
		List<GenericValue> contactMechIdList = null;
		EntityCondition condition = EntityCondition.makeCondition(
				EntityCondition
						.makeCondition(UtilMisc.toMap("partyId", partyId, "contactMechTypeId", "TELECOM_NUMBER")),
				EntityUtil.getFilterByDateExpr());
		contactMechIdList = delegator.findList("PartyAndContactMech", condition, null, null, null, false);
		for (GenericValue contactMechInfo : contactMechIdList) {
			// 如果手机号码不同
			if (contactMechInfo.get("tnContactNumber") != contactNumber) {
				Map<String, Object> inputTelecom = new HashMap<String, Object>();
				inputTelecom.put("partyId", partyId);
				inputTelecom.put("contactNumber", contactNumber);
				inputTelecom.put("contactMechId", contactMechInfo.get("contactMechId"));
				inputTelecom.put("contactMechTypeId", "TELECOM_NUMBER");
				inputTelecom.put("contactMechPurposeTypeId", "PHONE_MOBILE");
				inputTelecom.put("userLogin", userLogin);
				Map<String, Object> updateTelecom = null;
				updateTelecom = dispatcher.runSync("updatePartyTelecomNumber", inputTelecom);
			}
		}
		// 更新邮箱地址
		// 查询原邮箱地址
		List<GenericValue> contactEmailList = null;
		EntityCondition conditionEmail = EntityCondition.makeCondition(
				EntityCondition.makeCondition(UtilMisc.toMap("partyId", partyId, "contactMechTypeId", "EMAIL_ADDRESS")),
				EntityUtil.getFilterByDateExpr());
		contactEmailList = delegator.findList("PartyAndContactMech", conditionEmail, null, null, null, false);
		// 原始邮箱不为空
		if (UtilValidate.isNotEmpty(contactEmailList)) {
			for (GenericValue contactEmailInfo : contactEmailList) {
				// 如果原邮箱地址与新邮箱地址不同
				if (contactEmailInfo.get("infoString") != contactEmail) {
					// 新邮箱地址为空时，将原邮箱地址设为过期
					if (UtilValidate.isEmpty(contactEmail)) {
						Map<String, Object> inputDeleteEmial = new HashMap<String, Object>();
						inputDeleteEmial.put("partyId", partyId);
						inputDeleteEmial.put("userLogin", userLogin);
						inputDeleteEmial.put("contactMechId", contactEmailInfo.get("contactMechId"));
						Map<String, Object> deleteEmail = null;
						deleteEmail = dispatcher.runSync("deletePartyContactMech", inputDeleteEmial);
					} else {
						// 否则更新邮箱地址
						Map<String, Object> inputEmail = new HashMap<String, Object>();
						inputEmail.put("partyId", partyId);
						inputEmail.put("emailAddress", contactEmail);
						inputEmail.put("contactMechId", contactEmailInfo.get("contactMechId"));
						inputEmail.put("contactMechTypeId", "EMAIL_ADDRESS");
						inputEmail.put("contactMechPurposeTypeId", "PRIMARY_EMAIL");
						inputEmail.put("userLogin", userLogin);
						Map<String, Object> updateEmail = null;
						updateEmail = dispatcher.runSync("updatePartyEmailAddress", inputEmail);
					}
				}
			}
		} else {
			// 原始邮箱为空，且新邮箱不为空，新增邮箱地址
			if (UtilValidate.isNotEmpty(contactEmail)) {
				Map<String, Object> inputEmail = new HashMap<String, Object>();
				inputEmail.put("partyId", partyId);
				inputEmail.put("emailAddress", contactEmail);
				inputEmail.put("contactMechTypeId", "EMAIL_ADDRESS");
				inputEmail.put("contactMechPurposeTypeId", "PRIMARY_EMAIL");
				inputEmail.put("userLogin", userLogin);
				Map<String, Object> createEmail = null;
				createEmail = dispatcher.runSync("createPartyEmailAddress", inputEmail);
			}
		}
		// 更新地址
		List<GenericValue> contactElcList = null;
		EntityCondition contactElcCondition = EntityCondition.makeCondition(
				EntityCondition
						.makeCondition(UtilMisc.toMap("partyId", partyId, "contactMechTypeId", "POSTAL_ADDRESS")),
				EntityUtil.getFilterByDateExpr());
		contactElcList = delegator.findList("PartyAndContactMech", contactElcCondition, null, null, null, false);
		// 原地址不为空
		if (UtilValidate.isNotEmpty(contactElcList)) {
			for (GenericValue contactElcInfo : contactElcList) {
				if (contactElcInfo.get("paAddress1") != contactAddress1 || contactElcInfo.get("paCity") != contactCity
						|| contactElcInfo.get("paPostalCode") != contactPostalCode
						|| contactGeoName != contactElcInfo.get("paStateProvinceGeoId")
						|| contactAddress2 != contactElcInfo.get("paAddress2")) {
					if (UtilValidate.isEmpty(contactAddress1) && UtilValidate.isEmpty(contactGeoName)
							&& UtilValidate.isEmpty(contactPostalCode) && UtilValidate.isEmpty(contactPostalCode)) {
						Map<String, Object> inputDelete = new HashMap<String, Object>();
						inputDelete.put("partyId", partyId);
						inputDelete.put("userLogin", userLogin);
						inputDelete.put("contactMechId", contactElcInfo.get("contactMechId"));
						Map<String, Object> deleteAddress = null;
						deleteAddress = dispatcher.runSync("deletePartyContactMech", inputDelete);

					} else {
						Map<String, Object> inputElc = new HashMap<String, Object>();
						inputElc.put("partyId", partyId);
						inputElc.put("address1", contactAddress1);
						inputElc.put("city", contactCity);
						inputElc.put("postalCode", contactPostalCode);
						inputElc.put("stateProvinceGeoId", contactGeoName);
						inputElc.put("address2", contactAddress2);
						inputElc.put("contactMechId", contactElcInfo.get("contactMechId"));
						inputElc.put("contactMechTypeId", "POSTAL_ADDRESS");
						inputElc.put("contactMechPurposeTypeId", "PRIMARY_LOCATION");
						inputElc.put("userLogin", userLogin);
						Map<String, Object> updateAddress = null;
						updateAddress = dispatcher.runSync("updatePartyPostalAddress", inputElc);
					}
				}
			}
		}
		// 原地址为空，新地址不为空，新增地址信息。(设为必选所以先注释)
		/*
		 * else{
		 * if(UtilValidate.isNotEmpty(contactAddress1)||UtilValidate.isNotEmpty(
		 * contactCity)||UtilValidate.isNotEmpty(contactPostalCode)){
		 * Map<String,Object> inputElcAddress =FastMap.newInstance();
		 * inputElcAddress.put("partyId",partyId);
		 * inputElcAddress.put("address1",contactAddress1);
		 * inputElcAddress.put("city",contactCity);
		 * inputElcAddress.put("postalCode",contactPostalCode);
		 * inputElcAddress.put("contactMechTypeId", "POSTAL_ADDRESS");
		 * inputElcAddress.put("contactMechPurposeTypeId", "PRIMARY_LOCATION");
		 * inputElcAddress.put("userLogin",userLogin); Map<String, Object>
		 * createElsAddress = null; try {
		 * createElsAddress=dispatcher.runSync("createPartyPostalAddress",
		 * inputElcAddress); } catch (GenericServiceException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); return
		 * ServiceUtil.returnError(e.getMessage()); } } }
		 */
		// 更新公司属性
		List<GenericValue> contactCompanyList = null;
		EntityCondition contactCompanuyCondition = EntityCondition.makeCondition(
				EntityCondition.makeCondition(UtilMisc.toMap("partyId", partyId, "attrName", "Company")));
		contactCompanyList = delegator.findList("PartyAttribute", contactCompanuyCondition, null, null, null, false);
		if (UtilValidate.isNotEmpty(contactCompanyList)) {
			for (GenericValue contactCompanuyInfo : contactCompanyList) {
				if (contactCompanuyInfo.get("attrValue") != contactCompany
						|| UtilValidate.isEmpty(contactCompanuyInfo.get("attrValue"))) {
					if (UtilValidate.isEmpty(contactCompany)) {
						GenericValue deleteattr;
						deleteattr = delegator.findOne("PartyAttribute", false,
								UtilMisc.toMap("partyId", partyId, "attrName", "Company"));
						deleteattr.remove();

					} else {
						Map<String, Object> inputCompany = new HashMap<String, Object>();
						inputCompany.put("attrName", "Company");
						inputCompany.put("attrValue", contactCompany);
						inputCompany.put("partyId", partyId);
						inputCompany.put("userLogin", userLogin);
						Map<String, Object> createCompany = null;
						createCompany = dispatcher.runSync("updatePartyAttribute", inputCompany);
					}
				}
			}
		} else {
			if (UtilValidate.isNotEmpty(contactCompany)) {
				Map<String, Object> inputCompany = new HashMap<String, Object>();
				inputCompany.put("attrName", "Company");
				inputCompany.put("attrValue", contactCompany);
				inputCompany.put("partyId", partyId);
				inputCompany.put("userLogin", userLogin);
				Map<String, Object> createCompany = null;
				createCompany = dispatcher.runSync("createPartyAttribute", inputCompany);
			}
		}
		// 更新标签
		List<GenericValue> contactGroupList = null;
		EntityCondition contactGroupCondition = EntityCondition.makeCondition(
				EntityCondition.makeCondition(UtilMisc.toMap("partyIdTo", partyId,"partyRelationshipTypeId","GROUP_ROLLUP")), EntityUtil.getFilterByDateExpr());
		contactGroupList = delegator.findList("PartyRelationship", contactGroupCondition, null, null, null, false);
		// 原标签不为空
		if (UtilValidate.isNotEmpty(contactGroupList) && UtilValidate.isNotEmpty(contactGroup)) {
			for (GenericValue contactGroupInfo : contactGroupList) {
				// 新标签为空则原标签变为过期
				if (UtilValidate.isEmpty(contactGroup)) {
					Map<String, Object> inputGroup = new HashMap<String, Object>();
					inputGroup.put("partyId", partyId);
					inputGroup.put("partyIdTo", partyId);
					inputGroup.put("partyRelationshipTypeId", "GROUP_ROLLUP");
					inputGroup.put("fromDate", contactGroupInfo.get("fromDate"));
					inputGroup.put("partyIdFrom", contactGroupInfo.get("partyIdFrom"));
					inputGroup.put("userLogin", userLogin);
					Map<String, Object> deleteGroup = null;
					deleteGroup = dispatcher.runSync("deletePartyRelationship", inputGroup);
				} else {
					Map<String, Object> inputLable = new HashMap<String, Object>();
					inputLable.put("partyIdFrom", contactGroup);
					inputLable.put("partyIdTo", partyId);
					inputLable.put("userLogin", userLogin);
					Map<String, Object> createLable = null;
					createLable = dispatcher.runSync("createPartyRelationship", inputLable);
				}
			}
		}
		// 原标签为空，新标签不为空，创建一个新的标签
		else {
			if (UtilValidate.isNotEmpty(contactGroup)) {
				Map<String, Object> inputLable = new HashMap<String, Object>();
				inputLable.put("partyIdFrom", contactGroup);
				inputLable.put("partyIdTo", partyId);
				inputLable.put("partyRelationshipTypeId", "GROUP_ROLLUP");
				inputLable.put("userLogin", userLogin);
				Map<String, Object> createLable = null;
				createLable = dispatcher.runSync("createPartyRelationship", inputLable);
			}
		}
		Map<String, Object> result = ServiceUtil.returnSuccess();
		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("resultMsg", UtilProperties.getMessage("PersonContactsUiLabels", "success", locale));
		result.put("resultMap", inputMap);
		return result;
	}

	/**
	 * 删除标签
	 * 
	 * @param dctx
	 * @param context
	 * @return Map
	 * @throws GenericEntityException
	 * @throws GenericServiceException
	 */
	public static Map<String, Object> deleteLable(DispatchContext dctx, Map<String, Object> context)
			throws GenericEntityException, GenericServiceException {
		LocalDispatcher dispatcher = dctx.getDispatcher();
		Delegator delegator = dispatcher.getDelegator();
		Locale locale = (Locale) context.get("locale");
		String partyId = (String) context.get("partyId");
		Map<String, Object> resultMap = ServiceUtil.returnSuccess();
		Map<String, Object> inputMap = new HashMap<String, Object>();
		// 模拟一个用户登录信息
		String userLoginId = "admin";
		GenericValue userLogin;
		userLogin = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", userLoginId), false);
		// 同步updatePartyGroup服务，使statusId更新为禁用
		Map<String, Object> input = new HashMap<String, Object>();
		input.put("partyId", partyId);
		input.put("statusId", "PARTY_DISABLED");
		input.put("userLogin", userLogin);
		Map<String, Object> deleteLable = null;
		deleteLable = dispatcher.runSync("updatePartyGroup", input);
		inputMap.put("resultMsg", UtilProperties.getMessage("PersonContactsUiLabels", "success", locale));
		resultMap.put("resultMap", inputMap);
		return resultMap;
	}

	/**
	 * 新建标签
	 * 
	 * @param dctx
	 * @param context
	 * @return Map
	 * @throws GenericEntityException
	 * @throws GenericServiceException
	 */
	public static Map<String, Object> createLable(DispatchContext dctx, Map<String, Object> context)
			throws GenericEntityException, GenericServiceException {
		LocalDispatcher dispatcher = dctx.getDispatcher();
		Delegator delegator = dispatcher.getDelegator();
		Locale locale = (Locale) context.get("locale");
		String lableName = (String) context.get("lableName");
		Map<String, Object> resultMap = ServiceUtil.returnSuccess();
		Map<String, Object> inputMap = new HashMap<String, Object>();
		// 模拟一个用户登录信息
		String userLoginId = "admin";
		GenericValue userLogin;
		userLogin = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", userLoginId), false);
		// 同步createPartyGroup服务
		Map<String, Object> input = new HashMap<String, Object>();
		input.put("groupName", lableName);
		input.put("userLogin", userLogin);
		Map<String, Object> createLable = null;
		createLable = dispatcher.runSync("createPartyGroup", input);
		inputMap.put("resultMsg", UtilProperties.getMessage("PersonContactsUiLabels", "success", locale));
		resultMap.put("resultMap", inputMap);
		return resultMap;
	}
	/**
	 * 添加标签内成员
	 * 
	 * @param dctx
	 * @param context
	 * @return Map
	 * @throws GenericEntityException
	 * @throws GenericServiceException
	 */
	public static Map<String, Object> addLablePerson(DispatchContext dctx, Map<String, Object> context)
			throws GenericEntityException, GenericServiceException {
		LocalDispatcher dispatcher = dctx.getDispatcher();
		Delegator delegator = dispatcher.getDelegator();
		Locale locale = (Locale) context.get("locale");
		String partyIdFrom = (String) context.get("partyIdFrom");
		String partyIdTo = (String) context.get("partyIdTo");
		Map<String, Object> resultMap = ServiceUtil.returnSuccess();
		Map<String, Object> inputMap = new HashMap<String, Object>();
		// 模拟一个用户登录信息
		String userLoginId = "admin";
		GenericValue userLogin;
		userLogin = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", userLoginId), false);
		// 同步createPartyRelationship服务
		Map<String, Object> input = new HashMap<String, Object>();
		input.put("partyIdFrom", partyIdFrom);
		input.put("partyIdTo", partyIdTo);
		input.put("partyRelationshipTypeId", "GROUP_ROLLUP");
		input.put("userLogin", userLogin);
		Map<String, Object> addLablePerson = null;
		addLablePerson = dispatcher.runSync("createPartyRelationship", input);
		inputMap.put("resultMsg", UtilProperties.getMessage("PersonContactsUiLabels", "success", locale));
		resultMap.put("resultMap", inputMap);
		return resultMap;
	}
}
