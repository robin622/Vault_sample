package com.rehat.tools.vault.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.jboss.logging.Logger;
import net.sf.json.JSONObject;
import com.redhat.tools.vault.bean.Request;
import com.redhat.tools.vault.bean.RequestMap;
import com.redhat.tools.vault.dao.RequestDAO;
import com.redhat.tools.vault.dao.RequestHistoryDAO;
import com.redhat.tools.vault.dao.RequestMapDAO;
import com.redhat.tools.vault.bean.RequestHistory;
import com.redhat.tools.vault.service.RequestHistoryService;

public class RequestHistoryServiceImpl implements RequestHistoryService {
	@Inject
	private RequestHistoryDAO requestHistoryDAO;
	@Inject
	private RequestDAO requestDAO;
	@Inject
	private RequestMapDAO requestMapDAO;
	
	private static final Logger log = Logger.getLogger(RequestHistoryServiceImpl.class);
	
	public JSONObject RequestHistory(String requestid) {
		List<Request> requests = null;
		JSONObject joReturn = new JSONObject();
		RequestHistory history = new RequestHistory();
		Request searchRequest = new Request();
		RequestHistory addHistory = null;
		List<RequestHistory> historys = new ArrayList<RequestHistory>();
		requests = null;
		List<RequestHistory> comments = new ArrayList<RequestHistory>();
		// List<RequestHistory> editedtimeHistorys = new
		// ArrayList<RequestHistory>();

		history.setRequestid(Long.parseLong(requestid));
		searchRequest.setRequestid(Long.parseLong(requestid));

		requests = requestDAO.get(searchRequest);
		Request request = requests.get(0);

		// get notifyMap
		Map<String, String> notifyMap = new HashMap<String, String>();
		RequestMap requestMap = new RequestMap();
		requestMap.setRequestid(Long.parseLong(requestid));
		requestMap.setRequestVersion(request.getRequestVersion());
		List<RequestMap> maps=new ArrayList<RequestMap>();
		try {
			maps = requestMapDAO.get(requestMap);
		} catch (Exception e1) {
			log.error(e1.getMessage());
		}
		if (maps != null && maps.size() > 0) {
			for (RequestMap rMap : maps) {
				if (rMap.getMapname().equals(
						Request.PROPERTY_REQUEST_NOTIFYCATION)) {
					notifyMap
							.put(rMap.getMapvalue()
									.substring(
											0,
											rMap.getMapvalue()
													.indexOf(":")),
									rMap.getMapvalue()
											.substring(
													rMap.getMapvalue()
															.indexOf(
																	":") + 1));
				}
			}
		}

		try {
			comments = requestHistoryDAO.get(history, true);
			// comments = historyDAO.get(Long.parseLong(requestid),
			// Boolean.FALSE);
			// 1. find all signatories thro request.getOwner();
			// 2. for each signatory, find all RequestHistory
			// instances. If has any record, (if is signed off, put
			// to history list. else put the latest record with
			// comments into history list). Else, put a simulated
			// one to history list.
			String[] signatories = request.getOwner().split(",");

			/*
			 * Remove, because will not list creator sign on behalf
			 * in the "sign off by" table List signList =
			 * Arrays.asList(signatories); if
			 * (!signList.contains(request.getCreatedby() +
			 * "@redhat.com") &&
			 * !signList.contains(request.getCreatedby() +
			 * "@REDHAT.COM")) { RequestHistory rHist = new
			 * RequestHistory();
			 * rHist.setEditedby(request.getCreatedby());
			 * rHist.setRequestid(Long.parseLong(requestid));
			 * rHist.setIsHistory(false); List<RequestHistory>
			 * rHists = historyDAO.get(rHist); if (rHists != null &&
			 * rHists.size() > 0) { for (RequestHistory rh : rHists)
			 * { if (rh.getStatus().equals(
			 * Request.SIGNED_ONBEHALF)) { rh.setDisplayFlag("1");
			 * historys.add(rh); break; } } } }
			 */

			for (int i = 0; i < signatories.length; i++) {
				RequestHistory rHist = new RequestHistory();
				rHist.setEditedby(signatories[i].substring(0,
						signatories[i].indexOf("@")).toLowerCase());
				rHist.setRequestid(Long.parseLong(requestid));
				rHist.setIsHistory(false);
				List<RequestHistory> rHists = new ArrayList<RequestHistory>();
				rHists = requestHistoryDAO.get(rHist, false);
				if (rHists != null && rHists.size() > 0) {
					RequestHistory finalHist = new RequestHistory();
					finalHist = rHists.get(0);
					for (int j = 0; j < rHists.size(); j++) {
						RequestHistory eachHist = rHists.get(j);
						if (eachHist.getStatus().equals(
								Request.SIGNED)
								|| eachHist.getStatus().equals(
										Request.SIGNED_BY)) {
							finalHist = eachHist;
							break;
						}
					}
					if (notifyMap.containsKey(finalHist
							.getUseremail())) {
						finalHist.setNotifiyOptionValue(notifyMap
								.get(finalHist.getUseremail()));
					}
					else { // for history record, default to
							// re-signoff
						finalHist
								.setNotifiyOptionValue(Request.NOTIFICATION_TYPE_REQUIRED_SIGNOFF);
					}
					finalHist.setDisplayFlag("0");
					historys.add(finalHist);
				}
				else {
					addHistory = new RequestHistory();
					addHistory.setRequestid(Long
							.parseLong(requestid));
					addHistory.setEditedby(signatories[i]);
					addHistory.setStatus(Request.WAITING);
					addHistory.setUseremail(signatories[i]);
					addHistory.setComment("");
					addHistory.setIsHistory(false);
					if (notifyMap.containsKey(signatories[i])) {
						addHistory.setNotifiyOptionValue(notifyMap
								.get(signatories[i]));
					}
					else {
						addHistory
								.setNotifiyOptionValue(Request.NOTIFICATION_TYPE_REQUIRED_SIGNOFF);
					}
					addHistory.setDisplayFlag("0");

					historys.add(addHistory);
				}
			}

			if (historys != null && historys.size() > 0) {
				for (RequestHistory h : historys) {
					if (h.getIsHistory()) {
						h.setStatus(Request.WAITING);
					}
				}
			}
			request.setParent(requestDAO.generateParent(request)[0]);
			request.setChildren(requestDAO.generateParent(request)[1]);

			// if (historyid != null) {
			// editedtimeHistory.setHistoryid(Long
			// .parseLong(historyid));
			// editedtimeHistory.setIsHistory(false);
			// editedtimeHistorys = historyDAO
			// .get(editedtimeHistory,false);
			// }

		}
		catch (Exception e) {
			log.error(e.getMessage());
		}
		finally {
			joReturn.put("comments", comments);
			joReturn.put("historys", historys);
			joReturn.put("requests", requests);
			// if (historyid != null) {
			// joReturn.put("editedtimeHistorys",
			// editedtimeHistorys);
			// }
		}
		return joReturn;
	}
}
