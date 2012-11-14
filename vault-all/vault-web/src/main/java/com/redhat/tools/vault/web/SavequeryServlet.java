package com.redhat.tools.vault.web;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.redhat.tools.vault.bean.Product;
import com.redhat.tools.vault.bean.Savequery;
import com.redhat.tools.vault.bean.Version;
import com.redhat.tools.vault.service.SavequeryService;
import com.redhat.tools.vault.util.DateUtil;
import com.redhat.tools.vault.util.StringUtil;

/**
 * Servlet implementation class SavequeryServlet
 */
@WebServlet("/SavequeryServlet")
public class SavequeryServlet extends HttpServlet {
    
	private static final long serialVersionUID = 1L;
    @Inject
	private SavequeryService queryService;
    
    private static Logger log = Logger.getLogger(SavequeryServlet.class);
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SavequeryServlet() {
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
		String operation = request.getParameter("operation");
		JSONArray array = new JSONArray();
		try{
    		if(operation.equals("getUserQuery")){
    		    try{
    		    	String userName=(String) request.getSession().getAttribute("userName");
    		        List<Savequery> querys = queryService.getUserquery(userName);
                    if(querys != null && querys.size() > 0){
                        for(Savequery query : querys){
                            JSONObject json = new JSONObject();
                            json.put("queryId", query.getQueryid());
                            json.put("queryName", query.getQueryname());
                            json.put("creator", query.getSearchcreator());
                            //json.put("createdTime", query.getCreatedtime());
                            json.put("owner", query.getOwner());
                            json.put("productId", query.getProductid());
                            json.put("searchCreator", query.getSearchcreator());
                            json.put("searchName", query.getSearchname());
                            json.put("status", query.getStatus());
                            json.put("type", query.getType());
                            json.put("versionId", query.getVersionid());
                            array.put(json);
                        }
                    }
    		    }catch(Exception e){
    		        log.error(e.getMessage(),e);
    		    }
    		}else if(operation.equals("verifyQueryName")){
    			String userName=(String) request.getSession().getAttribute("userName");
    		    JSONObject json = new JSONObject();
    		    
    		    String queryName = request.getParameter("queryName");
    		    Savequery query = new Savequery();
    		    query.setQueryname(queryName);
    		    query.setCreatedby(userName);
    		    try{
    		        List<Savequery> querys = queryService.seachrQuery(query);
                    if(querys != null && querys.size() > 0){
                        json.put("queryExist", true);
                    }else{
                        json.put("queryExist", false);
                    }
                    array.put(json);
    		    }catch(Exception e){
    		        log.error(e.getMessage(),e);
    		    }
    		}else if(operation.equals("saveQuery")){
    			String userName=(String) request.getSession().getAttribute("userName");
    		    JSONObject json = new JSONObject();
                String type             = (String) request.getParameter("type");
                String creator          = (String) request.getParameter("creator");
                String versionid        = (String) request.getParameter("versionId");
                String productid        = (String) request.getParameter("productId");
                String status           = (String) request.getParameter("status");
                String owneremail       = (String) request.getParameter("owner");
                String queryName        = (String) request.getParameter("queryName");
                String requestName      = (String) request.getParameter("requestName");
                Savequery query         = new Savequery();
                query.setQueryname(queryName);
                query.setCreatedby(userName);
                query.setCreatedtime(new Date());
                query.setSearchname(requestName);
                query.setType("1");
                try {
                    if(creator != null && !"".equals(creator)){
                        query.setSearchcreator(creator);
                    }
                    if(versionid != null && !"-1".equals(versionid)){
                        query.setVersionid(Long.parseLong(versionid));
                    }
                    if(productid != null && !"-1".equals(productid)){                         
                        query.setProductid(Long.parseLong(productid));
                    }
                    if(status != null && !"-1".equals(status)){
                        query.setStatus(status);
                    }
                    if(owneremail != null && !"".equals(owneremail)){
                        query.setOwner(owneremail);
                    }
                    Long queryId = queryService.saveQuery(query);
                    json.put("queryId", queryId);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
    		}else if(operation.equals("delQuery")){
    		    JSONObject json = new JSONObject();
    		    String queryId = request.getParameter("queryId");
    		    Savequery query = new Savequery();
    		    query.setQueryid(Long.parseLong(queryId));
    		    try{
    		        queryService.deleteQuery(query);
                    json.put("isSuccess", true);
    		    }catch(Exception e){
    		        log.error(e.getMessage(),e);
    		    }
    		}else if(operation.equals("getAllProduct")){
    		    List<Product> products = queryService.getAllProduct();
    		    if(products != null && products.size() > 0){
    		        for(Product p : products){
    		            JSONObject json = new JSONObject();
    		            json.put("productId", p.getId());
    		            json.put("productName", p.getName());
    		            array.put(json);
    		        }
    		    }
    		}else if(operation.equals("getProdVersion")){
    		    String productId = request.getParameter("productId");
    		    List<Version> versions = queryService.getProdVersion(Long.parseLong(productId));
    		    if(versions != null && versions.size() > 0){
                    for(Version v : versions){
                        JSONObject json = new JSONObject();
                        json.put("versionId", v.getId());
                        json.put("versionValue", v.getValue());
                        array.put(json);
                    }
                }
    		}
    		response.getWriter().print(array.toString());
		}catch(Exception e){
		    
		}
	}

}
