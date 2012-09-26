package com.rehat.tools.vault.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringEscapeUtils;
import org.jboss.logging.Logger;
import com.redhat.tools.vault.bean.Product;
import com.redhat.tools.vault.bean.Request;
import com.redhat.tools.vault.bean.Version;
import com.redhat.tools.vault.dao.ProductDAO;
import com.redhat.tools.vault.dao.RequestDAO;
import com.redhat.tools.vault.dao.VersionDAO;
import com.redhat.tools.vault.service.RequestService;

public class RequestServiceImpl implements RequestService{
	@Inject
	private RequestDAO requestDAO;
	@Inject
	private VersionDAO versionDAO;
	@Inject
	private ProductDAO productDAO;
	
	private static final Logger log = Logger.getLogger(RequestServiceImpl.class);

	public List<Request> getMyRequest(String username) {
		Request condition = new Request();
		condition.setCreatedby(username);
		List<Request> myRequests  = requestDAO.get(condition);
		if (myRequests != null && myRequests.size() > 0) {
			for (Request myRequest : myRequests) {
				myRequest.setRequestname(StringEscapeUtils
						.escapeHtml(myRequest.getRequestname()));
				try {
					setVersionAndProduct(myRequest);
					setParentAndChildren(myRequest);
				} catch (Exception e) {
					
				}
			}
		}
		return myRequests;
	}
	
	private void setVersionAndProduct(Request request) throws Exception {
		if (request.getVersionid() == -1) {
			request.setProductname("Unspecified");
			request.setVersiondesc("Unspecified");
		}
		else {
			Version version = new Version();
			List<Version> versions = null;
			Product product = new Product();
			List<Product> products = null;
			version.setId(request.getVersionid());
			versions = versionDAO.get(version);
			if (versions != null && versions.size() > 0) {
				request.setVersiondesc(versions.get(0).getValue());
				product.setId(versions.get(0).getProduct_id());
				products = productDAO.get(product);
				if (products != null && products.size() > 0) {
					request.setProductname(products.get(0).getName());
				}
			}
		}
	}
	
	private void setParentAndChildren(Request request) throws Exception {
		request.setParent(requestDAO.generateParent(request)[0]);
		request.setChildren(requestDAO.generateParent(request)[1]);
	}
}
