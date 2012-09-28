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
 * Servlet implementation class ListRequestServlet
 */
@WebServlet("/listRequest")
public class ListRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	@Inject 
    private RequestService reqService;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListRequestServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String userName="wguo";
        String userEmail="wguo@redhat.com";

        String operation=request.getParameter("operation");
        operation=operation==null?"":operation;
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Cache-Control", "no-chche");
        if("CanViewRequest".equalsIgnoreCase(operation)){
            List<Request> canViewRequests = reqService.getCanViewRequests(userName, userEmail);
            request.setAttribute("canViewRequests", canViewRequests);
            request.setAttribute("operationstatus", "CanView");
            request.getRequestDispatcher("/jsp/home.jsp").forward(request, response);
        }else if("WaitRequest".equalsIgnoreCase(operation)){
            List<Request> waitRequests = reqService.getCanViewRequests(userName, userEmail);
            request.setAttribute("waitRequests", waitRequests);
            request.setAttribute("operationstatus", "wait");
            request.getRequestDispatcher("/jsp/home.jsp").forward(request, response);
        }else if("SignedRequest".equalsIgnoreCase(operation)){
            List<Request> signedOffRequests = reqService.getSignedOffRequests(userName, userEmail);
            request.setAttribute("signedOffRequests", signedOffRequests);
            request.setAttribute("operationstatus", "signed");
            request.getRequestDispatcher("/jsp/home.jsp").forward(request, response);
        }else if("CCToMeRequest".equalsIgnoreCase(operation)){
            List<Request> ccToMeRequests = reqService.getCCToMeRequests(userName, userEmail);
            request.setAttribute("ccToMeRequests", ccToMeRequests);
            request.setAttribute("operationstatus", "cctome");
            request.getRequestDispatcher("/jsp/home.jsp").forward(request, response);
        }else if("MyRequest".equalsIgnoreCase(operation)){
            List<Request> myRequests = reqService.getMyRequest(userName);
            request.setAttribute("myRequests", myRequests);
            request.setAttribute("operationstatus", "myrequest");
            request.getRequestDispatcher("/jsp/home.jsp").forward(request, response);
        }else if("NewRequest".equalsIgnoreCase(operation)){
            request.setAttribute("operationstatus", "newrequest");
            request.getRequestDispatcher("/jsp/home.jsp").forward(request, response);
        }else if("Search".equalsIgnoreCase(operation)){
            String          requestName       =   (String) request.getParameter("requestName");
            String          type            =   (String) request.getParameter("type");
            String          creator         =   (String) request.getParameter("creator");
            String          versionid       =   (String) request.getParameter("versionid");
            String          productid       =   (String) request.getParameter("productid");
            String          status          =   (String) request.getParameter("status");
            String          owneremail      =   (String) request.getParameter("owneremail");
            List<Request> requests = reqService.advanceSearch(requestName, creator, versionid, productid, status, owneremail, userName, userEmail);
            request.setAttribute("searchRequests", requests);
            request.setAttribute("is_search", "is_search");
            request.getRequestDispatcher("/jsp/home.jsp").forward(request, response);
        }
	}

}
