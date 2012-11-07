package com.redhat.tools.vault.web;

import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;

import com.redhat.tools.vault.bean.MGProperties;
import com.redhat.tools.vault.service.RequestService;

/**
 * @author wezhao
 * Servlet implementation class HomeServlet
 */
@WebServlet("/SignedOrRject")
public class SignedOrRejectServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Inject 
	private RequestService service;

    /**
     * Default constructor. 
     */
    public SignedOrRejectServlet() {
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userName=(String) request.getSession().getAttribute("userName");
		String userEmail=(String) request.getSession().getAttribute("userEmail");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("Cache-Control", "no-chche");
		JSONObject joReturn = new JSONObject();
		String requestId = request.getParameter("requestid");
		String username = request.getParameter("username");
		String comment = request.getParameter("comment");
		comment = comment.replaceAll(" ", "&nbsp;");
		String type = request.getParameter("type");
		String useremail = request.getParameter("useremail");
		String flag = request.getParameter("onbehalf");
		String onBehalfUsers = request.getParameter("onbehalfUsers");
		
		joReturn=service.SignedOrRject(requestId, username, comment, type, useremail, flag, onBehalfUsers);
		
		boolean isSignatory = service.displaySignButton(userName,Long.parseLong(requestId));
        boolean displaySignoffOnBehalf=service.displaySignOnBehalfButton(userName,Long.parseLong(requestId));
        
        joReturn.put("isSignatory", isSignatory);
        joReturn.put("displaySignoffOnBehalf", displaySignoffOnBehalf);
		response.getWriter().print(joReturn);
	}
}
