package com.redhat.tools.vault.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.redhat.tools.vault.bean.Request;
import com.redhat.tools.vault.service.RequestService;

/**
 * Servlet implementation class DeleteRequestServlet
 */
@WebServlet("/withDrawRequest")
public class WithDrawRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    @Inject
	private RequestService service;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WithDrawRequestServlet() {
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
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
	    res.setContentType("text/html;charset=UTF-8");
        res.setHeader("Cache-Control", "no-chche");
        String requestid = (String) req.getParameter("requestid");
        String requestVersion = (String) req.getParameter("requestversion");
        Request request = new Request();
        try {
            request.setRequestid(Long.parseLong(requestid));
            String userName=(String) req.getSession().getAttribute("userName");
            String userEmail=(String) req.getSession().getAttribute("userEmail");
            service.withdrawRequest(request,requestid,requestVersion,userName,userEmail);
            List<Request> myRequests = service.getMyRequest(userName);
            req.setAttribute("myRequests", myRequests);
            req.setAttribute("operationstatus", "myrequest");
            
            Map<String,Long> counts = service.getRequestCount(userName, userEmail);
            req.setAttribute("reqCounts", counts);
            req.getRequestDispatcher("/jsp/home.jsp").forward(req, res);
            return;
        }catch (Exception e) {
            e.printStackTrace();
        }
	}

}
