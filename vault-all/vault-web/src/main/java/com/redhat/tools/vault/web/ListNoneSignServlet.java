package com.redhat.tools.vault.web;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.redhat.tools.vault.bean.Request;
import com.redhat.tools.vault.service.RequestService;

/**
 * @author wezhao
 * Servlet implementation class HomeServlet
 */
@WebServlet("/ListNoneSign")
public class ListNoneSignServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Inject 
	private RequestService service;

    /**
     * Default constructor. 
     */
    public ListNoneSignServlet() {
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-chche");
		String requestId = request.getParameter("requestId");
		JSONObject joReturn=service.listNoneSign(requestId);
		response.getWriter().print(joReturn);
	}

}
