package com.redhat.tools.vault.web.filters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class XssServletWrapper extends HttpServletRequestWrapper {

	private HttpServletRequest orgRequest = null;

	public XssServletWrapper(HttpServletRequest request) {
		super(request);
		orgRequest = request;
	}

	@Override
	public String getParameter(String name) {
		String value = super.getParameter(name);
		if (value != null) {
			value = replaceCRLF(value);
		}
		return value;
	}

	@Override
	public String getHeader(String name) {
		String value = super.getHeader(name);
		if (value != null) {
			value = replaceCRLF(value);
		}
		return value;
	}

	public HttpServletRequest getOrgRequest() {
		return orgRequest;
	}

	public HttpServletRequest getOrgRequest(HttpServletRequest req) {
		if (req instanceof XssServletWrapper) {
			return ((XssServletWrapper) req).getOrgRequest();
		}
		return req;
	}

	private static String replaceCRLF(String s) {
		if (s.toLowerCase().contains("0d%") || s.toLowerCase().contains("0a%")) {
			s.replaceAll("0d%", "");
			s.replaceAll("0D%", "");
			s.replaceAll("0a%", "");
			s.replaceAll("0A%", "");
		}
		return s;
	}
}