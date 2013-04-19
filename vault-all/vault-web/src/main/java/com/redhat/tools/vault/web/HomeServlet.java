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
import com.redhat.tools.vault.web.helper.VaultHelper;

/**
 * @author wezhao Servlet implementation class HomeServlet
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName_init = (String) request.getSession().getAttribute("userName");
        if (userName_init == null) {
            String user = VaultHelper.getUserNameFromRequest(request);
            if ("".equals(user)) {
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }
        }
        String ref = (String) request.getSession().getAttribute("ref");
        if (null != ref && ref.contains("showRequest")) {
            request.getRequestDispatcher(ref).forward(request, response);
            request.getSession().removeAttribute("ref");
            return;
        }
        String userName = VaultHelper.getUserNameFromRequest(request);
        String userEmail = VaultHelper.getEmailFromName(userName);
        request.getSession().setAttribute("userName", userName);
        request.getSession().setAttribute("userEmail", userEmail);
        List<Request> myRequests = service.getMyRequest(userName);
        boolean judgeDetailValue = false;
        request.setAttribute("myRequests", myRequests);

        List<Request> waitReuqests = service.getWaitRequests(userName, userEmail);
        request.setAttribute("waitRequests", waitReuqests);

        request.setAttribute("judgeDetailValue", judgeDetailValue);

        Map<String, Long> counts = service.getRequestCount(userName, userEmail);
        request.setAttribute("reqCounts", counts);
        request.getRequestDispatcher("/jsp/home.jsp").forward(request, response);
    }
}
