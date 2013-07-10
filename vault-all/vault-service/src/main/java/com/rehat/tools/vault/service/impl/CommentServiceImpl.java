package com.rehat.tools.vault.service.impl;

import java.io.IOException;

import java.util.List;

import javax.inject.Inject;

import org.jboss.logging.Logger;

import net.sf.json.JSONObject;

import com.redhat.tools.vault.bean.Request;
import com.redhat.tools.vault.bean.RequestHistory;
import com.redhat.tools.vault.dao.RequestDAO;
import com.redhat.tools.vault.dao.RequestHistoryDAO;
import com.redhat.tools.vault.service.CommentService;
import com.redhat.tools.vault.util.DateUtil;

public class CommentServiceImpl implements CommentService{
	private static final Logger log = Logger.getLogger(CommentServiceImpl.class);
	@Inject
	private RequestDAO requestDAO;
	@Inject
	private RequestHistoryDAO historyDAO;
	@Inject
	private VaultSendMail mailer;
	
	public JSONObject AddComment(String requestid, String username,
			String useremail, String actionURL, String comment) {
		JSONObject joReturn = new JSONObject();
		RequestHistory history = new RequestHistory();
		String message = "";
		Request request = new Request();
		String historyid = null;
		List<RequestHistory> list = null;
		history.setRequestid(Long.parseLong(requestid));
		history.setEditedby(username);
		history.setEditedtime(DateUtil.getLocalUTCTime());
		history.setStatus(Request.COMMENTS);
		history.setComment(comment);
		history.setUseremail(useremail);
		history.setIsHistory(Boolean.FALSE);
		request.setRequestid(Long.parseLong(requestid));
		int requestVersion = requestDAO.get(request).get(0)
				.getRequestVersion();
		history.setRequestVersion(requestVersion);
		request.setEditedby(username);
		request.setEditedtime(DateUtil.getLocalUTCTime());
		request.setComment(comment);
		try {
			historyDAO.save(history);
			requestDAO.update(request);
			list = historyDAO.get(history, true);
			if (list != null && list.size() > 0) {
				historyid = list.get(0).getHistoryid().toString();
			}
			message = "Add comment success!";
			List<Request> requests = requestDAO.get(request);
			// send out eamil
			mailer.sendEmail(null, requests.get(0), null, actionURL,
					"showrequest", "comment", "");
		}
		catch (Exception e) {
			message = "Add comment failed!";
			log.error(e.getMessage());
		}
		finally {
			joReturn.put("message", message);
			joReturn.put("historyid", historyid);
		}
		return joReturn;
	}
	
}
