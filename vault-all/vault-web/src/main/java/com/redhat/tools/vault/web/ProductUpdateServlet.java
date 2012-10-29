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
import com.rehat.tools.vault.service.impl.BugzillaProductUpdate;


/**
 * 
 * @author maying
 *
 */
@WebServlet("/ProductUpdate")
public class ProductUpdateServlet extends HttpServlet  {

	private static final long serialVersionUID = 1L;

	@Inject
	private BugzillaProductUpdate updateService;
	
	/** The logger. */
	protected static final Logger log = Logger.getLogger(ProductUpdateServlet.class);

	public ProductUpdateServlet() {
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			JSONObject joReturn = new JSONObject();
			joReturn = updateService.productVersionUpdate();
			joReturn.put("flag", "success");
			response.getWriter().print(joReturn);
		} catch (Exception e) {
//			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
}
