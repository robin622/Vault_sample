package org.apache.xmlrpc.webserver;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;

import com.redhat.tools.vault.bean.Request;
import com.redhat.tools.vault.bean.RequestHistory;
import com.redhat.tools.vault.bean.RequestMap;
import com.redhat.tools.vault.bean.RequestRelationship;
import com.redhat.tools.vault.bean.SendemailCount;
import com.redhat.tools.vault.dao.RequestDAO;
import com.redhat.tools.vault.dao.RequestHistoryDAO;
import com.redhat.tools.vault.dao.RequestMapDAO;
import com.redhat.tools.vault.dao.RequestRelationshipDAO;
import com.redhat.tools.vault.dao.SendemailCountDAO;
import com.redhat.tools.vault.util.DateUtil;
import com.redhat.tools.vault.util.StringUtil;
import com.rehat.tools.vault.service.impl.VaultSendMail;

public class ContentAPI{
	
	
	protected static final Logger log = Logger.getLogger(ContentAPI.class);
	private static HttpServletRequest pRequest=null;
	public static void setRequest(HttpServletRequest request){
		pRequest=request;
	}
	public static HttpServletRequest getRequest(){
		return pRequest;
	}
	private static VaultSendMail mailer=null;
	public int createRequest(String requestName, String description, String createdBy, String dueDate, String owner, String productId,
			String cc, String parent, String children, int is_public){
		if(null==requestName || requestName.trim().length()==0)return 0;
		if(null==description || description.trim().length()==0)return 0;
		if(null==createdBy || createdBy.trim().length()==0)return 0;
		if(null==dueDate || dueDate.trim().length()==0)return 0;
		if(null==owner || owner.trim().length()==0)return 0;
		if(owner.indexOf("@")==-1)return 0;
		int requestId=0;
//		owners=CollectionUtil.removeIllegalElement(owners);
//		StringBuffer owner=new StringBuffer("");
//		for(int i=0;i<owners.size();i++){
//			if(i!=owners.size()-1 && owners.size()>1){
//				owner.append(owners.get(i)).append(",");
//			}else{
//				owner.append(owners.get(i));
//			}
//		}
		owner = StringUtil.removeComma(owner.trim());
		Request request = new Request();
		request.setRequestname(requestName);
		request.setDetail(description);
		request.setCreatedby(createdBy);
		request.setOwner(owner.toString());
		if(productId!=null && productId.trim().length()!=0){
			try {
				request.setVersionid(Long.parseLong(productId));
			} catch (NumberFormatException e) {
				log.error(e.getMessage());
				return 0;
			}
		}else{
			request.setVersionid(-1L);
		}
		cc = StringUtil.removeComma(cc);
		request.setForward(cc);
		request.setParent(parent);
		children = StringUtil.removeComma(children);
		request.setChildren(children);
		
		
		request.setRequesttime(DateUtil.toVaultDate(dueDate));
		request.setCreatedtime(DateUtil.getLocalUTCTime());
		
		request.setIs_public(is_public);

		request.setFrom(1); //from Content module
		
//		Date date = new Date();
//		date=DateUtil.addHours(date,Integer.parseInt("24"));
		
		// save parent and child info
		RequestRelationshipDAO rDao = new RequestRelationshipDAO();
		if (parent != null && !"".equals(parent.trim())) {
			Long parentId;
			try {
				parentId = Long.parseLong(parent.split("  ")[0]);
			} catch (NumberFormatException e) {
				log.error(e.getMessage());
				return 0;
			}
			RequestRelationship rls = new RequestRelationship();
			rls.setRequestId(request.getRequestid());
			rls.setRelationshipId(parentId);
			rls.setIsParent(true);
			rls.setRequestVersion(request.getRequestVersion());
			rls.setEnable(true);
			rDao.save(rls);
		}
		
		// 2. child
		if (!StringUtil.isEmpty(children)) {
			// 100 requestname,101 requestname
			String[] childArray = children.split(",");
			if (childArray != null && childArray.length > 0) {
				for (int i = 0; i < childArray.length; i++) {
					Long childId;
					try {
						childId = Long.parseLong(childArray[i]
								.split("  ")[0]);
					} catch (NumberFormatException e) {
						log.error(e.getMessage());
						return 0;
					}
					// remove relationship which childId as child,
					// as there is only one parent for this child
					// request.
					RequestRelationship childR = new RequestRelationship();
					childR.setRelationshipId(childId);
					childR.setIsParent(false);
					childR.setEnable(true);
					rDao.disable(childR);
					childR = new RequestRelationship();
					childR.setRequestId(childId);
					childR.setIsParent(true);
					childR.setEnable(true);
					rDao.disable(childR);

					// save new child
					childR = new RequestRelationship();
					childR.setRequestId(request.getRequestid());
					childR.setRelationshipId(childId);
					childR.setIsParent(false);
					childR.setEnable(true);
					childR.setRequestVersion(request
							.getRequestVersion());
					rDao.save(childR);
				}
			}
		}
		
		RequestDAO requestDAO = new RequestDAO();
		request.setStatus(Request.ACTIVE);
		
		try {
			requestId = Integer.parseInt(requestDAO.save(request).toString());
		} catch (NumberFormatException e) {
			log.error(e.getMessage());
			requestId=0;
		} catch (Exception e) {
			log.error(e.getMessage());
			requestId=0;
		} 
		
		//email
		try {
			SendemailCountDAO countDAO = new SendemailCountDAO();
			SendemailCount emailCount = new SendemailCount();
			try {
				emailCount.setRequestid(Long.parseLong(String.valueOf(requestId)));
			} catch (Exception e) {
				log.error(e.getMessage());
				return 0;
			}
			List<SendemailCount> sends = countDAO.get(emailCount);
			emailCount.setCount(0L);
			emailCount.setRequesttime(request.getRequesttime());
			if (sends != null && sends.size() > 0) {
				emailCount.setSendid(sends.get(0).getSendid());
				countDAO.update(emailCount);
			}
			else {
				countDAO.save(emailCount);
			}
			mailer.sendEmail(pRequest, request,null,"",
					"showrequest", "new", "");
		} catch (NumberFormatException e) {
			log.error(e.getMessage());
			log.error(e.getMessage());
			return 0;
		} catch (Exception e) {
			log.error(e.getMessage());
			log.error(e.getMessage());
			return 0;
		}
		
		return requestId;
	}
	
