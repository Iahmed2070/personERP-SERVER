package com.banfftech.peplatform.common;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.StringUtil;
import org.apache.ofbiz.base.util.UtilValidate;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 *这个过滤器是为了解决JS跨域问题
 */
public class CrossOriginFilter implements Filter {

	 public static final String module = com.banfftech.personerp.common.CrossOriginFilter.class.getName();
	 
	 public List<String> allowList;
	 
	 
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;  
        HttpServletResponse httpResponse = (HttpServletResponse) response;  
        String curOrigin = httpRequest.getHeader("Origin");
        Debug.logInfo("Request Origin:" + curOrigin, module);
        if(curOrigin != null && UtilValidate.isNotEmpty(allowList)) {  
            for (String origin : allowList) {
            	 // 如果allowList里面配置了*，就不用去匹配了，直接allow
                if("*".equals(origin) || curOrigin.equals(origin)) {  
                    httpResponse.setHeader("Access-Control-Allow-Origin", curOrigin);
                    httpResponse.setHeader("Access-Control-Allow-Methods", "POST");
                    httpResponse.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
                    break;
                }
            }  
        } 
        chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		  String allowedOrigin = config.getInitParameter("allowedOrigin");
		  allowList = StringUtil.split(allowedOrigin, ";");
		  
	}

}
