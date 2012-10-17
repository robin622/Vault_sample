package com.redhat.tools.vault.web;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import com.redhat.tools.vault.bean.Request;
import com.redhat.tools.vault.service.ReportService;
import com.redhat.tools.vault.service.RequestService;

/**
 * @author wezhao
 * Servlet implementation class HomeServlet
 */
@WebServlet("/ReportServlet")
public class ReportServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Inject 
	private ReportService service;

    /**
     * Default constructor. 
     */
    public ReportServlet() {
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userName = (String) request.getSession().getAttribute("userName");
        String userEmail = (String) request.getSession().getAttribute("userEmail");
        String requestid = request.getParameter("id");
		String doExport = request.getParameter("doExport");
		Object[] objs=service.report(requestid);
		List<Request> requests=null;
		if(objs[5] != null){
			requests=(List<Request>)objs[5];
		}
		if("export".equals(doExport)){
			Element e_root = new Element("report");
			Document doc = new Document(e_root);
			doc=service.doExport(requests,request,objs[1],objs[2],objs[6],objs[7]);
			XMLOutputter xmlOut = new XMLOutputter();
			Format xml_format = Format.getPrettyFormat();
			xmlOut.setFormat(xml_format);
			response.setContentType("application/x-download");
			response.addHeader("content-disposition", "attachment; filename=\""
					+ "report_" 
					+ Long.toString(requests.get(0)
							.getRequestid()) + ".xml\"");
			OutputStream out = response.getOutputStream();
			xmlOut.output(doc,out);
			out.flush();
			return;
		}else{
			request.setAttribute("Status", objs[0]);
			request.setAttribute("reports", objs[1]);
			request.setAttribute("replys", objs[2]);
			request.setAttribute("Owners", objs[3]);
			request.setAttribute("LastEditedtime", objs[4]);
	//		request.setAttribute("Requests", objs[5]);
			request.setAttribute("pRequest", objs[6]);
			request.setAttribute("cRequests", objs[7]);
			if(objs[5] != null){
				if(requests.size() > 0){
					Request reportRequest=requests.get(0);
					String requestId = reportRequest.getRequestid().toString();
					String requestname = reportRequest.getRequestname();
					String productName = reportRequest.getProductname();
					String versionDesc = reportRequest.getVersiondesc();
					Date createdtime = reportRequest.getCreatedtime();
					Date duetime = reportRequest.getRequesttime();
					String detail = reportRequest.getDetail();
					String forward = reportRequest.getForward();
					request.setAttribute("reportRequest", reportRequest);
				}
			}
			request.getRequestDispatcher("/jsp/report.jsp").forward(request, response);
		}
	}
}
