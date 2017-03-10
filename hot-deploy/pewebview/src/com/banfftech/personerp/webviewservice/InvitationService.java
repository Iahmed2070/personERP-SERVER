package com.banfftech.personerp.webviewservice;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by S.Y.L on 2017/3/10.
 */
public class InvitationService {

}
    public static Map<String, Object> findMyEventDetail(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = (String) userLogin.get("partyId");
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> inputMap = new HashMap<String, Object>();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        String workEffortId = (String) context.get("workEffortId");

}