	public boolean withDrawRequest(String requestid,String userName,String userEmail){
		if(requestid==null||requestid.trim().length()==0)return false;
		if(userName==null||userName.trim().length()==0)return false;
		if(userEmail==null||userEmail.trim().length()==0)return false;
		Request request = new Request();
		RequestDAO requestDAO = new RequestDAO();
		
		try {
			request.setRequestid(Long.parseLong(requestid));
		} catch (NumberFormatException e1) {
			log.error(e1.getMessage());
			return false;
		}
		List<Request> withDrawRequest = null;
		withDrawRequest = requestDAO.get(request);
		if (withDrawRequest != null && withDrawRequest.size() > 0) {
			request.setSignedby(userName);
			request.setSignedtime(DateUtil.getLocalUTCTime());
			request.setStatus(Request.WITHDRAW);
			requestDAO.update(request);
			//save to history
			RequestHistoryDAO historyDAO = new RequestHistoryDAO();
			RequestHistory rh = new RequestHistory();
			rh.setIsHistory(false);
			rh.setStatus(Request.WITHDRAW);
			try {
				rh.setRequestid(Long.parseLong(requestid));
			} catch (NumberFormatException e1) {
				log.error(e1.getMessage());
				return false;
			}
			rh.setEditedby(userName);
			rh.setEditedtime(DateUtil.getLocalUTCTime());
			rh.setComment("");
			rh.setUseremail(userEmail);
			int requestVersion = requestDAO.get(request).get(0)
					.getRequestVersion();
			rh.setRequestVersion(requestVersion);
		//	rh.setRequestVersion(Integer.parseInt(requestVersion));
			try {	
				historyDAO.save(rh);
				VaultSendMail mailer = new VaultSendMail();
				mailer.sendEmail(pRequest, withDrawRequest.get(0),null,
						"", "", "withdraw", "");
			} catch (Exception e) {
				log.error(e.getMessage());
				return false;
			}
		}
		return true;
	}
	
	public String queryResult(String requestId){
		if(requestId==null || requestId.trim().length()==0)return "";
		RequestDAO requestDAO=new RequestDAO();
		Request rq=new Request();
		try {
			rq.setRequestid(Long.parseLong(requestId));
		} catch (NumberFormatException e) {
			log.error(e.getMessage());
			return "";
		}
		List<Request> requestList=requestDAO.get(rq);
		if(requestList==null || requestList.size()==0 || requestList.get(0)==null)return "";
		return requestList.get(0).getStatus();
	}
	
