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

@WebServlet("/CountRequest")
public class StaticsServlet extends HttpServlet{

	   private static final long serialVersionUID = 1L;
		
			private static Logger log = Logger.getLogger(CheckRequestServlet.class);
		    @Inject
		    private RequestService service; 
		    
		    /**
		     * @see HttpServlet#HttpServlet()
		     */
		    public StaticsServlet() {
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
				response.setContentType("application/json;charset=UTF-8");
				response.setHeader("Cache-Control", "no-chche");
				JSONObject joReturn=service.staicsCount();
				response.getWriter().print(joReturn);
			
			}
}
