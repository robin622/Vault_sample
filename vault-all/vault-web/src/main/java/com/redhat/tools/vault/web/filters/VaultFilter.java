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

@WebFilter(urlPatterns = { "/Addcomment", "/AddReply", "/Checkrequest",
		"/deleteRequest", "/editVersion", "/FindRequest", "/HomeServlet",
		"/ListNoneSign", "/listRequest", "/ReportServlet", "/RequestHistory",
		"/SavequeryServlet", "/Saverequest", "/ShowAllEmails",
		"/ShowReplyComment", "/showRequest", "/SignedOrRject", "/download",
		"/VaultFileUpload", "/vaultSumReport" })
public class VaultFilter implements Filter {

	public void init(FilterConfig config) throws ServletException {

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		XssServletWrapper xssRequest = new XssServletWrapper(
				(HttpServletRequest) request);
		chain.doFilter(xssRequest, response);
	}

	public void destroy() {

	}
}