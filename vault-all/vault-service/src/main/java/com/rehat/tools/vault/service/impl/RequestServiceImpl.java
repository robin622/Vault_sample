package com.rehat.tools.vault.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.jboss.logging.Logger;

import com.redhat.tools.vault.bean.Product;
import com.redhat.tools.vault.bean.Request;
import com.redhat.tools.vault.bean.RequestHistory;
import com.redhat.tools.vault.bean.Version;
import com.redhat.tools.vault.dao.ProductDAO;
import com.redhat.tools.vault.dao.RequestDAO;
import com.redhat.tools.vault.dao.RequestHistoryDAO;
import com.redhat.tools.vault.dao.VAUserDAO;
import com.redhat.tools.vault.dao.VersionDAO;
import com.redhat.tools.vault.service.RequestService;
import com.redhat.tools.vault.util.DateUtil;

public class RequestServiceImpl implements RequestService{
	@Inject
	private RequestDAO requestDAO;
	@Inject
	private VersionDAO versionDAO;
	@Inject
	private ProductDAO productDAO;
	@Inject
	private VAUserDAO userDAO;
	@Inject
	private RequestHistoryDAO historyDAO;
	@Inject
	private VaultSendMail mailer;
	
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

	public List<Request> getDetailRequest(String requestid) {
		Request detailRequest = new Request();
		List<Request> detailRequests = null;
		detailRequest.setRequestid(Long.parseLong(requestid));
		try {
			detailRequests = requestDAO.get(detailRequest);
			if (detailRequests != null && detailRequests.size() > 0) {
				for (Request tempRequest : detailRequests) {
					tempRequest.setRequestname(StringEscapeUtils
							.escapeHtml(tempRequest.getRequestname()));
					setVersionAndProduct(tempRequest);
					setParentAndChildren(tempRequest);
				}
			}
		}
		catch (Exception e) {
			log.error(e.getMessage());
		}
		return detailRequests;
	}

