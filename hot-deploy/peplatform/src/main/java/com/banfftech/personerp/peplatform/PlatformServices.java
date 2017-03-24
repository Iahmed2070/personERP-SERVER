package main.java.com.banfftech.personerp.peplatform;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
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
import javax.servlet.ServletException;

/**
 * Created by Administrator on 2017/3/24.
 */
public class PlatformServices {



    public static final String module = PlatformServices.class.getName();

    private static String url = null;
    private static String appkey = null;
    private static String secret = null;
    private static String smsFreeSignName = null;
    private static String smsTemplateCode = null;

    /**
     * Get Sms Property
     * @param delegator
     */
    public static void getSmsProperty(Delegator delegator){
        url = EntityUtilProperties.getPropertyValue("pe","sms.url",delegator);
        appkey = EntityUtilProperties.getPropertyValue("pe","sms.appkey",delegator);
        secret = EntityUtilProperties.getPropertyValue("pe","sms.secret",delegator);
        smsFreeSignName = EntityUtilProperties.getPropertyValue("pe","sms.smsFreeSignName",delegator);
        smsTemplateCode = EntityUtilProperties.getPropertyValue("pe","sms.smsTemplateCode",delegator);
    }







    /**
     * Get Login Captcha
     *
     * @param dctx
     * @param context
     * @return
     */
    public static Map<String, Object> getLoginCaptcha(DispatchContext dctx, Map<String, Object> context) {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        String teleNumber = (String) context.get("teleNumber");
        String smsType = (String) context.get("smsType");//LOGIN 或 REGISTER
        java.sql.Timestamp nowTimestamp  = org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp();
        int validTime = Integer.valueOf(EntityUtilProperties.getPropertyValue("pe","sms.validTime","900",delegator));
        int intervalTime = Integer.valueOf(EntityUtilProperties.getPropertyValue("pe","sms.intervalTime","60",delegator));
        boolean sendSMS = false;




        //Find SmsData
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
            //TODO Sms-Ga EXCEPTION
        }






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

//                return ServiceUtil.returnError(PeSendFailedError"));
            }






            //Send Message
            context.put("phone", teleNumber);
            context.put("code", captcha);
            context.put("product", "不分梨");
            PlatformServices.sendMessage(dctx, context);
        }
        return ServiceUtil.returnSuccess();
    }


    /**
     * Send Message
     *
     * @param dctx
     * @param context
     * @return
     */
    public static Map<String, Object> sendMessage(DispatchContext dctx, Map<String, Object> context) {


        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();


        //Scope Param
        Locale locale = (Locale) context.get("locale");
        String phone = (String) context.get("phone");
        String code = (String) context.get("code");
        String product = (String) context.get("product");



        //Initial Message Config
        getSmsProperty(delegator);



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



        //Service Foot
        Map<String, Object> result = ServiceUtil.returnSuccess();
        Map<String, Object> inputMap = new HashMap<String, Object>();
        inputMap.put("resultMsg", UtilProperties.getMessage("PePlatFromUiLabels", "success", locale));
        result.put("resultMap", inputMap);
        return result;
    }
}
