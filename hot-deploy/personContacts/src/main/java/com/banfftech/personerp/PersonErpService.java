package com.banfftech.personerp;

import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;


import main.java.com.banfftech.personerp.submail.config.AppConfig;
import main.java.com.banfftech.personerp.submail.lib.MESSAGEXsend;
import main.java.com.banfftech.personerp.submail.utils.ConfigLoader;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.condition.EntityConditionList;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.entity.util.EntityUtilProperties;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ofbiz.entity.condition.EntityOperator;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import org.apache.ofbiz.entity.model.ModelEntity;
import java.io.IOException;
import main.java.com.banfftech.personerp.util.EncrypDES;
import javax.servlet.ServletException;

/**
 * 聚合服务草稿类,暂未分离不同业务
 */
public class PersonErpService {
    public static final String module = com.banfftech.personerp.PersonErpQueryService.class.getName();
    private static String url = null;
    private static String appkey = null;
    private static String secret = null;
    private static String smsFreeSignName = null;
    private static String smsTemplateCode = null;

    public static void getSmsProperty(Delegator delegator){
        url = EntityUtilProperties.getPropertyValue("pe","sms.url",delegator);
        appkey = EntityUtilProperties.getPropertyValue("pe","sms.appkey",delegator);
        secret = EntityUtilProperties.getPropertyValue("pe","sms.secret",delegator);
        smsFreeSignName = EntityUtilProperties.getPropertyValue("pe","sms.smsFreeSignName",delegator);
        smsTemplateCode = EntityUtilProperties.getPropertyValue("pe","sms.smsTemplateCode",delegator);
    }


    public static String processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, GenericServiceException, GenericEntityException {
//		request.setContentType("text/html;charset=gb2312");
//		request.setCharacterEncoding("GBK");
//		response.setCharacterEncoding("GBK");
        String path = request.getSession().getServletContext().getRealPath("/") + "upload\\";
        String encodedFileName = UUID.randomUUID().toString();

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        //模拟登陆
        GenericValue userLogin = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", "admin"), false);
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

        org.apache.ofbiz.base.util.HttpRequestFileUpload uploadObject = new org.apache.ofbiz.base.util.HttpRequestFileUpload();

//		uploadObject.setOverrideFilename(encodedFileName);
        uploadObject.setSavePath(path);
        uploadObject.doUpload(request);


        String fileName = uploadObject.getFilename();

        String fileName5 = new String(fileName.getBytes(), "UTF-8");

        //String result = getEncoding(fileName);
        //创建人
        String partyId = new String(uploadObject.getFieldValue("partyId").getBytes("ISO-8859-1"), "UTF-8");
        //活动名称
        String workEffortName = new String(uploadObject.getFieldValue("workEffortName").getBytes(), "UTF-8");
        //简介
        String description = new String(uploadObject.getFieldValue("description").getBytes("ISO-8859-1"), "UTF-8");
        //地址
        String convertedLeads = new String(uploadObject.getFieldValue("convertedLeads").getBytes("ISO-8859-1"), "UTF-8");
        //活动时间
        String actualStartDateStr = new String(uploadObject.getFieldValue("actualStartDate").getBytes("ISO-8859-1"), "UTF-8");

        Timestamp actualStartDate = null;
        if (actualStartDateStr != null) {
            actualStartDate = Timestamp.valueOf(actualStartDateStr);
        }


//		//读取请求Body
//		byte[] body = readBody(request);
//		//取得所有Body内容的字符串表示
//		String textBody = new String(body, "ISO-8859-1");
//		//取得上传的文件名称
//		String fileName = getFileName(textBody);
//		//String result = getEncoding(fileName);
//		fileName = new String(fileName.getBytes("ISO-8859-1"),"UTF-8");
//		//取得文件开始与结束位置
//		Position p = getFilePosition(request, textBody);
//		//输出至文件
//		writeTo(fileName, body, p,path);

        //1.CREATE DATA RESOURCE
        Map<String, Object> createDataResourceMap = UtilMisc.toMap("userLogin", userLogin,
                "dataResourceTypeId", "LOCAL_FILE",
                "dataCategoryId", "PERSONAL",
                "dataResourceName", fileName
                , "mimeTypeId", "image/jpeg",
                "isPublic", "Y",
                "dataTemplateTypeId", "NONE",
                "statusId", "CTNT_PUBLISHED",
                "objectInfo", path
        );
        Map<String, Object> serviceResultByDataResource = dispatcher.runSync("createDataResource", createDataResourceMap);
        String dataResourceId = (String) serviceResultByDataResource.get("dataResourceId");
        //2.CREATE CONTENT
        Map<String, Object> createContentMap = UtilMisc.toMap("userLogin", userLogin, "contentTypeId", "ACTIVITY_BACKGROUND", "mimeTypeId", "image/jpeg", "dataResourceId", dataResourceId);
        Map<String, Object> serviceResultByCreateContentMap = dispatcher.runSync("createContent", createContentMap);
        String contentId = (String) serviceResultByCreateContentMap.get("contentId");
        //IF HAS PICTURE WALL
        //TODO FOREACH CREATE
        //3.CREATE WORK_EFFORT 目前状态计划中 目前类型事件
        Map<String, Object> createWorkEffortMap = UtilMisc.toMap("userLogin", userLogin, "currentStatusId", "CAL_IN_PLANNING", "workEffortName", workEffortName, "workEffortTypeId", "workEffortTypeId", "description", description, "actualStartDate", actualStartDate);
        Map<String, Object> serviceResultByCreateWorkEffortMap = dispatcher.runSync("createWorkEffort", createWorkEffortMap);
        //NEW WORKEFFORT_ID
        String workEffortId = (String) serviceResultByCreateWorkEffortMap.get("workEffortId");
        //4.assignPartyToWorkEffort
        Map<String, Object> createAdminAssignPartyMap = UtilMisc.toMap("userLogin", userLogin, "partyId", partyId, "roleTypeId", "ACTIVITY_ADMIN", "statusId", "PRTYASGN_ASSIGNED", "workEffortId", workEffortId);
        Map<String, Object> createMemberAssignPartyMap = UtilMisc.toMap("userLogin", userLogin, "partyId", partyId, "roleTypeId", "ACTIVITY_ADMIN", "statusId", "PRTYASGN_ASSIGNED", "workEffortId", workEffortId);
        dispatcher.runSync("assignPartyToWorkEffort", createAdminAssignPartyMap);
        dispatcher.runSync("assignPartyToWorkEffort", createMemberAssignPartyMap);

        //ASSOC WORKEFFORT & CONTENT
        Map<String, Object> createAssocWFContMap = UtilMisc.toMap("userLogin", userLogin, "contentId", contentId, "workEffortContentTypeId", "PROPOSAL_MEDIA", "workEffortId", workEffortId);
        dispatcher.runSync("createWorkEffortContent", createAssocWFContMap);
        return "success";
    }


