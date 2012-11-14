package com.redhat.tools.vault.web;

import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import com.redhat.tools.vault.service.RequestService;

@WebServlet("/editVersion")
public class EditVersionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	@Inject 
    private RequestService reqService;

    public EditVersionServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 response.setContentType("application/json;charset=UTF-8");
	     response.setHeader("Cache-Control", "no-chche");
		JSONObject joReturn = new JSONObject();
		String versionid = (String) request.getParameter("id");
		joReturn=reqService.EditVersion(versionid);
		response.getWriter().print(joReturn);
	}

}
