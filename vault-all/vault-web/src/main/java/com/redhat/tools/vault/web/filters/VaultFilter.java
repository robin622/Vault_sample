package com.redhat.tools.vault.web.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.redhat.tools.vault.bean.MGProperties;
import com.redhat.tools.vault.web.helper.VaultHelper;

@WebFilter(urlPatterns = { "/Addcomment", "/AddReply", "/Checkrequest", "/deleteRequest", "/editVersion", "/FindRequest",
        "/ListNoneSign", "/showRequest", "/listRequest", "/ReportServlet", "/RequestHistory", "/SavequeryServlet",
        "/Saverequest", "/ShowAllEmails", "/ShowReplyComment", "/SignedOrRject", "/download", "/VaultFileUpload",
        "/vaultSumReport" })
public class VaultFilter implements Filter {

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
    }

    public void destroy() {

    }
}