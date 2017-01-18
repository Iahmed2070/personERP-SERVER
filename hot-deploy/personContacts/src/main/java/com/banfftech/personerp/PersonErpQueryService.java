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
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

public class PersonErpQueryService {
	public static final String module = PersonErpQueryService.class.getName();

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
					"http://114.215.200.46:3400/personContacts/control/stream?contentId=" + contentId);
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
		}
		inputMap.put("resultMsg", UtilProperties.getMessage("PersonContactsUiLabels", "success", locale));
		inputMap.put("partyId", partyId);
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
					"http://114.215.200.46:3400/personContacts/control/stream?contentId=" + contentId);
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
	 * @author zhangwenwen
	 * @param dctx
	 * @param context
	 * @return
	 * @throws GenericEntityException
	 * @throws GenericServiceException
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
	 * @author zhangwenwen
	 * @param dispatcher
	 * @param delegator
	 * @param context
	 * @param userLogin
	 * @param contactMechPurposeTypeId
	 * @throws GenericServiceException
	 * @throws GenericEntityException
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
	 * @author zhangwenwen
	 * @param dispatcher
	 * @param delegator
	 * @param context
	 * @throws GenericEntityException
	 * @throws GenericServiceException
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
	public static Map<String, Object> findContectsInfo(DispatchContext dctx, Map<String, Object> context)
			throws GenericEntityException, GenericServiceException {
		LocalDispatcher dispatcher = dctx.getDispatcher();
		Delegator delegator = dispatcher.getDelegator();
		Locale locale = (Locale) context.get("locale");
		String partyId = (String) context.get("partyId");
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
}
