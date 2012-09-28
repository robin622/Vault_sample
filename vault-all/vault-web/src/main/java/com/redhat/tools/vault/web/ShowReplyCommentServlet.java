package com.redhat.tools.vault.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringEscapeUtils;

import com.redhat.tools.vault.bean.Request;
import com.redhat.tools.vault.service.ReplyService;
import com.redhat.tools.vault.service.RequestService;

/**
 * @author wezhao
 * Servlet implementation class HomeServlet
 */
@WebServlet("/ShowReplyComment")
public class ShowReplyCommentServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
    private ReplyService service;

    /**
     * Default constructor. 
     */
    public ShowReplyCommentServlet() {
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String userName = (String) request.getSession().getAttribute("userName");
        String userEmail = (String) request.getSession().getAttribute("userEmail");

        String operation = request.getParameter("operation");
        operation = operation == null ? "" : operation;
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Cache-Control", "no-chche");
        JSONObject joReturn = new JSONObject();
        String requestid = (String) request.getParameter("requestid");
        String historyid = (String) request.getParameter("historyid");
        joReturn = service.ShowReply(requestid, historyid);
        response.getWriter().print(joReturn);
	}
}
