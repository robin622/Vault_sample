package com.redhat.tools.vault.web.filters;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSessionContext;

import com.redhat.tools.vault.bean.MGProperties;
import com.redhat.tools.vault.util.PropertiesUtil;
import com.redhat.tools.vault.web.helper.VaultHelper;

@WebFilter(urlPatterns = { "/Addcomment", "/AddReply", "/Checkrequest", "/deleteRequest", "/editVersion", "/FindRequest",
        "/ListNoneSign", "/showRequest", "/listRequest", "/ReportServlet", "/RequestHistory", "/SavequeryServlet",
        "/Saverequest", "/ShowAllEmails", "/ShowReplyComment", "/SignedOrRject", "/download", "/VaultFileUpload",
        "/vaultSumReport" })
public class VaultFilter  implements Filter {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void init(FilterConfig config) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse rsp = (HttpServletResponse) response;
        String userName = (String) req.getSession().getAttribute("userName");
        if (userName == null) {
            String user = VaultHelper.getUserNameFromRequest(req);
            if ("".equals(user)) {
                req.getSession().setAttribute("ref",
                        req.getRequestURI() + (req.getQueryString() == null ? "" : "?" + req.getQueryString()));
                rsp.sendRedirect("/");
                // req.getRequestDispatcher("login.jsp").forward(req, rsp);
                return;
            }
            String userEmail = VaultHelper.getEmailFromName(user);
            req.getSession().setAttribute("userName", user);
            req.getSession().setAttribute("userEmail", userEmail);            
        }

        MGProperties.NAME_SERVER = req.getServerName();
        MGProperties.PORT_SERVER = req.getServerPort();

        XssServletWrapper xssRequest = new XssServletWrapper((HttpServletRequest) request);
        chain.doFilter(xssRequest, response);
        
        
        //read roles.properties
        String path = req.getServletContext().getRealPath("/");;
		String filePath = path + "WEB-INF/classes/roles.properties";
		Properties prop = PropertiesUtil.readProperties(filePath);
		File file = new File(filePath);
		 		
		long lastModified = file.lastModified();
		String lastmodifiedtime_s =String.valueOf(req.getServletContext()
		 				.getAttribute("lastmodifiedtime"));
		if (lastmodifiedtime_s!="null"&&Long.parseLong(lastmodifiedtime_s) == lastModified) {
		 			// do nothing
		 } else {
		 	req.getServletContext().setAttribute("lastmodifiedtime",
		 					lastModified);
		 	Map<String,String> map = (Map<String, String>) req.getServletContext().getAttribute("userRoles");
		 	if(map!=null){
		 		getRole(prop, map);
		 		req.getServletContext().setAttribute("userRoles", map);
		 	}else{
		 		Map<String, String> userRoles = new TreeMap<String, String>();
		 		getRole(prop, userRoles);
		 		req.getServletContext().setAttribute("userRoles", userRoles);
		 			}
		 		}
       
    }

    public void destroy() {

    }

    private void getRole(Properties prop, Map<String, String> userRoles) {
		Enumeration en = prop.propertyNames();
		while (en.hasMoreElements()) {
			String name = (String) en.nextElement();
			String value = prop.getProperty(name);
			userRoles.put(name, value);
		}
}
    
   
}