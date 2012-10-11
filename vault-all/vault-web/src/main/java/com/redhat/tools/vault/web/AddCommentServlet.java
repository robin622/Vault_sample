package com.redhat.tools.vault.web;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import com.redhat.tools.vault.bean.ReplyComment;
import com.redhat.tools.vault.bean.Request;
import com.redhat.tools.vault.service.CommentService;
import com.redhat.tools.vault.service.ReplyService;
import com.redhat.tools.vault.service.RequestService;

/**
 * @author wezhao
 * Servlet implementation class HomeServlet
 */
@WebServlet("/Addcomment")
public class AddCommentServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Inject 
	private CommentService service;

    /**
     * Default constructor. 
     */
    public AddCommentServlet() {
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("Cache-Control", "no-chche");
		JSONObject joReturn = new JSONObject();
		String requestid = (String) request.getParameter("requestid");
		String username = (String) request.getParameter("username");
		String useremail = (String) request.getParameter("useremail");
		String actionURL = (String) request.getParameter("actionURL");
		String comment = request.getParameter("comment");
		comment = comment.replaceAll(" ", "&nbsp;");
		joReturn=service.AddComment(requestid, username, useremail, actionURL, comment);
		response.getWriter().print(joReturn);
	}
}