    public static String getEncoding(String str) {
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {      //判断是不是GB2312
                String s = encode;
                return s;      //是的话，返回“GB2312“，以下代码同理
            }
        } catch (Exception exception) {
        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {      //判断是不是ISO-8859-1
                String s1 = encode;
                return s1;
            }
        } catch (Exception exception1) {
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {   //判断是不是UTF-8
                String s2 = encode;
                return s2;
            }
        } catch (Exception exception2) {
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {      //判断是不是GBK
                String s3 = encode;
                return s3;
            }
        } catch (Exception exception3) {
        }
        return "";        //如果都不是，说明输入的内容不属于常见的编码格式。
    }


    //构造类
    static class Position {

        int begin;
        int end;

        public Position(int begin, int end) {
            this.begin = begin;
            this.end = end;
        }
    }

    private static byte[] readBody(HttpServletRequest request) throws IOException {
        //获取请求文本字节长度
        int formDataLength = request.getContentLength();
        //取得ServletInputStream输入流对象
        DataInputStream dataStream = new DataInputStream(request.getInputStream());
        byte body[] = new byte[formDataLength];
        int totalBytes = 0;
        while (totalBytes < formDataLength) {
            int bytes = dataStream.read(body, totalBytes, formDataLength);
            totalBytes += bytes;
        }
        return body;
    }

    private static Position getFilePosition(HttpServletRequest request, String textBody) throws IOException {
        //取得文件区段边界信息
        String contentType = request.getContentType();
        String boundaryText = contentType.substring(contentType.lastIndexOf("=") + 1, contentType.length());
        //取得实际上传文件的气势与结束位置
        int pos = textBody.indexOf("filename=\"");
        pos = textBody.indexOf("\n", pos) + 1;
        pos = textBody.indexOf("\n", pos) + 1;
        pos = textBody.indexOf("\n", pos) + 1;
        int boundaryLoc = textBody.indexOf(boundaryText, pos) - 4;
        int begin = ((textBody.substring(0, pos)).getBytes("ISO-8859-1")).length;
        int end = ((textBody.substring(0, boundaryLoc)).getBytes("ISO-8859-1")).length;

        return new Position(begin, end);
    }

    private static String getParameter(String requestBody, String parameterName) {
        String fileName = "";
        if (requestBody.indexOf(parameterName + "\"") > 0) {
            fileName = requestBody.substring(requestBody.indexOf(parameterName + "\"") + 10);
        }

        fileName = fileName.substring(0, fileName.indexOf("\n"));
        fileName = fileName.substring(fileName.indexOf("\n") + 1, fileName.indexOf("\""));

        return fileName;
    }

    private static String getFileName(String requestBody) {
        String fileName = requestBody.substring(requestBody.indexOf("filename=\"") + 10);
        fileName = fileName.substring(0, fileName.indexOf("\n"));
        fileName = fileName.substring(fileName.indexOf("\n") + 1, fileName.indexOf("\""));

        return fileName;
    }

    private static void writeTo(String fileName, byte[] body, Position p, String path) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(path + fileName);
        fileOutputStream.write(body, p.begin, (p.end - p.begin));
        fileOutputStream.flush();
        fileOutputStream.close();
    }


    /**
     * 用户注册
     * @param dctx
     * @param context
     * @return
     */
    public static Map<String, Object> userAppRegister(DispatchContext dctx, Map<String, Object> context) throws GenericServiceException,GenericEntityException{
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");

        //TODO 1. CHECK CAPTCHA
        String teleNumber = (String) context.get("teleNumber");//手机号 也就是userLoginId
        String captcha    = (String) context.get("captcha");//验证码
        String nickname   = (String) context.get("nickname");//昵称
        GenericValue userLogin = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", "admin"), false);

        //        //查找用户验证码是否存在
        EntityConditionList<EntityCondition> captchaConditions = EntityCondition
                .makeCondition(EntityCondition.makeCondition("teleNumber", EntityOperator.EQUALS, teleNumber),EntityUtil.getFilterByDateExpr(),EntityCondition.makeCondition("isValid", EntityOperator.EQUALS, "N"),EntityCondition.makeCondition("smsType", EntityOperator.EQUALS, "REGISTER"));
        List<GenericValue> smsList = new ArrayList<GenericValue>();
        try {
            smsList = delegator.findList("SmsValidateCode", captchaConditions, null,
                    UtilMisc.toList("-" + ModelEntity.CREATE_STAMP_FIELD), null, false);
        } catch (GenericEntityException e) {
            org.apache.ofbiz.base.util.Debug.logError(e.getMessage(), module);
            //org.apache.ofbiz.base.util.UtilProperties.getMessage("PeInternalServiceError", "success", locale)
            return ServiceUtil.returnError(org.apache.ofbiz.base.util.UtilProperties.getMessage("PersonContactsUiLabels","PeInternalServiceError", locale));
        }

        if(UtilValidate.isEmpty(smsList)){
            return ServiceUtil.returnError(org.apache.ofbiz.base.util.UtilProperties.getMessage("PersonContactsUiLabels","PeCaptchaNotExistError", locale));
        }else{
            GenericValue sms = smsList.get(0);

            if(sms.get("captcha").equals(captcha)){
                //创建Party Person
                Map<String, Object> createPartyInMap =
                        UtilMisc.toMap("userLogin", userLogin,
                                "nickname",nickname,
                                "firstName","设置",
                                "lastName","未");
//                inputFieldMap.put("gender", gender);
                Map<String, Object> createPerson = null;
                createPerson = dispatcher.runSync("createUpdatePerson", createPartyInMap);
                String partyId = (String) createPerson.get("partyId");
                //创建UserLogin
                Map<String, Object> createUserLoginInMap =
                        UtilMisc.toMap("userLogin", userLogin,
                                "userLoginId",teleNumber,
                                "partyId",partyId,
                                "currentPassword","ofbiz","currentPasswordVerify","ofbiz"
                                );

                Map<String, Object> createUserLogin = null;
                createUserLogin = dispatcher.runSync("createUserLogin", createUserLoginInMap);


                //TODO 5. GRANT ABOUT PE PERMISSION 授予权限
                Map<String, Object> createPartyRoleMemberMap = UtilMisc.toMap("userLogin", userLogin, "partyId", partyId, "roleTypeId", "ACTIVITY_MEMBER");
                dispatcher.runSync("createPartyRole", createPartyRoleMemberMap);
                createPartyRoleMemberMap =  UtilMisc.toMap("userLogin", userLogin, "partyId", partyId, "roleTypeId", "ACTIVITY_ADMIN");
                dispatcher.runSync("createPartyRole", createPartyRoleMemberMap);
                createPartyRoleMemberMap =  UtilMisc.toMap("userLogin", userLogin, "partyId", partyId, "roleTypeId", "ACTIVITY_INVITATION");
                dispatcher.runSync("createPartyRole", createPartyRoleMemberMap);

                Map<String, Object> createPartyRoleSecurityGroupMap = UtilMisc.toMap("userLogin", userLogin, "userLoginId",teleNumber, "groupId", "FULLADMIN");
                dispatcher.runSync("addUserLoginToSecurityGroup", createPartyRoleSecurityGroupMap);

            }
        }



        Map<String,Object> result = ServiceUtil.returnSuccess();
        Map<String, Object> inputMap = new HashMap<String, Object>();    inputMap.put("resultMsg", UtilProperties.getMessage("PersonContactsUiLabels", "success", locale));
        result.put("resultMap", inputMap);
        return result;
    }




    /**
     * GetCaptcha
     * @author S.Y.L
     * @param dctx
     * @param context
     * @return
     */
    public static Map<String, Object> getLoginCaptcha(DispatchContext dctx, Map<String, Object> context) {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        String teleNumber = (String) context.get("teleNumber");
        String smsType = (String) context.get("smsType");//LOGIN 或 REGISTER

        java.sql.Timestamp nowTimestamp  = org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp();



        EntityCondition captchaCondition = EntityCondition.makeCondition(
                EntityCondition.makeCondition("teleNumber", EntityOperator.EQUALS, teleNumber),
                EntityUtil.getFilterByDateExpr(),
                EntityCondition.makeCondition("isValid", EntityOperator.EQUALS,"N"));

        GenericValue sms = null;
        try {
            sms = EntityUtil.getFirst(
                    delegator.findList("SmsValidateCode", captchaCondition, null,UtilMisc.toList("-" + ModelEntity.CREATE_STAMP_FIELD), null, false)
            );
        } catch (GenericEntityException e) {
            //TODO ADD EXCEPTION
        }


        int validTime = Integer.valueOf(EntityUtilProperties.getPropertyValue("pe","sms.validTime","900",delegator));
        int intervalTime = Integer.valueOf(EntityUtilProperties.getPropertyValue("pe","sms.intervalTime","60",delegator));


        boolean sendSMS = false;
        if(UtilValidate.isEmpty(sms)){
            sendSMS = true;
        }else{
            org.apache.ofbiz.base.util.Debug.logInfo("The user tel:[" + teleNumber + "]  verfiy code[" + sms.getString("captcha") + "], check the interval time , if we'll send new code", module);
            // 如果已有未验证的记录存在，则检查是否过了再次重发的时间间隔，没过就忽略本次请求
            if(org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp().after(org.apache.ofbiz.base.util.UtilDateTime.adjustTimestamp((java.sql.Timestamp) sms.get("fromDate"), Calendar.SECOND, intervalTime))){
                sms.set("thruDate", nowTimestamp);
                try {
                    sms.store();
                } catch (GenericEntityException e) {

//                    return ServiceUtil.returnError("CloudCardInternalServiceError"));
                }
                org.apache.ofbiz.base.util.Debug.logInfo("The user tel:[" + teleNumber + "]  will get new verfiy code!", module);
                sendSMS = true;
            }
        }

        if(sendSMS){
            //生成验证码
            String captcha = org.apache.ofbiz.base.util.UtilFormatOut.padString(String.valueOf(Math.round((Math.random() * 10e6))), 2, false, '0');
             Map<String,Object> smsValidateCodeMap = UtilMisc.toMap();
            smsValidateCodeMap.put("teleNumber", teleNumber);
            smsValidateCodeMap.put("captcha", captcha);
            smsValidateCodeMap.put("smsType", smsType);
            smsValidateCodeMap.put("isValid", "N");
            smsValidateCodeMap.put("fromDate", nowTimestamp);
            smsValidateCodeMap.put("thruDate", org.apache.ofbiz.base.util.UtilDateTime.adjustTimestamp(nowTimestamp, Calendar.SECOND, validTime));
            try {
                GenericValue smstGV = delegator.makeValue("SmsValidateCode", smsValidateCodeMap);
                smstGV.create();
            } catch (GenericEntityException e) {

//                return ServiceUtil.returnError("CloudCardSendFailedError"));
            }

            //发送短信
            context.put("phone", teleNumber);
            context.put("code", captcha);
            context.put("product", "丕屹");
            PersonErpService.sendMessage(dctx, context);
        }
        return ServiceUtil.returnSuccess();
    }





    public static Map<String, Object> sendFuckMss(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        String messageInfo = "http://114.215.200.46:3400/pewebview/control/showActivityDetail?p_ctx="+"23123213";
        AppConfig config =  new AppConfig();
        config.setAppId("13407");
        config.setAppKey("d0f68f840616a7cd8586ce63d6c77c03");
        config.setSignType("normal");
        MESSAGEXsend submail = new MESSAGEXsend(config);
        submail.addTo("15800803851");
        submail.setProject("ps6Xa4");
        submail.addVar("fromparty", "王亮");
        submail.addVar("activityname", "登山");
        submail.addVar("activityurl", messageInfo);
        submail.xsend();
        Map<String, Object> result = ServiceUtil.returnSuccess();
        return result;
    }
    /**
     * SendMessageInvitation
     * @Author S.Y.L
     * @param dctx
     * @param context
     * @return
     */
    public static Map<String, Object> sendInvitation(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        //ActivityId
        String workEffortId = (String) context.get("workEffortId");
        //ContactInfoMation
        String contact = (String) context.get("contact");
        //分割
        String [] contactArrays = contact.split(",");

        //SendFrom
        String partyId = (String) context.get("partyId");

        //TODO 获得邀请人的名称
        GenericValue person = delegator.findOne("Person", false, UtilMisc.toMap("partyId", partyId));
        //TODO 获得活动名称
        GenericValue event = delegator.findOne("WorkEffort", false, UtilMisc.toMap("workEffortId", workEffortId));
        //Get Activity Admin NikeName
        String nikeName =  person.get("nickname")==null?person.get("lastName")+""+person.get("firstName"):(String) person.get("nickname");
        String workEffortName = (String)event.get("workEffortName");

        //TODO 发送邀请短信
        forSendInvitation(contactArrays,nikeName,delegator,dispatcher,workEffortId,workEffortName);



        Map<String, Object> result = ServiceUtil.returnSuccess();
        Map<String, Object> inputMap = new HashMap<String, Object>();    inputMap.put("resultMsg", UtilProperties.getMessage("PersonContactsUiLabels", "success", locale));
        result.put("resultMap", inputMap);
        return result;
    }

    /**
     * 发送邀请函
     * @author S.Y.L
     * @param contactArrays
     * @param nikeName
     * @param delegator
     * @param dispatcher
     * @param workEffortId
     */
    private static void forSendInvitation(String[] contactArrays, String nikeName, Delegator delegator, LocalDispatcher dispatcher, String workEffortId,String workEffortName) throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException {
        //Str Add Str
        StringBuffer stringBuffer = new StringBuffer();
        //TODO DES 加密请求参数
        EncrypDES de1 = new EncrypDES();

        for(int i =0 ; i < contactArrays.length;i++){
            String tel = contactArrays[i].substring(contactArrays[i].indexOf("/")+1);
            String name = contactArrays[i].substring(0,contactArrays[i].indexOf("/"));
            stringBuffer.append("fromName:"+nikeName+",");
            stringBuffer.append("workEffortId:"+workEffortId+",");
            stringBuffer.append("tel:"+ tel+",");
            stringBuffer.append("name:"+ name);
            String encontent = stringBuffer.toString();
            //发送给邀请人
            String messageInfo = "http://www.vivafoo.com:3400/pewebview/control/showActivityDetail?p_ctx="+encontent;
            //String messageInfo = "http://t.im/nosplitpear?p_ctx="+encontent;
            AppConfig config =  new AppConfig();
            config.setAppId("13407");
            config.setAppKey("d0f68f840616a7cd8586ce63d6c77c03");
            config.setSignType("normal");
            MESSAGEXsend submail = new MESSAGEXsend(config);
            if(tel.indexOf("-")>0){
                tel = tel.replaceAll("-","");
            }
            submail.addTo(tel);
            submail.setProject("ps6Xa4");
            submail.addVar("fromparty", nikeName);
            submail.addVar("activityname", workEffortName);
            submail.addVar("activityurl", messageInfo);
            submail.xsend();


        }
    }

    /**
     * 发送
     * @param dctx
     * @param context
     * @return
     */
    public static Map<String, Object> sendMessage(DispatchContext dctx, Map<String, Object> context) {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        String phone = (String) context.get("phone");
        String code = (String) context.get("code");
        String product = (String) context.get("product");
        //初始化短信发送配置文件
        getSmsProperty(delegator);
        //发送短信
        //暂时先写死、此处应当放入配置文件
        TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "23654770", "9c58a5fa366e2aabd8a62363c4c228c6");
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setExtend("");
        req.setSmsType("normal");
        req.setSmsFreeSignName(smsFreeSignName);
        String json="{\"number\":\""+code+"\"}";
        req.setSmsParamString(json);
        req.setRecNum(phone);
        req.setSmsTemplateCode(smsTemplateCode);
        AlibabaAliqinFcSmsNumSendResponse rsp = null;
        try {
            rsp = client.execute(req);
        } catch (ApiException e) {

        }
        if(rsp!=null && !rsp.isSuccess()){
            org.apache.ofbiz.base.util.Debug.logWarning("something wrong when send the short message, response body:" + rsp.getBody(), module);
        }
        Map<String, Object> result = ServiceUtil.returnSuccess();
        Map<String, Object> inputMap = new HashMap<String, Object>();    inputMap.put("resultMsg", UtilProperties.getMessage("PersonContactsUiLabels", "success", locale));
        result.put("resultMap", inputMap);
        return result;
    }

    /**
     *创建支付临时数据
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws InterruptedException
     */
    public static Map<String, Object> createPartyPaymentInfo(DispatchContext dctx, Map<String, ? extends Object> context)
            throws  GenericEntityException, GenericServiceException, InterruptedException {

        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        Locale locale = (Locale) context.get("locale");
        // 登陆
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

        GenericValue newPay = delegator.makeValue("PartyPaymentInfo");
        newPay.set("tmpId", delegator.getNextSeqId("PartyPaymentInfo"));
        newPay.set("partyIdTo", partyIdTo);
        newPay.set("partyIdFrom", partyIdFrom);
        newPay.set("invoiceApplied", new BigDecimal(invoiceApplied) );
        newPay.set("workEffortId", workEffortId);
        newPay.set("amountApplied", new BigDecimal(amountApplied));
        newPay.set("payDate",payDate);

        newPay.create();

        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        Map<String, Object> inputMap = new HashMap<String, Object>();

        inputMap.put("resultMsg", UtilProperties.getMessage("PersonContactsUiLabels", "success", locale));

        resultMap.put("resultMap",inputMap);
        return resultMap;
    }


    /**
     * 用户确认投票
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws InterruptedException
     */
    public static Map<String, Object> doPollQuestion(DispatchContext dctx, Map<String, ? extends Object> context)
            throws  GenericEntityException, GenericServiceException, InterruptedException {

        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        // 登陆
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        // 报名人id
        String partyId = (String) userLogin.get("partyId");
        //投票项目Id
        String surveyQuestionId = (String) context.get("surveyQuestionId");
        //投票Id
        String surveyId = (String) context.get("surveyId");

        Locale locale = (Locale) context.get("locale");

        //创建Response Awser
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        Map<String, Object> inputMap = new HashMap<String, Object>();
        Map<String,Object> answersMap = new HashMap<String, Object>();
        answersMap.put("answers","{"+surveyQuestionId+"=Y}");
        Map<String, Object> createSurveyInMap =
                UtilMisc.toMap("userLogin", userLogin,
                       "answers",answersMap,
                        "partyId",partyId,
                        "surveyId",surveyId);
        Map<String, Object> createSurveyReturnMap = dispatcher.runSync("createSurveyResponse", createSurveyInMap);
        String surveyResponseId = (String) createSurveyReturnMap.get("surveyResponseId");
        String surveyMultiRespColId = (String) createSurveyReturnMap.get("surveyMultiRespColId");


        EntityCondition findConditionsToVoteList = null;
        findConditionsToVoteList = EntityCondition
                .makeCondition(UtilMisc.toMap("surveyResponseId", surveyResponseId,
                        "surveyQuestionId", surveyQuestionId, "surveyMultiRespColId", "_NA_"));
        //创建surveyResponseAnswer
        GenericValue surveyResponseAnswer =  EntityUtil.getFirst(delegator.findList("SurveyResponseAnswer",findConditionsToVoteList , null, null, null, false));
//        surveyResponseAnswer.set("surveyMultiRespColId", "_NA_");
        surveyResponseAnswer.set("surveyQuestionId", surveyQuestionId);
        surveyResponseAnswer.set("surveyResponseId", surveyResponseId);
        surveyResponseAnswer.set("booleanResponse","Y");
        surveyResponseAnswer.store();


        inputMap.put("resultMsg", UtilProperties.getMessage("PersonContactsUiLabels", "success", locale));
        resultMap.put("resultMap", inputMap);
        return resultMap;
    }


    /**
     * 创建投票标题和投票项
     * @param dctx
     * @param context
     * @return
     * @throws IOException
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws InterruptedException
     */
    public static Map<String, Object> createSurveyAndQuestions(DispatchContext dctx, Map<String, ? extends Object> context)
            throws  GenericEntityException, GenericServiceException, InterruptedException {

        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        // 登陆
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        // 报名人id
        String partyId = (String) userLogin.get("partyId");
        Locale locale = (Locale) context.get("locale");
        //活动的id
        String workEffortId = (String) context.get("workEffortId");
        //投票标题
        String surveyName = (String) context.get("surveyName");
        //投票描述
        //String description = (String) context.get("description");
        //允许匿名投票,默认'否'N
        String isAnonymous =  context.get("isAnonymous")==null?"N":(String)context.get("isAnonymous");
        //允许重复投票,默认'是'N
        String allowMultiple = context.get("allowMultiple")==null?"N":(String)context.get("allowMultiple");
        //允许投票被更新,默认'是'Y
        String allowUpdate = context.get("allowUpdate")==null?"Y":(String)context.get("allowUpdate");
        //投票项描述 多个 暂不提供
        //String qsDescriptions = (String) context.get("qsDescriptions");
        //投票项名称 多个
        String questions = (String) context.get("questions");
        //投票项分类 默认Poll Questions 投票类型 1002
        String surveyQuestionCategoryId = "1002";
        //投票项目类型 默认布尔值
        String surveyQuestionTypeId   = "BOOLEAN";

        //将单个项分成数组进行循环创建
        String [] questionsArray = questions.split("&");



        //第一步、创建调查
        Map<String, Object> createSurveyInMap =
                UtilMisc.toMap("userLogin", userLogin,
                        "partyId", partyId,
                        "surveyName",surveyName,
                        "isAnonymous",isAnonymous,
                        "allowMultiple",allowMultiple,
                        "allowUpdate",allowUpdate);
        Map<String, Object> createSurveyResultMap = dispatcher.runSync("createSurvey", createSurveyInMap);
        //得到投票标题主键
        String surveyId = (String)createSurveyResultMap.get("surveyId");


        //第二步、创建调查对应的问题
        forEachCreateQuestionsToSurvey(surveyQuestionTypeId,surveyQuestionCategoryId,userLogin,surveyId,delegator,dispatcher,questionsArray);


        //第三步、将投票与活动关联起来

        Map<String, Object> createSurveyAppMap =
                UtilMisc.toMap("userLogin", userLogin,
                        "surveyId", surveyId,
                        "workEffortId",workEffortId ,
                        "fromDate",new Timestamp(new Date().getTime())
                       );
        dispatcher.runSync("createWorkEffortSurveyAppl", createSurveyAppMap);

        Map<String, Object> inputMap = new HashMap<String, Object>();
        Map<String, Object> result = ServiceUtil.returnSuccess();
        result.put("resultMap", inputMap);
        inputMap.put("resultMsg", UtilProperties.getMessage("PersonContactsUiLabels", "success", locale));
        return result;
    }

    /**
     * 循环创建投票项目
     * @param surveyId
     * @param delegator
     * @param dispatcher
     * @param questionsArray
     * @throws GenericEntityException
     * @throws GenericServiceException
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

    /**
     * 报名活动
     * @param dctx
     * @param context
     * @return
     * @throws IOException
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws InterruptedException
     */
    public static Map<String, Object> translationActivity(DispatchContext dctx, Map<String, ? extends Object> context)
            throws IOException, GenericEntityException, GenericServiceException, InterruptedException {

        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue admin = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", "admin"), false);
        // 登陆
        GenericValue userLogin = (GenericValue)context.get("userLogin");
        // 报名人id
        String partyId = (String) userLogin.get("partyId");
        Locale locale = (Locale) context.get("locale");


        //活动的id
        String workEffortId = (String) context.get("workEffortId");

        Map<String, Object> createPartyRoleMemberMap = UtilMisc.toMap("userLogin", admin, "partyId", partyId, "roleTypeId", "ACTIVITY_MEMBER");
        GenericValue isExsitsMember = delegator.findOne("PartyRole", UtilMisc.toMap("partyId", partyId, "roleTypeId", "ACTIVITY_MEMBER"), false);
        if (null == isExsitsMember) {
            dispatcher.runSync("createPartyRole", createPartyRoleMemberMap);
        }


        //是否作为'受邀人'存在与活动邀请列表
        EntityCondition findConditions = null;

        findConditions = EntityCondition
                .makeCondition(UtilMisc.toMap("partyId", partyId,"roleTypeId","ACTIVITY_INVITATION","workEffortTypeId","Event"));
       List<GenericValue> partyExsitEvents =   delegator.findList("WorkEffortAndPartyAssign", findConditions, null, null, null, false);
        if(null!=partyExsitEvents){
            //DO unassignPartyFromWorkEffort\
            for(GenericValue gv : partyExsitEvents){
                if(workEffortId.equals((String) gv.get("workEffortId"))){
                    Map<String, Object> updateMemberAssignPartyMap = UtilMisc.toMap("userLogin", admin, "partyId", partyId, "roleTypeId", "ACTIVITY_INVITATION", "fromDate", gv.get("fromDate"), "workEffortId", workEffortId);
                    dispatcher.runSync("unassignPartyFromWorkEffort", updateMemberAssignPartyMap);
                }
            }
        }

        Map<String, Object> createMemberAssignPartyMap = UtilMisc.toMap("userLogin", admin, "partyId", partyId, "roleTypeId", "ACTIVITY_MEMBER", "statusId", "PRTYASGN_ASSIGNED", "workEffortId", workEffortId);
        dispatcher.runSync("assignPartyToWorkEffort", createMemberAssignPartyMap);
        Map<String, Object> inputMap = new HashMap<String, Object>();
        inputMap.put("workEffortId",workEffortId);
        Map<String, Object> result = ServiceUtil.returnSuccess();
        result.put("resultMap", inputMap);
        inputMap.put("resultMsg", UtilProperties.getMessage("PersonContactsUiLabels", "success", locale));
        return result;
    }

    /**
     * 创建新的标签并且关联指定的联系人
     *
     * @param dctx
     * @param context
     * @return
     * @throws IOException
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws InterruptedException
     */
    public static Map<String, Object> lableAndRelationContact(DispatchContext dctx, Map<String, ? extends Object> context)
            throws IOException, GenericEntityException, GenericServiceException, InterruptedException {

        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        //模拟登陆
        GenericValue userLogin = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", "admin"), false);
        Locale locale = (Locale) context.get("locale");


        //标签的名字
        String groupId = (String) context.get("groupId");

        String partyArray = (String) context.get("partyArray");
//		Map<String,Object> createLableServiceReturn =  dispatcher.runSync("createLable", UtilMisc.toMap("userLogin", userLogin, "lableName", lableName));

        Map<String, Object> inputMap = new HashMap<String, Object>();

        inputMap.put("groupId", groupId);


        Map<String, Object> result = ServiceUtil.returnSuccess();
        result.put("result", inputMap);
        inputMap.put("resultMsg", UtilProperties.getMessage("PersonContactsUiLabels", "success", locale));
        return result;
    }


    /**
     * 给活动创建活动项
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

        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();

        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Locale locale = (Locale) context.get("locale");

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
        inputMap.put("resultMsg", UtilProperties.getMessage("PersonContactsUiLabels", "success", locale));
        return result;
    }


    /**
     * 创建新的活动
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> createNewEvent(DispatchContext dctx, Map<String, ? extends Object> context)
            throws IOException, GenericEntityException, GenericServiceException, InterruptedException {
        //服务头部
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Locale locale = (Locale) context.get("locale");


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
        //经纬度
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



        //5.群发邀请函 这个功能暂时不需要了
//        String contacts = (String) context.get("contactsList");

//        if(contacts!=null){
//            String [] arrays   = contacts.split(",");
//            forEachInvitationParty(arrays,dispatcher,userLogin,workEffortId,delegator);
//        }


        Map<String, Object> inputMap = new HashMap<String, Object>();
        inputMap.put("workEffortId", workEffortId);

        Map<String, Object> result = ServiceUtil.returnSuccess();
        result.put("resultMap", inputMap);
        inputMap.put("resultMsg", UtilProperties.getMessage("PersonContactsUiLabels", "success", locale));
        return result;
    }

    /**
     * 群发邀请函
     * @param contact
     * @param dispatcher
     * @param userLogin
     * @param delegator
     */
    private static void forEachInvitationParty(String[] contact, LocalDispatcher dispatcher, GenericValue userLogin, String workEffortId, Delegator delegator)throws GenericEntityException, GenericServiceException {
        if(contact!=null && contact.length>0){
            for(int index =0;index<contact.length;index++){
                GenericValue isExsitsInvitation = delegator.findOne("PartyRole", UtilMisc.toMap("partyId", contact[index], "roleTypeId", "ACTIVITY_INVITATION"), false);

                if(null==isExsitsInvitation){
                    Map<String, Object> createPartyRoleInvitationMap = UtilMisc.toMap("userLogin", userLogin, "partyId", contact[index], "roleTypeId", "ACTIVITY_INVITATION");
                    dispatcher.runSync("createPartyRole", createPartyRoleInvitationMap);
                }
                Map<String, Object> createMemberAssignPartyMap = UtilMisc.toMap("userLogin", userLogin, "partyId", contact[index], "roleTypeId", "ACTIVITY_INVITATION", "statusId", "PRTYASGN_ASSIGNED", "workEffortId", workEffortId);
                dispatcher.runSync("assignPartyToWorkEffort", createMemberAssignPartyMap);
            }
        }

    }

    /**
     * 添加联系人
     *
     * @param dctx
     * @param context
     * @return Map
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> addContacts(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        String personName = (String) context.get("personName");
        String lastName = personName.substring(0, 1);
        String firstName = personName.substring(1, personName.length());
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
            inputElcAddress.put("contactMechTypeId", "POSTAL_ADDRESS");
            inputElcAddress.put("contactMechPurposeTypeId", "PRIMARY_LOCATION");
            inputElcAddress.put("countryGeoId", "CHN");//国家
            inputElcAddress.put("stateProvinceGeoId", contactGeoName);//省
            if (UtilValidate.isNotEmpty(contactCity)) {
                GenericValue city = delegator.findOne("Geo", UtilMisc.toMap("geoId", contactCity), false);
                inputElcAddress.put("city", city.get("geoName"));//市
            }
            if (UtilValidate.isNotEmpty(contactAddress1)) {
                GenericValue city = delegator.findOne("Geo", UtilMisc.toMap("geoId", contactAddress1), false);
                inputElcAddress.put("address2", city.get("geoName"));//区
            }
            inputElcAddress.put("address1", contactAddress2);//详细地址
            inputElcAddress.put("postalCode", contactPostalCode);//邮政编码
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
    public static Map<String, Object> updateContacts(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        String personName = (String) context.get("personName");
        String lastName = personName.substring(0, 1);
        String firstName = personName.substring(1, personName.length());
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
        GenericValue postalAddress = EntityUtil.getFirst(delegator.findByAnd("findPostalAddressByPartyId",
                UtilMisc.toMap("partyId", partyId, "contactMechPurposeTypeId", "PRIMARY_LOCATION", "contactMechTypeId",
                        "POSTAL_ADDRESS"),
                null, false));
        // 原地址不为空
        if (UtilValidate.isNotEmpty(postalAddress)) {
            if (postalAddress.get("address1") != contactAddress1 || postalAddress.get("geoIdCity") != contactCity
                    || contactGeoName != postalAddress.get("stateProvinceGeoId")
                    || contactAddress2 != postalAddress.get("geoIdArea")) {
                if (UtilValidate.isEmpty(contactAddress1) && UtilValidate.isEmpty(contactGeoName)
                        && UtilValidate.isEmpty(contactPostalCode) && UtilValidate.isEmpty(contactPostalCode)) {
                    Map<String, Object> inputDelete = new HashMap<String, Object>();
                    inputDelete.put("partyId", partyId);
                    inputDelete.put("userLogin", userLogin);
                    inputDelete.put("contactMechId", postalAddress.get("contactMechId"));
                    Map<String, Object> deleteAddress = null;
                    deleteAddress = dispatcher.runSync("deletePartyContactMech", inputDelete);

                } else {
                    Map<String, Object> inputElc = new HashMap<String, Object>();
                    inputElc.put("partyId", partyId);
                    inputElc.put("postalCode", contactPostalCode);
                    inputElc.put("stateProvinceGeoId", contactGeoName);
                    if (UtilValidate.isNotEmpty(contactCity)) {
                        GenericValue city = delegator.findOne("Geo", UtilMisc.toMap("geoId", contactCity), false);
                        inputElc.put("city", city.get("geoName"));//市
                    }
                    if (UtilValidate.isNotEmpty(contactAddress1)) {
                        GenericValue city = delegator.findOne("Geo", UtilMisc.toMap("geoId", contactAddress1), false);
                        inputElc.put("address2", city.get("geoName"));//区
                    }
                    inputElc.put("address1", contactAddress2);
                    inputElc.put("contactMechId", postalAddress.get("contactMechId"));
                    inputElc.put("contactMechTypeId", "POSTAL_ADDRESS");
                    inputElc.put("contactMechPurposeTypeId", "PRIMARY_LOCATION");
                    inputElc.put("userLogin", userLogin);
                    Map<String, Object> updateAddress = null;
                    updateAddress = dispatcher.runSync("updatePartyPostalAddress", inputElc);
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
                EntityCondition.makeCondition(UtilMisc.toMap("partyIdTo", partyId, "partyRelationshipTypeId", "GROUP_ROLLUP")), EntityUtil.getFilterByDateExpr());
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
     * 删除联系人
     *
     * @param dctx
     * @param context
     * @return Map
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> deleteContacts(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        String partyIdTo = (String) context.get("partyIdTo");
        String partyIdFrom = (String) context.get("partyIdFrom");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        Map<String, Object> inputMap = new HashMap<String, Object>();
        // 模拟一个用户登录信息
        String userLoginId = "admin";
        GenericValue userLogin;
        userLogin = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", userLoginId), false);
        GenericValue contact = EntityUtil.getFirst(delegator.findByAnd("PartyRelationship",
                UtilMisc.toMap("partyIdTo", partyIdTo, "partyIdFrom", partyIdFrom, "partyRelationshipTypeId", "CONTACT_REL"), null, false));
        // 同步deletePartyRelationship服务删除联系人
        Map<String, Object> input = new HashMap<String, Object>();
        input.put("fromDate", contact.get("fromDate"));
        input.put("partyIdTo", partyIdTo);
        input.put("partyIdFrom", partyIdFrom);
        input.put("userLogin", userLogin);
        Map<String, Object> deleteContact = null;
        deleteContact = dispatcher.runSync("deletePartyRelationship", input);
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
        String userLoginId = (String) context.get("userLoginId");
        GenericValue userLogin;
        userLogin = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", userLoginId), false);
        // 同步createPartyGroup服务
        Map<String, Object> input = new HashMap<String, Object>();
        input.put("groupName", lableName);
        input.put("userLogin", userLogin);
        Map<String, Object> createLable = null;
        createLable = dispatcher.runSync("createPartyGroup", input);
        inputMap.put("partyId", createLable.get("partyId") + "");
        inputMap.put("groupName", lableName);
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
