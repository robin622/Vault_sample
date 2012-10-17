package com.rehat.tools.vault.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.jboss.logging.Logger;
import org.jdom.Document;
import org.jdom.Element;

import com.redhat.tools.vault.bean.Product;
import com.redhat.tools.vault.bean.ReplyComment;
import com.redhat.tools.vault.bean.Request;
import com.redhat.tools.vault.bean.RequestHistory;
import com.redhat.tools.vault.bean.Version;
import com.redhat.tools.vault.dao.CommentRelationshipDAO;
import com.redhat.tools.vault.dao.ProductDAO;
import com.redhat.tools.vault.dao.ReplyCommentDAO;
import com.redhat.tools.vault.dao.RequestDAO;
import com.redhat.tools.vault.dao.RequestHistoryDAO;
import com.redhat.tools.vault.dao.VersionDAO;
import com.redhat.tools.vault.service.ReportService;
import com.redhat.tools.vault.util.DateUtil;

public class ReportServiceImpl implements ReportService {
	private static final Logger log = Logger.getLogger(ReportServiceImpl.class);
	@Inject
	private RequestHistoryDAO historyDAO;
	@Inject 
	private VersionDAO versionDAO;
	@Inject
	private RequestDAO requestDAO;
	@Inject
	private ProductDAO productDAO;
	@Inject
	private VaultSendMail mailer;
	
