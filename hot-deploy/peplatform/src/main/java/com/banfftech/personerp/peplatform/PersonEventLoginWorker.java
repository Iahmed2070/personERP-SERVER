/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package main.java.com.banfftech.personerp.peplatform;


import com.auth0.jwt.JWTExpiredException;
import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import org.apache.ofbiz.entity.util.EntityUtil;
import com.auth0.jwt.JWTVerifyException;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.DelegatorFactory;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityUtilProperties;
import org.apache.ofbiz.security.*;
import org.apache.ofbiz.security.Security;
import org.apache.ofbiz.security.SecurityConfigurationException;
import org.apache.ofbiz.security.SecurityFactory;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.webapp.control.ContextFilter;
import org.apache.ofbiz.webapp.WebAppUtil;
import org.apache.ofbiz.entity.condition.EntityConditionList;
import org.apache.ofbiz.webapp.control.LoginWorker;
import org.apache.ofbiz.entity.model.ModelEntity;
import org.apache.ofbiz.webapp.stats.VisitHandler;
import org.apache.ofbiz.service.ServiceUtil;
import org.apache.ofbiz.entity.condition.EntityCondition;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.ofbiz.service.DispatchContext;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.*;

/**
 * Common Workers
 */
public class PersonEventLoginWorker {

    public final static String module = PersonEventLoginWorker.class.getName();
    public static final String resourceWebapp = "SecurityextUiLabels";

    public static final String TOKEN_KEY_ATTR = "tarjeta";


    /**
     * 是否已经注册用户
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     */
    public static Map<String, Object> isUserLoginExsits(DispatchContext dctx, Map<String, Object> context)throws GenericEntityException {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        String userLoginId = (String) context.get("userLoginId");//teleNumber
        Map<String, Object> result = ServiceUtil.returnSuccess();
        Map<String, Object> inputMap = new HashMap<String, Object>();
        GenericValue userLogin = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", userLoginId), false);
        if(null!= userLogin){
            inputMap.put("resultMsg", org.apache.ofbiz.base.util.UtilProperties.getMessage("PePlatFromUiLabels", "PeLoginExsit", locale));
        }else{
            inputMap.put("resultMsg", org.apache.ofbiz.base.util.UtilProperties.getMessage("PePlatFromUiLabels", "success", locale));
        }
        result.put("resultMap",inputMap);
        return result;
    }
    /**
     * App登录
     * @param dctx
     * @param context
     * @return
     */
    public static Map<String, Object> userAppLogin(DispatchContext dctx, Map<String, Object> context)throws GenericEntityException {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        String userLoginId = (String) context.get("userLoginId");//teleNumber
       // String captcha = (String) context.get("captcha");
        //String appType = (String) context.get("appType");
        GenericValue userLogin = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId",userLoginId), false);
        String token = null;
        Map<String, Object> result = ServiceUtil.returnSuccess();
        String captcha = (String) context.get("captcha");

        Map<String, Object> inputMap = new HashMap<String, Object>();



//        //查找用户验证码是否存在
        EntityConditionList<EntityCondition> captchaConditions = EntityCondition
                .makeCondition(EntityCondition.makeCondition("teleNumber", EntityOperator.EQUALS, userLoginId),EntityUtil.getFilterByDateExpr(),EntityCondition.makeCondition("isValid", EntityOperator.EQUALS, "N"),EntityCondition.makeCondition("smsType", EntityOperator.EQUALS, "LOGIN"));
        List<GenericValue> smsList = new ArrayList<GenericValue>();
        try {
            smsList = delegator.findList("SmsValidateCode", captchaConditions, null,
                    UtilMisc.toList("-" + ModelEntity.CREATE_STAMP_FIELD), null, false);
        } catch (GenericEntityException e) {
            Debug.logError(e.getMessage(), module);
            //org.apache.ofbiz.base.util.UtilProperties.getMessage("PeInternalServiceError", "success", locale)
            return ServiceUtil.returnError(org.apache.ofbiz.base.util.UtilProperties.getMessage("PePlatFromUiLabels","PeInternalServiceError", locale));
        }

        if(UtilValidate.isEmpty(smsList)){
            return ServiceUtil.returnError(org.apache.ofbiz.base.util.UtilProperties.getMessage("PePlatFromUiLabels","PeCaptchaNotExistError", locale));
        }else{
            GenericValue sms = smsList.get(0);

            if(sms.get("captcha").equals(captcha)){
                //有效时间
                long expirationTime = Long.valueOf(EntityUtilProperties.getPropertyValue("pe","tarjeta.expirationTime","172800L",delegator));
                String iss = EntityUtilProperties.getPropertyValue("pe","tarjeta.issuer",delegator);
                String tokenSecret = EntityUtilProperties.getPropertyValue("pe","tarjeta.secret",delegator);
                //开始时间
                final long iat = System.currentTimeMillis() / 1000L; // issued at claim
                //Token到期时间
                final long exp = iat + expirationTime;
                //生成Token
                final JWTSigner signer = new JWTSigner(tokenSecret);
                final HashMap<String, Object> claims = new HashMap<String, Object>();
                claims.put("iss", iss);

                claims.put("user", userLoginId);

                claims.put("delegatorName", delegator.getDelegatorName());
                claims.put("exp", exp);
                claims.put("iat", iat);
                token = signer.sign(claims);



                //修改验证码状态
                sms.set("isValid", "Y");
                try {
                    sms.store();
                } catch (GenericEntityException e) {
                    Debug.logError(e.getMessage(), module);
                    return ServiceUtil.returnError(org.apache.ofbiz.base.util.UtilProperties.getMessage("PePlatFromUiLabels","PeInternalServiceError", locale));
                }

                inputMap.put("tarjeta", token);

                inputMap.put("resultMsg", org.apache.ofbiz.base.util.UtilProperties.getMessage("PePlatFromUiLabels", "PeLoginSuccess", locale));

                if(userLogin.get("partyId") == null){//没有找到用户
                    return ServiceUtil.returnError(org.apache.ofbiz.base.util.UtilProperties.getMessage("PePlatFromUiLabels","PeUserNotFundError", locale));
                }
                inputMap.put("partyId",userLogin.get("partyId"));

                result.put("resultMap",inputMap);
            }else{
                Debug.log("---------------------------------------------------------CaptERROR,xi"+sms.get("captcha")+""+"|"+captcha);
                return ServiceUtil.returnError(org.apache.ofbiz.base.util.UtilProperties.getMessage("PePlatFromUiLabels","PeCaptchaCheckFailedError", locale));
            }
        }

        return result;
    }


