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
 * Servlet implementation class VaultReportServlet
 */
@WebServlet("/vaultSumReport")
public class VaultSumReportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    @Inject
	private RequestService service;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VaultSumReportServlet() {
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
	    String operation = req.getParameter("op");
	    if ("mr".equals(operation)) {
            String type = req.getParameter("tp");
            String selectedRpts = "";
            if (type.equals("search")) {
                selectedRpts = req.getParameter("selectedRptsSearch");
            }else {
                selectedRpts = req.getParameter("selectedRpts");
            }
            String[] rpts = selectedRpts.split("_");
            if (rpts == null || rpts.length < 1) {
                req.setAttribute("error",
                        "No request will be reported. Please select reports using checkbox.");
                //TODO dispatcher = req.getRequestDispatcher("/jsp/error.jsp");
            }
            Long[] requestIds = new Long[rpts.length];
            for (int i = 0; i < rpts.length; i++) {
                requestIds[i] = Long.parseLong(rpts[i]);
            }
            List<Request> requests = service.findByIds(requestIds);

            req.setAttribute("multiRequests", requests);
            req.setAttribute("type", type);
            req.setAttribute("selectedRpts", selectedRpts);
            req.getRequestDispatcher("/jsp/requestReport.jsp").forward(req, res);
        }
	}

}
