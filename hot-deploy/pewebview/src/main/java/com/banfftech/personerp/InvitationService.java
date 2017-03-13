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
import java.io.FileOutputStream;
import org.apache.ofbiz.entity.model.ModelEntity;
import java.io.IOException;
import java.util.*;

import main.java.com.banfftech.personerp.util.EncrypDES;
import javax.servlet.ServletException;

/**
 * Created by S.Y.L on 2017/3/10.
 */
public class InvitationService {


    /**
     * 给邀请人展示活动内容
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     */
    public static Map<String, Object> getInvitationDetail(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, UnsupportedEncodingException {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Locale locale = (Locale) context.get("locale");
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        //TODO DES 解密请求参数 直接拿
//        EncrypDES de1 = new EncrypDES();
//        byte [] param = (byte[]) context.get("pdo");
//        String s = new String(param.getBytes(), "GB2312");
        //        GenericValue userLogin = (GenericValue) context.get("userLogin");
//        String partyId = (String) userLogin.get("partyId");

        //活动id
        String workEffortId = (String) context.get("workEffortId");
        //其他参数
        String p_ctx   = (String) context.get("p_ctx");

        //受邀人手机
        String tel = null;
        //受邀人姓名
        String custName = null;
        //邀请人姓名
        String fromParty = null;
        //组装受邀人信息
        Map<String,Object> invitationPersonInfo = new HashMap<String, Object>();

        //有参数，开始分割获取
        if(null!=p_ctx && p_ctx.length()>1){
            String [] ptx_array = p_ctx.split(",");
              fromParty    = ptx_array[0].substring(ptx_array[0].indexOf(":")+1);
              workEffortId = ptx_array[1].substring(ptx_array[1].indexOf(":")+1);
              tel = ptx_array[2].substring(ptx_array[2].indexOf(":")+1);
              custName = ptx_array[3].substring(ptx_array[3].indexOf(":")+1);
              invitationPersonInfo = new HashMap<String, Object>();
              invitationPersonInfo.put("tel",tel);
              invitationPersonInfo.put("custName",custName);
        }






        //活动详情
        EntityCondition findConditions = null;
        findConditions = EntityCondition
                .makeCondition(UtilMisc.toMap("workEffortId", workEffortId));
        List<GenericValue> eventsDetail = null;
        //要查询的字段
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

        //活动详情返回
        if(null!=eventsDetail && eventsDetail.size()>=1){
            resultMap.put("eventsDetail",eventsDetail.get(0));
        }
        //受邀人信息返回(如果有)
        if(null!=invitationPersonInfo){
            resultMap.put("invitationPersonInfo",invitationPersonInfo);
        }



        //参与的人员其实是头像列表
        EntityCondition findConditionsToPartyContent = null;
        findConditionsToPartyContent = EntityCondition
                .makeCondition(UtilMisc.toMap("workEffortId",workEffortId,"roleTypeId","ACTIVITY_MEMBER"));
        List<GenericValue> partyJoinEventsList = null;
        partyJoinEventsList = delegator.findList("WorkEffortPartyAssignmentAndPerson",findConditionsToPartyContent,UtilMisc.toSet("workEffortId","partyId","nickname","lastName","firstName"),
                null, null, false);

        resultMap.put("partyJoinEventsList",partyJoinEventsList);
     return resultMap;
    }

}
