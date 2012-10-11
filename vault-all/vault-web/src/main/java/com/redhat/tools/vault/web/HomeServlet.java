package com.redhat.tools.vault.web;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.redhat.tools.vault.bean.Request;
import com.redhat.tools.vault.service.RequestService;

/**
 * @author wezhao
 * Servlet implementation class HomeServlet
 */
@WebServlet("/HomeServlet")
public class HomeServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Inject 
	private RequestService service;

    /**
     * Default constructor. 
     */
    public HomeServlet() {
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userName="wezhao";
		String userEmail="wezhao@redhat.com";
		request.getSession().setAttribute("userName", userName);
		request.getSession().setAttribute("userEmail", userEmail);
		List<Request> myRequests=service.getMyRequest(userName);
		boolean judgeDetailValue = false;
		request.setAttribute("myRequests", myRequests);
		request.setAttribute("judgeDetailValue", judgeDetailValue);
		request.getRequestDispatcher("/jsp/home.jsp").forward(request, response);
	}

}