	public boolean displaySignButton(String userName, long requestId) {
		RequestDAO rDAO = new RequestDAO();
		RequestHistoryDAO rhDAO = new RequestHistoryDAO();
		Request condition = new Request();
		condition.setRequestid(requestId);
		try {
			List<Request> requests = rDAO.get(condition);
			if (requests != null && requests.size() > 0) {
				Request request = requests.get(0);
				if (!request.displaySignoffRejectButton(userName)) {
					return false;
				}
				RequestHistory histCondition = new RequestHistory();
				histCondition.setEditedby(userName);
				histCondition.setRequestid(requestId);
				histCondition.setIsHistory(false);
				List<RequestHistory> rHists = rhDAO.get(histCondition, false);
				if (rHists != null && rHists.size() > 0) {
					for (RequestHistory rh : rHists) {
						if (rh.getStatus().equals(Request.SIGNED)
								|| rh.getStatus().equals(Request.SIGNED_BY)
								|| rh.getStatus().equals(Request.REJECTED)) {
							return false;
						}
					}
				}
				return true;
			}
		}
		catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	public boolean displaySignOnBehalfButton(String userName, Long requestId) {
		RequestDAO rDAO = new RequestDAO();
		RequestHistoryDAO rhDAO = new RequestHistoryDAO();
		Request condition = new Request();
		condition.setRequestid(requestId);
		try {
			List<Request> requests = rDAO.get(condition);
			if (requests != null && requests.size() > 0) {
				Request request = requests.get(0);
				if (!request.displaySignoffOnBehalfButton(userName)) {
					return false;
				}

				// other singatories have all signed off. return false.
				String[] signarories = request.getOwner().split(",");
				int number = 0;
				boolean isInSignatoryList = false;
				for (String sg : signarories) {
					String editor = sg.substring(0, sg.indexOf("@"));
					if (!editor.equals(userName)) {
						RequestHistory histCondition = new RequestHistory();
						histCondition.setEditedby(editor);
						histCondition.setRequestid(requestId);
						histCondition.setIsHistory(false);
						List<RequestHistory> rHists = rhDAO.get(histCondition,
								false);
						if (rHists != null && rHists.size() > 0) {
							for (RequestHistory rh : rHists) {
								if (rh.getStatus().equals(Request.SIGNED)
										|| rh.getStatus().equals(
												Request.SIGNED_BY)) {
									number += 1;
								}
							}
						}
					}
				}

				// others all signed
				if (isInSignatoryList && number == signarories.length - 1) {
					return false;
				}

				// all signed
				if (!isInSignatoryList && number == signarories.length) {
					return false;
				}
				return true;
			}
		}
		catch (Exception e) {
			return false;
		}
		return false;
	}

	public JSONObject findRequest() {
		JSONObject joReturn = new JSONObject();
		List<String> requests_s = new ArrayList<String>();
		try {
			requestDAO = new RequestDAO();
			List<Request> rqsts = requestDAO.findAll();
			if (rqsts != null && rqsts.size() > 0) {
				for (Request r : rqsts) {
					requests_s.add(r.getRequestid().toString() + "  "
							+ r.getRequestname());
				}
			}
		}
		catch (Exception e) {
			log.error(e.getMessage());
		}finally {
			joReturn.put("rqsts", requests_s);
		}
		return joReturn;
	}

	public JSONObject showAllEmails() {
		JSONObject joReturn = new JSONObject();
		List<String> emails = null;
		userDAO = new VAUserDAO();
		try {
			emails = userDAO.findAllUserEmails();
		}
		catch (Exception e) {
			log.error(e.getMessage());
		}
		finally {
			joReturn.put("emails", emails);
		}
		return joReturn;
	}

	public JSONObject listNoneSign(String requestId) {
		JSONObject json = new JSONObject();
		List<String> list = getNoneSigned(Long.parseLong(requestId));
		json.put("noneSignUsers", list);
		return json;
	}
	
	private List<String> getNoneSigned(Long requestId) {
		List<String> result = new ArrayList<String>();
		Request request = new RequestDAO().find(requestId);
		String[] ownerArr = request.getOwner().split(",");
		RequestHistory rh = new RequestHistory();
		rh.setRequestid(request.getRequestid());
		rh.setIsHistory(false);
		List<RequestHistory> list = new RequestHistoryDAO().get(rh, false);
		if (list != null && list.size() > 0) {
			for (String email : ownerArr) {
				boolean signed = false;
				for (RequestHistory h : list) {
					if (email.equalsIgnoreCase(h.getUseremail())
							&& (Request.SIGNED.equals(h.getStatus()) || Request.SIGNED_BY
									.equals(h.getStatus()))) {
						signed = true;
					}
				}

				if (!signed) {
					result.add(email.substring(0, email.indexOf("@")));
				}
			}
		}
		else {
			for (String email : ownerArr) {
				result.add(email.substring(0, email.indexOf("@")));
			}
		}

		return result;
	}

	public JSONObject SignedOrRject(String requestid, String username,
			String comment, String type, String useremail, String flag,
			String onBehalfUsers) {
		JSONObject joReturn = new JSONObject();
		String message = "";
		Long requestId = Long.parseLong(requestid);
		Request searchRequest = requestDAO.find(requestId);
		Integer requestVersion = searchRequest.getRequestVersion();
		Request request = new Request();
		request.setRequestid(requestId);
		boolean onBehalf = "2".equals(flag) ? true : false;

		if (Request.REJECTED.equals(type)) {
			RequestHistory rh = new RequestHistory();
			rh.setIsHistory(false);
			rh.setStatus(Request.REJECTED);
			rh.setRequestid(requestId);
			rh.setEditedby(username);
			rh.setEditedtime(DateUtil.getLocalUTCTime());
			rh.setComment(comment);
			rh.setUseremail(useremail);
			rh.setRequestVersion(requestVersion);
			try {
				historyDAO.save(rh);
			} catch (Exception e) {
				e.printStackTrace();
			}

			request.setComment(comment);
			request.setEditedby(username);
			request.setEditedtime(DateUtil.getLocalUTCTime());
			request.setSignedby(username);
			request.setSignedtime(DateUtil.getLocalUTCTime());
			requestDAO.update(request);
			message = "Reject success!";

			request = requestDAO.get(new Request(requestId)).get(0);
			try {
				mailer.sendEmail(null, request, null, null,
						"showrequest", "reject", "");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		else if (!onBehalf) {
			RequestHistory rh = new RequestHistory();
			rh.setIsHistory(false);
			rh.setStatus(Request.SIGNED);
			rh.setRequestid(requestId);
			rh.setEditedby(username);
			rh.setEditedtime(DateUtil.getLocalUTCTime());
			rh.setComment(comment);
			rh.setUseremail(useremail);
			rh.setRequestVersion(requestVersion);
			try {
				historyDAO.save(rh);
			} catch (Exception e) {
				e.printStackTrace();
			}

			request.setComment(comment);
			request.setEditedby(username);
			request.setEditedtime(DateUtil.getLocalUTCTime());
			request.setSignedby(username);
			request.setSignedtime(DateUtil.getLocalUTCTime());
			if (allSigned(searchRequest)) {
				request.setStatus(Request.INACTIVE);
			}
			requestDAO.update(request);

			request = requestDAO.get(new Request(requestId)).get(0);

			try {
				mailer.sendEmail(null, request, null, null,
						"showrequest", "signoff", "");
			} catch (Exception e) {
				e.printStackTrace();
			}

			message = "Sign Off success!";

		}
		else {

			String[] userArray = onBehalfUsers.split(",");
			for (String user : userArray) {
				if (StringUtils.isBlank(user)) {
					continue;
				}
				RequestHistory rh = new RequestHistory();
				rh.setIsHistory(false);
				rh.setStatus(Request.SIGNED_BY);
				rh.setRequestid(requestId);
				rh.setEditedby(user);
				rh.setEditedtime(DateUtil.getLocalUTCTime());
				// rh.setComment(comment);
				rh.setComment("");
				rh.setUseremail(user + "@redhat.com");
				rh.setRequestVersion(requestVersion);
				try {
					historyDAO.save(rh);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// save request history for creator
			RequestHistory rh = new RequestHistory();
			rh.setIsHistory(false);
			rh.setStatus(Request.SIGNED_ONBEHALF);
			rh.setRequestid(requestId);
			rh.setEditedby(username);
			rh.setEditedtime(DateUtil.getLocalUTCTime());
			rh.setComment(comment);
			rh.setUseremail(useremail);
			rh.setRequestVersion(requestVersion);
			try {
				historyDAO.save(rh);
			} catch (Exception e) {
				e.printStackTrace();
			}

			request.setComment(comment);
			request.setEditedby(username);
			request.setEditedtime(DateUtil.getLocalUTCTime());
			request.setSignedby(username);
			request.setSignedtime(DateUtil.getLocalUTCTime());

			if (allSigned(searchRequest)) {
				request.setStatus(Request.INACTIVE);
			}

			requestDAO.update(request);

			request = requestDAO.get(new Request(requestId)).get(0);
			VaultSendMail mailer = new VaultSendMail();
			message = "Sign Off success!";
			try {
				mailer.sendEmail(null, request, null, null,
						"showrequest", "signoffOnBehalf", "");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		joReturn.put("message", message);
		return joReturn;
	}
	
	private boolean allSigned(Request request) {

		String[] ownerArr = request.getOwner().split(",");
		int ownerCount = ownerArr.length;

		RequestHistory rh = new RequestHistory();
		rh.setRequestid(request.getRequestid());
		rh.setIsHistory(false);
		List<RequestHistory> list = new RequestHistoryDAO().get(rh, false);
		int signCount = 0;
		if (list != null && list.size() > 0) {
			for (String email : ownerArr) {
				for (RequestHistory h : list) {
					if (email.equalsIgnoreCase(h.getUseremail())
							&& (Request.SIGNED.equals(h.getStatus()) || Request.SIGNED_BY
									.equals(h.getStatus()))) {
						signCount++;
					}
				}
			}
		}
		if (ownerCount == signCount) {
			return true;
		}

		return false;

	}
}