	public List<Map<String,String>> get_request_users_status(String requestid){
		RequestDAO requestDAO = null;
		RequestHistoryDAO historyDAO = null;
		RequestHistory addHistory = null;
		List<Request> requests = null;
		
		RequestHistory history = new RequestHistory();
		Request searchRequest = new Request();
		List<RequestHistory> historys = new ArrayList<RequestHistory>();
		List<RequestHistory> comments = new ArrayList<RequestHistory>();
		history.setRequestid(Long.parseLong(requestid));
		searchRequest.setRequestid(Long.parseLong(requestid));
	
		requestDAO = new RequestDAO();
		historyDAO = new RequestHistoryDAO();
		requests = requestDAO.get(searchRequest);
		if(requests.size()==0)return new ArrayList<Map<String,String>>();
		Request request = requests.get(0);
		
		RequestMapDAO requestMapDAO = new RequestMapDAO();
		Map<String, String> notifyMap = new HashMap<String, String>();
		RequestMap requestMap = new RequestMap();
		requestMap.setRequestid(Long.parseLong(requestid));
		requestMap.setRequestVersion(request.getRequestVersion());
		List<RequestMap> maps;
		try {
			maps = requestMapDAO.get(requestMap);
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
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
		
		comments = historyDAO.get(history, true);
		String[] signatories = request.getOwner().split(",");
		for (int i = 0; i < signatories.length; i++) {
			RequestHistory rHist = new RequestHistory();
			rHist.setEditedby(signatories[i].substring(0,
					signatories[i].indexOf("@")).toLowerCase());
			rHist.setRequestid(Long.parseLong(requestid));
			rHist.setIsHistory(false);
			List<RequestHistory> rHists = new ArrayList<RequestHistory>();
			rHists = historyDAO.get(rHist, false);
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
		//format output
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		for(RequestHistory rh:historys){
			String editedby="";
			if(rh.getEditedby().contains("@")){
				editedby=rh.getEditedby().substring(0,rh.getEditedby().indexOf("@"));
			}else{
				editedby=rh.getEditedby();
			}
			//list.add(new StringBuffer(editedby)
			//.append(",").append(rh.getStatus())
			//.append(",").append(rh.getEditDate()).toString());
			Map<String,String> map=new HashMap<String,String>();
			map.put("editedBy", editedby==null?"":editedby);
			map.put("status", rh.getStatus()==null?"":rh.getStatus());
			map.put("editeDate", rh.getEditDate()==null?"":rh.getEditDate());
			list.add(map);
		}
		return list;
	}
	
	//alexander requirement
	public List<Map<String,String>> request_created_by(String user, String start_date, String end_date){
		Date start=null;
		Date end=null;
		RequestDAO requestDAO=new RequestDAO();
		if(start_date!=null && start_date.trim().length()!=0){
			start=DateUtil.toVaultDate(start_date);
		}
		if(end_date!=null && end_date.trim().length()!=0){
			end=DateUtil.toVaultDate(end_date);
		}
		List<Request> list=requestDAO.getPeriodRequest(user, start, end);
		List<Map<String, String>> realList = formatRequestData(list);
		return realList;
	}
	
	public  List<Map<String,String>> request_signed_by(String user, String start_date, String end_date){
		Date start=null;
		Date end=null;
		RequestDAO requestDAO=new RequestDAO();
		if(start_date!=null && start_date.trim().length()!=0){
			start=DateUtil.toVaultDate(start_date);
		}
		if(end_date!=null && end_date.trim().length()!=0){
			end=DateUtil.toVaultDate(end_date);
		}
		List<Request> list=requestDAO.getPeriodSignedRequest(user, start, end);
		List<Map<String, String>> realList = formatRequestData(list);
		return realList;
	} 
	
	public List<Map<String,String>> get_comments(String request_id,String username,String start_date, String end_date){
		RequestHistoryDAO requestHistoryDAO=new RequestHistoryDAO();
		RequestHistory condition = new RequestHistory();
		if(request_id!=null && request_id.trim().length()>0){
			try {
				condition.setRequestid(Long.parseLong(request_id));
			} catch (NumberFormatException e) {
				log.error(e.getMessage());
				return new ArrayList<Map<String,String>>();
			}
		}
		return get_comments_common(username, start_date, end_date,
				requestHistoryDAO, condition);
	}
	
	public List<Map<String,String>> get_comments(String username,String start_date, String end_date){
		RequestHistoryDAO requestHistoryDAO=new RequestHistoryDAO();
		RequestHistory condition = new RequestHistory();
		return get_comments_common(username, start_date, end_date,
				requestHistoryDAO, condition);
	}
	
	private List<Map<String, String>> get_comments_common(String username,
			String start_date, String end_date,
			RequestHistoryDAO requestHistoryDAO, RequestHistory condition) {
		if(username!=null && username.trim().length()>0){
			condition.setEditedby(username);
		}
		if(start_date!=null&& start_date.trim().length()!=0){
			condition.setStartDate(start_date);
		}
		if(end_date!=null&& end_date.trim().length()!=0){
			condition.setEndDate(end_date);
		}
		List<RequestHistory> list=requestHistoryDAO.get(condition, false);
		List<Map<String, String>> realList = formatRequestData(list);
		return realList;
	}
	
	private List<Map<String, String>> formatRequestData(List list) {
		List<Map<String,String>> realList=new ArrayList<Map<String,String>>();
		if(list!=null&&list.size()>0){
			if(list.get(0) instanceof Request){
				for(int i=0;i<list.size();i++){
					Map<String,String> map=new HashMap<String,String>();
					transferToRequest((Request)list.get(i),map);
					realList.add(map);
				}
				return realList;
			}else if(list.get(0) instanceof RequestHistory){
				for(int i=0;i<list.size();i++){
					Map<String,String> map=new HashMap<String,String>();
					transferToRequestHistory((RequestHistory)list.get(i),map);
					realList.add(map);
				}
				return realList;
			}
		}
		return realList;
	}
	
	private void transferToRequest(Request rq, Map<String, String> map) {
		map.put(rq.PROPERTY_REQUESTID,String.valueOf(rq.getRequestid())==null?"":String.valueOf(rq.getRequestid()));
		map.put(rq.PROPERTY_REQUESTNAME,rq.getRequestname()==null?"":rq.getRequestname());
		map.put(rq.PROPERTY_SUMMARY,rq.getSummary()==null?"":rq.getSummary());
		map.put(rq.PROPERTY_DETAIL, rq.getDetail()==null?"":rq.getDetail());
		map.put(rq.PROPERTY_OWNER, rq.getOwner()==null?"":rq.getOwner());
		map.put(rq.PROPERTY_CREATEDTIME, String.valueOf(rq.getCreatedtime())==null?"":String.valueOf(rq.getCreatedtime()));
		map.put(rq.PROPERTY_CREATEDBY, rq.getCreatedby()==null?"":rq.getCreatedby());
		map.put(rq.PROPERTY_REQUESTTIME, rq.getRequesttime()==null?"":rq.getRequesttime().toString());
		map.put(rq.PROPERTY_SIGNEDBY, rq.getSignedby()==null?"":rq.getSignedby());
		map.put(rq.PROPERTY_SIGNEDTIME, rq.getSignedtime()==null?"":rq.getSignedtime().toString());
		map.put(rq.PROPERTY_STATUS, rq.getStatus()==null?"":rq.getStatus());
		map.put(rq.PROPERTY_COMMET, rq.getComment()==null?"":rq.getComment());
		map.put(rq.PROPERTY_VERSIONID, String.valueOf(rq.getVersionid())==null?"":String.valueOf(rq.getVersionid()));
		map.put(rq.PROPERTY_IS_PUBLIC,String.valueOf(rq.getIs_public())==null?"":String.valueOf(rq.getIs_public()));
		map.put(rq.PROPERTY_CREATEDBY, rq.getEditedby()==null?"":rq.getEditedby());
		map.put(rq.PROPERTY_EDITEDTIME, rq.getEditedtime()==null?"":rq.getEditedtime().toString());
		map.put(rq.PROPERTY_FORWARD, rq.getForward()==null?"":rq.getForward());
		map.put(rq.PROPERTY_REQUEST_VERSION, String.valueOf(rq.getRequestVersion())==null?"":String.valueOf(rq.getRequestVersion()));
	}
	
	private void transferToRequestHistory(RequestHistory rh, Map<String, String> map) {
		map.put(rh.PROPERTY_HISTORYID,String.valueOf(rh.getHistoryid())==null?"":String.valueOf(rh.getHistoryid()));
		map.put(rh.PROPERTY_REQUESTID,String.valueOf(rh.getRequestid())==null?"":String.valueOf(rh.getRequestid()));
		map.put(rh.PROPERTY_EDITEDBY,rh.getEditedby()==null?"":rh.getEditedby());
		map.put(rh.PROPERTY_EDITEDTIME, rh.getEditedtime()==null?"":rh.getEditedtime().toString());
		map.put(rh.PROPERTY_STATUS, rh.getStatus()==null?"":rh.getStatus());
		map.put(rh.PROPERTY_COMMET, rh.getComment()==null?"":rh.getComment());
		map.put(rh.PROPERTY_USEREMAIL, rh.getUseremail()==null?"":rh.getUseremail());
		map.put(rh.PROPERTY_REQUEST_VERSION, String.valueOf(rh.getRequestVersion())==null?"":String.valueOf(rh.getRequestVersion()));
		map.put(rh.PROPERTY_IS_HISTORY, String.valueOf(rh.getIsHistory())==null?"":String.valueOf(rh.getIsHistory()));
	}
	public static void setMail(VaultSendMail tmailer) {
		mailer=tmailer;
	}
	
}
