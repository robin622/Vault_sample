package com.redhat.tools.vault.web;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.xmlrpc.webserver.ContentAPI;
import org.apache.xmlrpc.webserver.XmlRpcServlet;
//import org.apache.xmlrpc.webserver.XmlRpcServlet;

import com.rehat.tools.vault.service.impl.VaultSendMail;

public class VaultXmlRpcServlet extends XmlRpcServlet{
	@Inject 
	private VaultSendMail mailer ;
	
	public void doPost(HttpServletRequest pRequest,
			HttpServletResponse pResponse) throws IOException, ServletException {
		if(ContentAPI.getRequest()==null){
			ContentAPI.setRequest(pRequest);
			ContentAPI.setMail(mailer);
		}
		super.doPost(pRequest, pResponse);
	}
	 
}
