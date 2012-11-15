package com.redhat.tools.vault.web;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.xmlrpc.webserver.ContentAPI;
import org.apache.xmlrpc.webserver.XmlRpcServlet;
import org.jboss.logging.Logger;

import com.redhat.tools.vault.web.helper.VaultHelper;
import com.rehat.tools.vault.service.impl.VaultSendMail;

public class VaultXmlRpcServlet extends XmlRpcServlet {
	private static final Logger log = Logger.getLogger(VaultXmlRpcServlet.class);
	@Inject
	private VaultSendMail mailer;
	

	public void doPost(HttpServletRequest pRequest,
			HttpServletResponse pResponse) throws IOException, ServletException {
		log.info("*****************User:"+VaultHelper.getUserNameFromRequest(pRequest)+", visit VaultXmlRpcServlet method************");
		ContentAPI.setRequest(pRequest);
		ContentAPI.setMail(mailer);
		ContentAPI.setUser(VaultHelper.getUserNameFromRequest(pRequest));
		super.doPost(pRequest, pResponse);
	}
	
}
