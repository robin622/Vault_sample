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
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = (String) request.getSession().getAttribute("userName");
        String userEmail = (String) request.getSession().getAttribute("userEmail");

        String operation = request.getParameter("operation");
        operation = operation == null ? "" : operation;
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Cache-Control", "no-chche");
        if ("CanViewRequest".equalsIgnoreCase(operation)) {
            List<Request> canViewRequests = reqService.getCanViewRequests(userName, userEmail);
            request.setAttribute("canViewRequests", canViewRequests);
            request.setAttribute("operationstatus", "CanView");
        } else if ("WaitRequest".equalsIgnoreCase(operation)) {
            List<Request> waitRequests = reqService.getWaitRequests(userName, userEmail);
            request.setAttribute("waitRequests", waitRequests);
            request.setAttribute("operationstatus", "wait");
        } else if ("SignedRequest".equalsIgnoreCase(operation)) {
            List<Request> signedOffRequests = reqService.getSignedOffRequests(userName, userEmail);
            request.setAttribute("signedOffRequests", signedOffRequests);
            request.setAttribute("operationstatus", "signed");
        } else if ("CCToMeRequest".equalsIgnoreCase(operation)) {
            List<Request> ccToMeRequests = reqService.getCCToMeRequests(userName, userEmail);
            request.setAttribute("ccToMeRequests", ccToMeRequests);
            request.setAttribute("operationstatus", "cctome");
        } else if ("MyRequest".equalsIgnoreCase(operation)) {
            List<Request> myRequests = reqService.getMyRequest(userName);
            request.setAttribute("myRequests", myRequests);
            request.setAttribute("operationstatus", "myrequest");
        } else if ("NewRequest".equalsIgnoreCase(operation)) {
            request.setAttribute("operationstatus", "newrequest");
        } else if ("StaticsRequest".equalsIgnoreCase(operation)) {
            request.setAttribute("operationstatus", "staticsrequest");
        } else if ("Search".equalsIgnoreCase(operation)) {
            String requestName = request.getParameter("requestName");
            String type = request.getParameter("type");
            String creator = request.getParameter("creator");
            String versionid = request.getParameter("versionid");
            String productid = request.getParameter("productid");
            String status = request.getParameter("status");
            String owneremail = request.getParameter("owneremail");
            /*
             * if(requestName != null){ requestName = StringEscapeUtils.escapeHtml(requestName); } if(creator != null){ creator
             * = StringEscapeUtils.escapeHtml(creator); } if(owneremail != null){ owneremail =
             * StringEscapeUtils.escapeHtml(owneremail); }
             */
            List<Request> requests = reqService.advanceSearch(requestName, creator, versionid, productid, status, owneremail,
                    userName, userEmail);
            request.setAttribute("searchRequests", requests);
            request.setAttribute("is_search", "is_search");
        }
        Map<String, Long> counts = reqService.getRequestCount(userName, userEmail);
        request.setAttribute("reqCounts", counts);
        request.getRequestDispatcher("/jsp/home.jsp").forward(request, response);
    }

}
