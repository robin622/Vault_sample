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
import com.redhat.tools.vault.service.RequestService;

/**
 * @author wezhao
 * Servlet implementation class HomeServlet
 */
@WebServlet("/ShowRequest")
public class ShowReplyCommentServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Inject 
	private RequestService service;

    /**
     * Default constructor. 
     */
    public ShowReplyCommentServlet() {
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userName=(String) request.getSession().getAttribute("userName");
		String userEmail=(String) request.getSession().getAttribute("userEmail");
		String requestid=request.getParameter("requestid");
		List<Request> detailRequests=service.getDetailRequest(requestid);
		String detailRequestName = null;
		Integer detailPublic = null;
		Request detailRequest = null;
		String detailCreator = null;
		String detailCCList = null;
		Boolean isEditable = false;
		Boolean isSignatory = false;
		Boolean displaySignoffOnBehalf = false;
		String requestName_unescape = null;

		boolean judgeDetailValue = false;
		if(detailRequests != null && detailRequests.size() > 0){
		    detailRequest = detailRequests.get(0);
		    Boolean isCreator = false;
		    Boolean isOwner = false;
		    Boolean isCC = false;
		    String detailOwnerList = null;
		    detailPublic = detailRequest.getIs_public();
		    detailCreator = detailRequest.getCreatedby();
		    detailCCList = detailRequest.getForward();
		    detailOwnerList = detailRequest.getOwner();
		    if(detailCreator != null && detailCreator.equals(userName)){
		            isCreator = true;
		    }        
		    if(detailOwnerList != null){
		            isOwner = Pattern.compile(userEmail).matcher(detailOwnerList).find();
		    }
		    if(detailCCList != null){
		            isCC = Pattern.compile(userEmail).matcher(detailCCList).find();
		    }
		    
		    if(detailPublic == 1 || isCreator || isOwner || isCC){
		            detailRequestName = detailRequest.getRequestname();
		            requestName_unescape = StringEscapeUtils.unescapeHtml(detailRequestName);
		            isEditable = detailRequest.displayEditButton(userName);
		            isSignatory = service.displaySignButton(userName,detailRequest.getRequestid());
		            displaySignoffOnBehalf=service.displaySignOnBehalfButton(userName,detailRequest.getRequestid());
		            judgeDetailValue = true;
		    }
		}
		request.setAttribute("detailRequest",detailRequest);
		request.setAttribute("isSignatory", isSignatory);
		request.setAttribute("isEditable", isEditable);
		request.setAttribute("displaySignoffOnBehalf", displaySignoffOnBehalf);
		request.setAttribute("requestName_unescape", requestName_unescape);
		request.setAttribute("judgeDetailValue", judgeDetailValue);
		request.getRequestDispatcher("/jsp/home.jsp").forward(request, response);
	}
}