    /**
     * 验证是否拥身份令牌
     * @param request
     * @param response
     * @return
     */
    public static String checkTarjetaLogin(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Delegator delegator = (Delegator) request.getAttribute("delegator");

        Debug.logInfo("token verify...",module);
        String token = request.getParameter("tarjeta");
        // 这种事件里面只能返回success, 后面的其它预处理事件会继续采用其它方式验证登录情况
        if (token == null) return "success";

        // 验证token
        Delegator defaultDelegator = DelegatorFactory.getDelegator("default");//万一出现多租户情况，应在主库中查配置
        String tokenSecret = EntityUtilProperties.getPropertyValue("pe","tarjeta.secret", defaultDelegator);
        String iss = EntityUtilProperties.getPropertyValue("pe","tarjeta.issuer",delegator);

        Map<String, Object> claims;
        try {
             JWTVerifier verifier = new JWTVerifier(tokenSecret, null, iss);//验证token和发布者（云平台）
             claims= verifier.verify(token);
        }catch(JWTExpiredException e1){
            Debug.logInfo("token过期：" + e1.getMessage(),module);
            return "success";
        }catch (JWTVerifyException | InvalidKeyException | NoSuchAlgorithmException | IllegalStateException | SignatureException | IOException e) {
            Debug.logInfo("token没通过验证：" + e.getMessage(),module);
            return "success";
        }

        if(UtilValidate.isEmpty(claims)||UtilValidate.isEmpty(claims.get("user"))||UtilValidate.isEmpty(claims.get("delegatorName"))){
        	 Debug.logInfo("token invalid",module);
             return "success";
        }

        String userLoginId = (String) claims.get("user");
        String tokenDelegatorName = (String) claims.get("delegatorName");
        Delegator tokenDelegator = DelegatorFactory.getDelegator(tokenDelegatorName);
        GenericValue userLogin;
		try {
            userLogin = tokenDelegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", userLoginId),false);
		} catch (GenericEntityException e) {
			Debug.logError("some thing wrong when verify the token:" + e.getMessage(), module);
			return "success";
		}



        //TODO  User Check  使用阿里大鱼



        if (userLogin != null) {
            //in case  in different tenants
            String currentDelegatorName = delegator.getDelegatorName();
            ServletContext servletContext = session.getServletContext();
            if (!currentDelegatorName.equals(tokenDelegatorName)) {
            //	LocalDispatcher tokenDispatcher = ContextFilter.makeWebappDispatcher(servletContext, tokenDelegator);
            //    setWebContextObjects(request, response, tokenDelegator, tokenDispatcher);
            }
            // found userLogin, do the external login...

            // if the user is already logged in and the login is different, logout the other user
            GenericValue sessionUserLogin = (GenericValue) session.getAttribute("userLogin");
            if (sessionUserLogin != null) {
                if (sessionUserLogin.getString("userLoginId").equals(userLoginId)) {
                    // is the same user, just carry on...
                    return "success";
                }

                // logout the current user and login the new user...
                LoginWorker.logout(request, response);
                // ignore the return value; even if the operation failed we want to set the new UserLogin
            }

            LoginWorker.doBasicLogin(userLogin, request);

            //当token离到期时间少于多少秒，更新新的token，默认24小时（24*3600 = 86400L）
            long secondsBeforeUpdatetoken = Long.valueOf(EntityUtilProperties.getPropertyValue("pe", "tarjeta.secondsBeforeUpdate", "86400", defaultDelegator));

            long now = System.currentTimeMillis() / 1000L;
            Long oldExp = Long.valueOf(String.valueOf(claims.get("exp")));

            if(oldExp - now < secondsBeforeUpdatetoken){
            	// 快要过期了，新生成token
            	long expirationTime = Long.valueOf(EntityUtilProperties.getPropertyValue("pe", "tarjeta.expirationTime", "172800", defaultDelegator));
    			//开始时间
    			//Token到期时间
    			long exp = now + expirationTime;
    			//生成Token
    			JWTSigner signer = new JWTSigner(tokenSecret);
    			claims = new HashMap<String, Object>();
    			claims.put("iss", iss);
    			claims.put("user", userLoginId);
    			claims.put("delegatorName", tokenDelegatorName);
    			claims.put("exp", exp);
    			claims.put("iat", now);
    			request.setAttribute(TOKEN_KEY_ATTR, signer.sign(claims));
            }
        } else {
            Debug.logWarning("Could not find userLogin for token: " + token, module);
        }

        return "success";
    }


    private static void setWebContextObjects(HttpServletRequest request, HttpServletResponse response, Delegator delegator, LocalDispatcher dispatcher) {
        HttpSession session = request.getSession();
        // NOTE: we do NOT want to set this in the servletContext, only in the request and session
        // We also need to setup the security objects since they are dependent on the delegator
        Security security = null;
        try {
            security = SecurityFactory.getInstance(delegator);
        } catch (SecurityConfigurationException e) {
            Debug.logError(e, module);
        }
        request.setAttribute("delegator", delegator);
        request.setAttribute("dispatcher", dispatcher);
        request.setAttribute("security", security);

        session.setAttribute("delegatorName", delegator.getDelegatorName());
        session.setAttribute("delegator", delegator);
        session.setAttribute("dispatcher", dispatcher);
        session.setAttribute("security", security);

        // get rid of the visit info since it was pointing to the previous database, and get a new one
        session.removeAttribute("visitor");
        session.removeAttribute("visit");
        VisitHandler.getVisitor(request, response);
        VisitHandler.getVisit(session);
    }
}
