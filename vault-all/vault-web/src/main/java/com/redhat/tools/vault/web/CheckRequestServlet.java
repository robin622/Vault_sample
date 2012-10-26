package com.redhat.tools.vault.web;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.jboss.logging.Logger;

import com.redhat.tools.vault.service.RequestService;

/**
 * Servlet implementation class CheckRequest
 */
@WebServlet("/Checkrequest")
public class CheckRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static Logger log = Logger.getLogger(CheckRequestServlet.class);
    @Inject
    private RequestService service;   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckRequestServlet() {
        super();
        // TODO Auto-generated constructor stub
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
	protected void doPost(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
		String operation = req.getParameter("operation");
		if("CheckChild".equalsIgnoreCase(operation)){
		    JSONObject joReturn = new JSONObject();
            String result = "";
            String childStr = (String) req.getParameter("child");// requestId
                                                                    // requestName,
            String currentRequestId = (String) req.getParameter("id");//
            String message = "ok";
            try {
                if (childStr != null && !"".equals(childStr)) {
                    String[] childStrArray = childStr.split(",");
                    if (childStrArray != null
                            && childStrArray.length > 0) {
                        for (int i = 0; i < childStrArray.length; i++) {
                            Long requestId = Long
                                    .parseLong(childStrArray[i]
                                            .split("  ")[0]);
                            String requestName = childStrArray[i]
                                    .split("  ")[1];
                            String[] parentArray = service
                                    .generateParent(requestId);
                            if (parentArray != null
                                    && parentArray.length > 0) {
                                String parentStr = parentArray[0];
                                if (parentStr != null
                                        && parentStr.length() > 0) {
                                    if (!parentStr.split("##")[1]
                                            .split("  ")[0]
                                            .equals(currentRequestId)) {
                                        // childId+"  "+childName+##+parentId+"  "+parentName
                                        result += requestId.toString()
                                                + "  "
                                                + requestName
                                                + "##"
                                                + parentStr.split("##")[1]
                                                + ",";
                                    }
                                }
                            }
                        }
                    }
                }
                if (result.length() > 0) {
                    result = result.substring(0, result.length() - 1);
                }
            } catch (Exception e) {
                message = "error";
                log.error(e.getMessage());
            } finally {
                joReturn.put("result", result);
                joReturn.put("message", message);
                response.getWriter().print(joReturn);
            }
		}
	}

}