	public Object[] report(String requestid) {
		Object[] args=new Object[8];
		String owner = null;
		String[] ownerArr = null;
		Request request = new Request();
		RequestHistory history = new RequestHistory();
		Version version = new Version();
		Product product = new Product();
		List<Request> requests = null;
		List<Version> versions = null;
		List<Product> products = null;
		Request pRequest = null;
		List<Request> cRequests = new ArrayList<Request>();
		request.setRequestid(Long.parseLong(requestid));
		history.setRequestid(Long.parseLong(requestid));
		requests = requestDAO.get(request);
		List<String> owners = new ArrayList<String>();
		RequestHistory addHistory = null;
		Date lastEditedtime = null;
		List<List<RequestHistory>> reports = new ArrayList<List<RequestHistory>>();
		Map<Long, List<ReplyComment>> replys = new HashMap<Long, List<ReplyComment>>();
		String status = null;
		if (requests != null && requests.size() > 0) {
			requests.get(0).setRequestname(
					StringEscapeUtils.escapeHtml(requests.get(0)
							.getRequestname()));
			owner = requests.get(0).getOwner();
			ownerArr = owner.split(",");
			if (ownerArr != null) {
				for (int i = 0; i < ownerArr.length; i++) {
					if (!owners.contains(ownerArr[i])) {
						owners.add(ownerArr[i]);
					}
				}
			}
			if (requests.get(0).getVersionid() == -1) {
				requests.get(0).setProductname("Unspecified");
				requests.get(0).setVersiondesc("Unspecified");
			}
			else {
				version.setId(requests.get(0).getVersionid());
				try {
					versions = versionDAO.get(version);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (versions != null && versions.size() > 0) {
					requests.get(0).setVersiondesc(
							versions.get(0).getValue());
					product.setId(versions.get(0).getProduct_id());
					try {
						products = productDAO.get(product);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (products != null && products.size() > 0) {
						requests.get(0).setProductname(
								products.get(0).getName());
					}
				}
			}

			reports = historyDAO.getReports(owners, requests.get(0)
					.getRequestid());
			
			

			// historys = historyDAO.get(history);
			if (reports != null && reports.size() > 0) {
				CommentRelationshipDAO commentRelationDAO = new CommentRelationshipDAO();
				ReplyCommentDAO replyCommentDAO = new ReplyCommentDAO();
				for (int i = 0; i < reports.size(); i++) {
					List<RequestHistory> temps = reports.get(i);
					if (temps != null && temps.size() > 0) {
						//replys map
						for(RequestHistory r : temps){
							Long historyid = r.getHistoryid();
							List<Long> replyIdList=null;
							try {
								replyIdList = commentRelationDAO.getReplyIdListByHistoryId(historyid);
							} catch (Exception e) {
								e.printStackTrace();
							}
							List<ReplyComment> replyList=null;
							try {
								replyList = replyCommentDAO.getReplyCommentListByIdList(replyIdList);
							} catch (Exception e) {
								e.printStackTrace();
							}	
							replys.put(r.getHistoryid(), replyList);
						}
						boolean judgeSign = false;
						boolean flag = false;
						for (RequestHistory temp : temps) {
							if (Request.SIGNED.equals(temp.getStatus())
									&& !temp.getIsHistory()) {
								temps.get(0).setReportStatus(
										Request.SIGNED);
								judgeSign = true;
							}
							if (Request.SIGNED_BY.equals(temp
									.getStatus())
									&& !temp.getIsHistory()) {
								temps.get(0).setReportStatus(
										Request.SIGNED_BY);
								judgeSign = true;
							}

							// if((Request.SIGNED.equals(temp.getStatus())
							// ||
							// Request.SIGNED_BY.equals(temp.getStatus()))
							// && temp.getIsHistory()){
							if (Request.SIGNED.equals(temp.getStatus())
									&& temp.getIsHistory()) {
								flag = true;
							}
						}
						if (!judgeSign) {
							if (flag) {
								temps.get(0).setReportStatus(
										Request.WAITING);
							}
							else {
								temps.get(0).setReportStatus(
										temps.get(0).getStatus());
							}
						}
					}
				}

				Request rq = requests.get(0);
				String[] parentAndChildren = null;
				try {
					parentAndChildren = requestDAO
							.generateParent(rq);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// get parent
				if (!"".equals(parentAndChildren[0])) {
					String parentId = parentAndChildren[0].split("##")[1]
							.split("  ")[0];
					pRequest = requestDAO
							.find(Long.parseLong(parentId));
					if (pRequest != null)
						try {
							pRequest = getDetailedStatus(pRequest,
									historyDAO);
						} catch (Exception e) {
							e.printStackTrace();
						}
				}

				// get children
				if (!"".equals(parentAndChildren[1])) {
					String[] array = parentAndChildren[1].split(",");
					if (array != null && array.length > 0) {
						for (int i = 0; i < array.length; i++) {
							String childId = array[i].split("##")[1]
									.split("  ")[0];
							Request cRequest = requestDAO.find(Long
									.parseLong(childId));
							if (cRequest != null) {
								try {
									cRequest = getDetailedStatus(cRequest,
											historyDAO);
								} catch (Exception e) {
									e.printStackTrace();
								}
								cRequests.add(cRequest);
							}
						}
					}
				}
			}
		}
		args[0]=status;
		args[1]=reports;
		args[2]=replys;
		args[3]=owners;
		args[4]=lastEditedtime;
		args[5]=requests;
		args[6]=pRequest;
		args[7]=cRequests;
		return args;
	}
		
		private Request getDetailedStatus(Request r, RequestHistoryDAO histDao)
				throws Exception {
			// RequestHistoryDAO histDao = new RequestHistoryDAO();
			String waiting = "";
			String comment = "";
			String reject = "";
			String signoff = "";
			boolean isSignOnBehalf = false;
			if ("".equals(r.getOwner()) || r.getOwner() == null)
				return r;
			String[] signatories = r.getOwner().split(",");
			List<String> signatoryList = Arrays.asList(signatories);
			for (int i = 0; i < signatories.length; i++) {
				RequestHistory hist = new RequestHistory();
				hist.setRequestid(r.getRequestid());
				hist.setUseremail(signatories[i]);
				hist.setIsHistory(false);
				List<RequestHistory> hists = histDao.get(hist, false);
				if (hists != null && hists.size() > 0) {
					boolean isSignedOrRejected = false;
					for (RequestHistory h : hists) {
						if (h.getStatus().equals(Request.SIGNED)
								|| h.getStatus().equals(Request.SIGNED_BY)) {
							signoff += signatories[i] + ",";
							isSignedOrRejected = true;
							break;
						}
						else if (h.getStatus().equals(Request.REJECTED)) {
							reject += signatories[i] + ",";
							isSignedOrRejected = true;
							break;
						}
					}
					if (!isSignedOrRejected) {
						comment += signatories[i] + ",";
					}

				}
				else {
					waiting += signatories[i] + ",";
				}
			}

			RequestHistory hist = new RequestHistory();
			hist.setRequestid(r.getRequestid());
			hist.setIsHistory(false);
			List<RequestHistory> hists = histDao.get(hist, true);
			if (hists != null && hists.size() > 0) {
				for (RequestHistory h : hists) {
					if (!signatoryList.contains(h.getUseremail())
							&& h.getStatus().equals(Request.COMMENTS)) {
						comment += h.getUseremail() + ",";
					}
					if (h.getEditedby().equals(r.getCreatedby())
							&& h.getStatus().equals(Request.SIGNED_ONBEHALF)) {
						isSignOnBehalf = true;
					}
				}
			}

			if (waiting.length() > 0) {
				waiting = waiting.substring(0, waiting.lastIndexOf(","));
			}
			if (comment.length() > 0) {
				comment = comment.substring(0, comment.lastIndexOf(","));
			}
			if (reject.length() > 0) {
				reject = reject.substring(0, reject.lastIndexOf(","));
			}
			if (signoff.length() > 0) {
				signoff = signoff.substring(0, signoff.lastIndexOf(","));
			}
			r.setWaitingList(waiting);
			r.setCommentList(comment);
			r.setSignoffList(signoff);
			r.setRejectedList(reject);
			r.setIsSignOnBehalf(isSignOnBehalf);
			return r;
		}

		public Document doExport(List<Request> requests,HttpServletRequest request, Object objs,Object objs1,Object objs2,Object objs3) {
			List<List<RequestHistory>> reports = new ArrayList<List<RequestHistory>>();
			Map<Long, List<ReplyComment>> replys = new HashMap<Long, List<ReplyComment>>();
			Request pRequest = null;
			List<Request> cRequests = new ArrayList<Request>();
			reports=(List<List<RequestHistory>>)objs;
			replys=(Map<Long, List<ReplyComment>>)objs2;
			pRequest=(Request)objs2;
			cRequests=(List<Request>)objs3;
			requests.get(0).setRequestname(
					StringEscapeUtils.unescapeHtml(requests.get(0)
							.getRequestname()));
			String element_createDate = DateUtil
					.toString(requests.get(0).getCreatedtime(),
							"yyyy-MM-dd HH:mm");
			String element_dueDate = DateUtil.toString(requests
					.get(0).getRequesttime(), "yyyy-MM-dd HH:mm");
			String path = "http://" + request.getServerName()
					+ "/Vault";
			//String element_attachment = Attachment.exportFileInfo(
			//		requests.get(0), path).toString();
			String element_attachment = "";
			if (element_attachment == null
					|| element_attachment.equals(""))
				element_attachment = "Unspecified";
			String element_cc = requests.get(0).getForward();
			if (element_cc == null || element_cc.equals(""))
				element_cc = "Unspecified";
			// ------------root------------//
			Element e_root = new Element("report");
			Document doc = new Document(e_root);
			// -----child(current request)-----//
			Element e_request = new Element("request");
			e_root.addContent(e_request);
			e_root.addContent("** All the time is UTC Time **");
			e_request.addContent(new Element("requestId").setText(Long.toString(requests.get(0).getRequestid())));
			e_request.addContent(new Element("requestName").setText(requests.get(0).getRequestname()));
			e_request.addContent(new Element("product").setText(requests.get(0).getProductname()));
			e_request.addContent(new Element("version").setText(requests.get(0).getVersiondesc()));
			e_request.addContent(new Element("createDate").setText(element_createDate));
			e_request.addContent(new Element("dueDate").setText(element_dueDate));
			e_request.addContent(new Element("detailedDescription")
				.setText(replaceStr("~~<br>"+ requests.get(0).getDetail()+ "<br>~~")));
			e_request.addContent(new Element("attachment").setText(element_attachment));
			e_request.addContent(new Element("cc").setText(element_cc));
			if (reports != null && reports.size() > 0) {
				for (int i = 0; i < reports.size(); i++) {
					Element e_signComment = new Element("signComment" + i);
					e_request.addContent(e_signComment);
					List<RequestHistory> temps = reports.get(i);
					e_signComment.setAttribute("signOffBy", reports.get(i).get(0).getUseremail());
					e_signComment.addContent(new Element("dueDate").setText(element_dueDate));
					if (reports.get(i).get(0).getEditedtime() != null) {
						String element_editedTime = DateUtil.toString(reports.get(i).get(0).getEditedtime(), "yyyy-MM-dd HH:mm");
						e_signComment.addContent(new Element("lastModified").setText(element_editedTime));
					}
					else {
						e_signComment.addContent(new Element("lastModified").setText("Unspecified"));
					}
					if (reports.get(i).get(0).getReportStatus() != null) {
						e_signComment.addContent(new Element("status").setText(reports.get(i).get(0).getReportStatus()));
					}
					else {
						e_signComment.addContent(new Element("status").setText("Unspecified"));
					}
					if (temps != null && temps.size() > 0) {
						for (int j = 0; j < temps.size(); j++) {
							if (temps.get(j).getEditedtime() != null
									&& !Request.SIGNED_BY.equals(temps.get(j).getStatus())) {
								Element e_comment = new Element("comment_"+i+"_"+j);
								e_signComment.addContent(e_comment);
								e_comment.addContent(new Element("content").setText(replaceStr(temps.get(j).getComment())));
								String element_Information = DateUtil.toString(temps.get(j).getEditedtime(), "yyyy-MM-dd HH:mm")+ " "+ temps.get(j).getEditedby()+ " "+ temps.get(j).getStatus();
								e_comment.addContent(new Element("information").setText(element_Information));
								//reply
								if (replys!=null){
									List<ReplyComment> replyList = replys.get(temps.get(j).getHistoryid());
									if(replyList != null && !replyList.equals("")){
										Map<String, Element> replyElementMap = new HashMap<String, Element>();
										for(ReplyComment r : replyList){
											if(r.getBaseid() == -1){
												Element e_reply = new Element("reply_" + r.getReplyid());
												e_comment.addContent(e_reply);
												addReplyElementContent(r, e_reply, replyElementMap);
											}else{
												Element e_reply = new Element("reply_" + r.getReplyid());
												Element p_e_reply = replyElementMap.get("reply_"+r.getBaseid());
												p_e_reply.addContent(e_reply);
												addReplyElementContent(r, e_reply, replyElementMap);
											}
										}
									}
								}
							}
						}
					}
				}
			}

			// -----child(parent request)-----//
			if (pRequest != null) {
				Element e_parent = new Element("parentRequest");
				e_root.addContent(e_parent);
				Long element_p_requestId = pRequest.getRequestid();
				e_parent.addContent(new Element("requestId")
						.setText(Long.toString(element_p_requestId)));
				e_parent.addContent(new Element("requestName")
						.setText(pRequest.getRequestname()));
				String element_p_status = pRequest.getStatus()
						.equals(Request.INACTIVE) ? "Approved"
						: "In progress";
				e_parent.addContent(new Element("requestStatus")
						.setText(element_p_status));
				if (pRequest.getWaitingList() != null
						&& !"".equals(pRequest.getWaitingList())) {
					e_parent.addContent(new Element("waiting")
							.setText(pRequest.getWaitingList()));
				}
				if (pRequest.getCommentList() != null
						&& !"".equals(pRequest.getCommentList())) {
					e_parent.addContent(new Element("comment")
							.setText(pRequest.getCommentList()));
				}
				if (pRequest.getRejectedList() != null
						&& !"".equals(pRequest.getRejectedList())) {
					e_parent.addContent(new Element("reject")
							.setText(pRequest.getRejectedList()));
				}
				if (pRequest.getSignoffList() != null
						&& !"".equals(pRequest.getSignoffList())) {
					e_parent.addContent(new Element("signOff")
							.setText(pRequest.getSignoffList()));
				}
			}

			// -----child(child request)-----//
			if (cRequests != null && cRequests.size() > 0) {
				for (int i = 0; i < cRequests.size(); i++) {
					Element e_child = new Element("childRequest"
							+ i);
					e_root.addContent(e_child);
					Long element_c_requestId = cRequests.get(i)
							.getRequestid();
					e_child.addContent(new Element("requestId")
							.setText(Long
									.toString(element_c_requestId)));
					e_child.addContent(new Element("requestName")
							.setText(cRequests.get(i)
									.getRequestname()));
					String element_c_status = cRequests.get(i)
							.getStatus().equals(Request.INACTIVE) ? "Approved"
							: "In progress";
					e_child.addContent(new Element("requestStatus")
							.setText(element_c_status));
					if (cRequests.get(i).getWaitingList() != null
							&& !"".equals(cRequests.get(i)
									.getWaitingList())) {
						e_child.addContent(new Element("waiting")
								.setText(cRequests.get(i)
										.getWaitingList()));
					}
					if (cRequests.get(i).getCommentList() != null
							&& !"".equals(cRequests.get(i)
									.getCommentList())) {
						e_child.addContent(new Element("comment")
								.setText(cRequests.get(i)
										.getCommentList()));
					}
					if (cRequests.get(i).getRejectedList() != null
							&& !"".equals(cRequests.get(i)
									.getRejectedList())) {
						e_child.addContent(new Element("reject")
								.setText(cRequests.get(i)
										.getRejectedList()));
					}
					if (cRequests.get(i).getSignoffList() != null
							&& !"".equals(cRequests.get(i)
									.getSignoffList())) {
						e_child.addContent(new Element("signOff")
								.setText(cRequests.get(i)
										.getSignoffList()));
					}
				}
			}
			return doc;
		}
		
		private String replaceStr(String str) {
			str = str.replaceAll("&amp;amp;", "&");
			str = str.replaceAll("&amp;", "&");
			str = str.replaceAll("&lt;", "<");
			str = str.replaceAll("&gt;", ">");
			str = str.replaceAll("&quot;", "\"");
			str = str.replaceAll("&nbsp;", " ");
			str = str.replaceAll("&copy;", "©");
			str = str.replaceAll("<br>", "\n");
			return str;
		}
		private void addReplyElementContent(ReplyComment r, Element e_reply, Map<String, Element> replyElementMap){
			e_reply.addContent(new Element("replyContent").setText(replaceStr(r.getReplycomment())));
			String element_replyInformation = r.getEditedtime()+ " "+ r.getEditedby()+ " Reply";
			e_reply.addContent(new Element("replyinformation").setText(element_replyInformation));
			replyElementMap.put("reply_" + r.getReplyid(), e_reply);
		}

}
