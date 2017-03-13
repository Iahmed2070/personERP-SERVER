package main.java.com.banfftech.personerp;


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
import sun.net.www.content.text.Generic;
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
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.io.DataInputStream;
import org.apache.ofbiz.entity.model.ModelEntity;
import java.io.IOException;
import java.util.*;
import javax.servlet.ServletException;

/**
 * Created by Administrator on 2017/3/14.
 */
public class ActivityBaseService {

    /**
     * PartyJoin
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws UnsupportedEncodingException
     */
    public static Map<String, Object> newPartyJoinActivity(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, UnsupportedEncodingException,GenericServiceException {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Locale locale = (Locale) context.get("locale");
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        GenericValue userLogin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));


        //Parameters
        String workEffortId = (String) context.get("workEffortId");
        String tel = (String) context.get("tel");
        String nickName = (String) context.get("nickName");

        //JoinParty
        GenericValue joinParty = null;

        //1.FIND USER IS EXSITS
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

            //创建UserLogin
            Map<String, Object> createUserLoginInMap =
                    UtilMisc.toMap("userLogin", userLogin,
                            "userLoginId",tel,
                            "partyId",partyId,
                            "currentPassword","ofbiz","currentPasswordVerify","ofbiz"
                    );
            Map<String, Object> createUserLogin = null;
            createUserLogin = dispatcher.runSync("createUserLogin", createUserLoginInMap);
            //TODO 5. GRANT ABOUT PE PERMISSION
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

        //IF EXSITS Join
        Map<String, Object> translationActivityMap = UtilMisc.toMap("userLogin", joinParty, "workEffortId", workEffortId);
        Map<String, Object> transaltionResult = dispatcher.runSync("translationActivity", translationActivityMap);


        return resultMap;
    }
}
