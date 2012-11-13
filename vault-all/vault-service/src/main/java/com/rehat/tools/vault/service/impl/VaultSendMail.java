package com.rehat.tools.vault.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;

import com.redhat.tools.vault.bean.MGProperties;
import com.redhat.tools.vault.bean.ReplyComment;
import com.redhat.tools.vault.bean.Request;
import com.redhat.tools.vault.bean.RequestHistory;
import com.redhat.tools.vault.bean.SendemailCount;
import com.redhat.tools.vault.bean.VAUser;
import com.redhat.tools.vault.dao.CommentRelationshipDAO;
import com.redhat.tools.vault.dao.ReplyCommentDAO;
import com.redhat.tools.vault.dao.RequestDAO;
import com.redhat.tools.vault.dao.RequestHistoryDAO;
import com.redhat.tools.vault.dao.SendemailCountDAO;
import com.redhat.tools.vault.dao.VAUserDAO;
import com.redhat.tools.vault.mail.Mail;
import com.redhat.tools.vault.util.DateUtil;
import com.redhat.tools.vault.util.StringUtil;

public class VaultSendMail {
	@Inject
	private RequestDAO requestDAO;
	@Inject
	private VAUserDAO userDAO;
	@Inject
	private SendemailCountDAO countDAO;
	@Inject
	private RequestHistoryDAO historyDao;
	@Inject
	private CommentRelationshipDAO  relationDAO;

	/** The logger. */
	protected static final Logger log = Logger.getLogger(VaultSendMail.class);

	/** PropertyManager */
	MGProperties prop = MGProperties.getInstance();
	
	/**email**/
	String email="vault-dev-list@redhat.com";

	List<SendemailCount> countlist = new ArrayList<SendemailCount>();

	SendemailCount emailCount = new SendemailCount();

	DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	DateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	String userName = null;

	String serverName = null;

	Integer serverPort = null;

	String URL = null;

	String nextLine = "\r\n";

	public String getSubject(Request bean, String status) {
		String subject = null;
		if ("new".equals(status)) {
			subject = "[Vault-" + bean.getRequestid() + "] [New] " + '"'
					+ bean.getRequestname() + '"';
		}
		else if ("reject".equals(status)) {
			subject = "[Vault-" + bean.getRequestid() + "] [Rejected] " + '"'
					+ bean.getRequestname() + '"';
		}
		else if ("signoff".equals(status)) {
			subject = "[Vault-" + bean.getRequestid() + "] [Approved] " + '"'
					+ bean.getRequestname() + '"';
		}
		else if ("signoffOnBehalf".equals(status)) {
			subject = "[Vault-" + bean.getRequestid()
					+ "] [Approved On Behalf] " + '"' + bean.getRequestname()
					+ '"';
		}
		else if ("change".equals(status)) {
			subject = "[Vault-" + bean.getRequestid() + "] [Changed] " + '"'
					+ bean.getRequestname() + '"';
			// sb.append("This message has been automatically generated in response to the sign of a sign off request regarding:<p>");
		}
		else if ("comment".equals(status)) {
			subject = "[Vault-" + bean.getRequestid() + "] [Commented] " + '"'
					+ bean.getRequestname() + '"';
		}
		else if("reply".equals(status)){
			subject = "[Vault-" + bean.getRequestid() + "] [Replied] " + '"'
			+ bean.getRequestname() + '"';
		}
		else if ("delete".equals(status)) {
			subject = "[Vault-" + bean.getRequestid() + "] [Deleted] " + '"'
					+ bean.getRequestname() + '"';
		}
		else if ("withdraw".equals(status)) {
			subject = "[Vault-" + bean.getRequestid() + "] [Withdrawn] " + '"'
					+ bean.getRequestname() + '"';
		}
		else if ("dueDate".equals(status)) {
			subject = "[Vault-" + bean.getRequestid() + "] [DueDate] " + '"'
					+ bean.getRequestname() + '"';
		}
		return subject;
	}

	public String getCreatorEmail(Request bean) throws Exception {
		String email = null;
		if (bean.getCreatedby() != null) {
			VAUser vauser = new VAUser();
			List<VAUser> users = null;
			vauser.setUsername(bean.getCreatedby());
			users = userDAO.get(vauser);
			if (users != null && users.size() > 0) {
				email = users.get(0).getUseremail();
			}
			else {
				vauser.setUseremail(bean.getCreatedby() + "@redhat.com");
				vauser.setCreatedtime(DateUtil.getLocalUTCTime());
				userDAO.save(vauser);
				users = userDAO.get(vauser);
				email = users.get(0).getUseremail();
			}
		}
		return email;
	}

	public void setServerInfo(Object aRequest, String actionURL) {
		if (aRequest != null) {
			if(aRequest instanceof HttpServletRequest){
				serverName = ((HttpServletRequest)aRequest).getServerName();
				serverPort = ((HttpServletRequest)aRequest).getServerPort();
			}
			URL = actionURL;
		}
		else {
			// if(MGProperties.USER_NAME != null){
			// userName = MGProperties.USER_NAME;
			// }
			if (MGProperties.NAME_SERVER != null) {
				serverName = MGProperties.NAME_SERVER;
			}
			if (MGProperties.PORT_SERVER != null) {
				serverPort = MGProperties.PORT_SERVER;
			}
			if (MGProperties.URL_ACTION != null) {
				URL = MGProperties.URL_ACTION;
			}
		}
		log.debug("username=" + userName);
		log.debug("ServerName=" + serverName);
		log.debug("ServerPort=" + serverPort);
		if (URL == null) {
			log.debug("Email is not sent");
		}
		else if (URL.length() != 0 && URL.indexOf(";jsessionid") != -1) {
			URL = URL.replaceAll(";jsessionid=\\w*\\?", "?");
		}

	}

	/**
	 * 
	 * @param aRequest
	 * @param bean
	 * @param reply
	 * @param actionURL
	 * @param operation
	 * @param status
	 * @param compare
	 * @throws Exception
	 */
	public void sendEmail(Object aRequest, Request bean,ReplyComment reply,
			String actionURL, String operation, String status, String compare)
			throws Exception {
		final String METHOD = "sendEmail";
		log.debug(METHOD + ": start");
		log.debug(bean);
		userName = null;
		serverName = null;
		serverPort = null;
		URL = null;

		userName = bean.getCreatedby();
		setServerInfo(aRequest, actionURL);
		StringBuffer sb = new StringBuffer("");
		List<Request> requests = new ArrayList<Request>();

		Request request = new Request();
		request.setRequestid(bean.getRequestid());
		String fromUserAddress = null;
		String fromUserName = null;
		String toUserAddress = "";
		String ccUserAddress = "";
		Long order = null;
		String url = "";
		String urlRpt = "";

		requests = requestDAO.get(request);
		String subject = getSubject(bean, status);

		/**/
		// if(serverName.indexOf("vault-devel") > -1){
		// sb.append("The email originated from the testing instance and is part of the development process.");
		// addLine(sb);
		// }

		addLine(sb);
		sb.append("Greetings,");
		addLine(sb);
		sb.append("(**all time below is UTC time**)");
		addLine(sb);
		// sb.append("Greetings,<p><p>");

		addLine(sb);
		if ("new".equals(status)) {
			// sb.append(
			// "This message has been automatically generated in response to the creation of a request regarding:<p>");
			// sb.append(
			// "This message has been automatically generated in response to the creation of a request regarding:");
			sb.append("Request \""
					+ bean.getRequestname()
					+ "\" has been created in Vault and requires your sign-off by "
					+ dayFormat.format(bean.getRequesttime()));
			addLine(sb);
		}
		else if ("reject".equals(status)) {
			sb.append("Your Vault request \"" + bean.getRequestname()
					+ "\" has been rejected by " + bean.getSignedby()
					+ "@redhat.com at " + format.format(bean.getSignedtime())
					+ ".");
			addLine(sb);
		}
		else if ("signoff".equals(status) || "signoffOnBehalf".equals(status)) {

		}
		else if ("change".equals(status)) {
			// sb.append(
			// "This message has been automatically generated in response to the change of Vault request \""+bean.getRequestid()+"  "+bean.getRequestname()+"\",");
			// sb.append(
			// "This message has been automatically generated in response to the change of Vault request \""+bean.getRequestid()+"  "+bean.getRequestname()+"\",");

			// addLine(sb);
		}
		else if ("delete".equals(status)) {
			// sb.append("This message has been automatically generated in response to the deletion of a request:<p>");
			// sb.append("This message has been automatically generated in response to the deletion of a request:<p>");
			sb.append("Request \"" + bean.getRequestname()
					+ "\" required your sign-off has been deleted.");
			addLine(sb);
		}
		else if ("withdraw".equals(status)) {
			// sb.append("This message has been automatically generated in response to the deletion of a request:<p>");
			// sb.append("This message has been automatically generated in response to the deletion of a request:<p>");
			sb.append("Request \"" + bean.getRequestname()
					+ "\" required your sign-off has been withdrawn.");
			addLine(sb);
		}
		else if ("dueDate".equals(status)) {
			// sb.append("This message has been automatically generated in response to the  maturity of a request:<p>");
			// sb.append("This message has been automatically generated in response to the  maturity of a request:");
			// sb.append("Request \""+bean.getRequestname()+"\" required your sign_off will be expired today.");
			sb.append("Request \"" + bean.getRequestname()
					+ "\" required your sign_off will be expired on "
					+ dayFormat.format(bean.getRequesttime()) + ".");
			addLine(sb);
		}

		if (serverName != null && serverPort != null && URL != null) {
			// url = "https://"
			// + serverName
			// + URL
			// + "&"
			// + (operation.equals("") ? ""
			// : ("operation=" + operation + "&")) + "requestid="
			// + bean.getRequestid();
			// url =
			// "http://localhost:8080/Vault/showRequest/"+bean.getRequestid();
			url = "https://" + serverName + "/showRequest/"
					+ bean.getRequestid();
			// url = "<a href='"+url+"'><strong>View This Request</strong></a>";
			/*
			 * if("new".equals(status) || "reject".equals(status) ){
			 * 
			 * }else if("signoff".equals(status)){ url = "https://" + serverName
			 * + URL + "&" + "operation=" + operation + "&" +
			 * "requestid="+bean.getRequestid(); }
			 */
		}

		fromUserName = "Vault";
		fromUserAddress = userName+"@redhat.com";
		if (!"signoffOnBehalf".equals(status) && !"signoff".equals(status)
				&& !"reject".equals(status) && !"delete".equals(status)
				&& !"withdraw".equals(status)) {
			if (userName != null) {
				VAUser vauser = new VAUser();
				List<VAUser> users = null;
				vauser.setUsername(userName);
				users = userDAO.get(vauser);
				if (users != null && users.size() > 0) {
					ccUserAddress = users.get(0).getUseremail();
				}
				else {
					vauser.setUseremail(userName + "@redhat.com");
					vauser.setCreatedtime(DateUtil.getLocalUTCTime());
					userDAO.save(vauser);
					users = userDAO.get(vauser);
					ccUserAddress = users.get(0).getUseremail();
				}
			}
		}

		if (bean.getForward() != null && !"".equals(bean.getForward())) {
			String forward = StringUtil.deFormartCCList(bean.getForward());
			ccUserAddress += "," + forward;
		}

		if ("signoff".equals(status) || "signoffOnBehalf".equals(status)) {
			toUserAddress = getCreatorEmail(bean);
			if ("signoffOnBehalf".equals(status)) {
				// ccUserAddress += ","+bean.getOwner();
				RequestHistory hist = new RequestHistory();
				hist.setRequestid(bean.getRequestid());
				hist.setIsHistory(false);
				List<RequestHistory> hists = historyDao.get(hist, false);
				if (hists != null && hists.size() > 0) {
					for (RequestHistory history : hists) {
						if (history.getStatus().equals(Request.SIGNED_BY)) {
							ccUserAddress += "," + history.getEditedby()
									+ "@redhat.com";
						}
					}
				}
			}
			else {
				if (bean.getSignedby() != null) {
					VAUser vauser = new VAUser();
					List<VAUser> users = null;
					vauser.setUsername(bean.getSignedby());
					users = userDAO.get(vauser);
					if (users != null && users.size() > 0) {
						ccUserAddress += "," + users.get(0).getUseremail();
					}
					else {
						vauser.setUseremail(bean.getSignedby() + "@redhat.com");
						vauser.setCreatedtime(DateUtil.getLocalUTCTime());
						userDAO.save(vauser);
						users = userDAO.get(vauser);
						ccUserAddress += "," + users.get(0).getUseremail();
					}

				}
			}

			// sb.append( "Request name: "+bean.getRequestname()+"<p><p>";
			if ("signoffOnBehalf".equals(status)) {
				// sb.append(
				// "Your Vault request \""+bean.getRequestid()+"  "+bean.getRequestname()+"\" has been signed off by "+bean.getSignedby()+"@redhat.com on behalf of the target signatories at "+format.format(bean.getSignedtime())+".<p><p>");
				// sb.append(
				// "Your Vault request \""+bean.getRequestid()+"  "+bean.getRequestname()+"\" has been signed off by "+bean.getSignedby()+"@redhat.com on behalf of the target signatories at "+format.format(bean.getSignedtime())+".");
				// addLine(sb);
				// //sb.append(
				// "Your Vault request \""+bean.getRequestid()+"  "+bean.getRequestname()+"\" has been signed off by "+bean.getSignedby()+"@redhat.com at "+format.format(bean.getSignedtime())+".<p><p>");
				// sb.append(
				// "Your Vault request \""+bean.getRequestid()+"  "+bean.getRequestname()+"\" has been signed off by "+bean.getSignedby()+"@redhat.com at "+format.format(bean.getSignedtime())+".");
				// addLine(sb);
				sb.append("Your request \"" + bean.getRequestname()
						+ "\" has been signed off on behalf by "
						+ bean.getSignedby() + "@redhat.com at "
						+ format.format(bean.getSignedtime()) + ".");
				addLine(sb);
			}
			else {
				sb.append("Your request \"" + bean.getRequestname()
						+ "\" has been signed off by " + bean.getSignedby()
						+ "@redhat.com at "
						+ format.format(bean.getSignedtime()) + ".");
				addLine(sb);
			}

			// sb.append( "Comment: <p>" + bean.getComment() + "<p><p>");
			// sb.append(
			// "You can find the request detailed description:<p><p>");
			// sb.append( "<ul><li>Created by: " + bean.getCreatedby() +
			// "</li>");
			// sb.append( "<li>Created: " + format.format(bean.getCreatedtime())
			// + "</li>");
			// if(bean.getRequesttime() != null){
			// sb.append( "<li>Due date: " +
			// dayFormat.format(bean.getRequesttime()) + "</li>");
			// }else{
			// sb.append( "<li>Due date: " + dayFormat.format(new Date()) +
			// "</li>");
			// }
			// sb.append( "<li>Sign off by: " + bean.getSignedby() + "</li>";

			// sb.append( "<li>Sign off date: " +
			// format.format(bean.getSignedtime()) + "</li>";

			// String detail = bean.getDetail();
			//
			// detail = StringUtil.showInEmail(detail);
			//
			// detail = StringUtil.convertToHref(detail);
			//
			// sb.append( "<li>Detailed description: <p><p>" + detail +
			// "</li></ul><p><p>");
			// addDetailLink(sb,url);
			// addRegards(sb);
			showComment(sb, bean);
			// addPlainDetailLink(sb,url);
			// addPlainDetail(sb,bean);
			addPlainDetails(sb, bean, url);
			addPlainQuestions(sb);
			addPlainRegards(sb);
			if (!"".equals(url)) {
				try {
					Mail.sendMail(prop.getSMTPAddress(), fromUserAddress,
							fromUserName, toUserAddress, ccUserAddress,
							subject, sb.toString());
				}
				catch (Exception e) {
					log.error(e.getMessage());
					throw e;
				}
			}
		}
		else if ("comment".equals(status)) {
			// sb.append(
			// "Request \""+bean.getRequestid()+"  "+bean.getRequestname()+
			// "\" requiring your sign off has been commented on the Vault portal.<p><p>");
			// sb.append(
			// "Request \""+bean.getRequestid()+"  "+bean.getRequestname()+
			// "\" requiring your sign off has been commented on the Vault portal.");
			sb.append("Request \"" + bean.getRequestname()
					+ "\" has been commented.");
			addLine(sb);
			// sb.append(
			// "Commented by "+bean.getEditedby()+"@redhat.com ["+format.format(bean.getCreatedtime())+"]: <p><p>"
			// + bean.getComment() + "<p><p>");
			toUserAddress += getOwner(bean);
			// showComment(sb,bean);
			sb.append("Commented by " + bean.getEditedby() + "@redhat.com ["
					+ format.format(bean.getCreatedtime()) + "]:");
			addLine(sb);
			String comm=(bean.getComment()==null?"":bean.getComment());
			sb.append(StringUtil.showInEmail(comm));
			addLine(sb);
			// addPlainDetailLink(sb,url);
			// addPlainDetail(sb,bean);
			addPlainDetails(sb, bean, url);
			addPlainQuestions(sb);
			addPlainRegards(sb);
			// sb.append(
			// "You can find the request detailed description:<p><p>");
			// sb.append( "<ul><li>Created by: " + bean.getCreatedby() +
			// "</li>");
			// sb.append( "<li>Created: " + format.format(bean.getCreatedtime())
			// + "</li>");
			// if(bean.getRequesttime() != null){
			// sb.append( "<li>Due date: " +
			// dayFormat.format(bean.getRequesttime()) + "</li>");
			// }else{
			// sb.append( "<li>Due date: " + dayFormat.format(new Date()) +
			// "</li>");
			// }
			// String detail = bean.getDetail();
			//
			// detail = StringUtil.showInEmail(detail);
			// detail = StringUtil.convertToHref(detail);
			//
			// sb.append( "<li>Detailed description: <p>" + detail +
			// "<p><p><p></li></ul><p><p>");
			// //sb.append( "<li>Comment by "+bean.getEditedby()+": <p><p>" +
			// bean.getComment() + "<p></li></ul><p><p>";
			// addDetailLink(sb,url);
			// addRegards(sb);
			if (!"".equals(url)) {
				try {
					Mail.sendMail(prop.getSMTPAddress(), fromUserAddress,
							fromUserName, toUserAddress, ccUserAddress,
							subject, sb.toString());
				}
				catch (Exception e) {
					log.error(e.getMessage());
					throw e;
				}
				emailCount.setRequestid(bean.getRequestid());
				countlist = countDAO.get(emailCount);
				if (countlist != null && countlist.size() > 0) {
					countlist.get(0).setCount(Long.parseLong("1"));
					countDAO.update(countlist.get(0));
				}
			}
		}
		else if("reply".equals(status)){
			try{
				Long historyid = relationDAO.getHistoryidByreplyId(reply.getReplyid());
				RequestHistory history = historyDao.getRequestHistoryByid(historyid);
				sb.append("Request \"" + bean.getRequestname()
						+ "\" 's comment("+history.getStatus()+") has a new Reply.");
				addLine(sb);
				addLine(sb);
				sb.append("This comment is:");
				addLine(sb);
				sb.append(StringUtil.showInEmail(history.getComment()));
			}
			catch (Exception e) {
				log.error(e.getMessage());
				throw e;
			}
			addLine(sb);
			addLine(sb);
			sb.append("Replied by " + reply.getEditedby() + "@redhat.com ["
					+ reply.getEditedtime() + "]:");
			addLine(sb);
			sb.append(StringUtil.showInEmail(StringUtil.showInEmail(reply.getReplycomment())));
			addLine(sb);
			addPlainDetails(sb, bean, url);
			addPlainQuestions(sb);
			addPlainRegards(sb);
			toUserAddress += getOwner(bean);
			if (!"".equals(url)) {
				try {
					Mail.sendMail(prop.getSMTPAddress(), fromUserAddress,
							fromUserName, toUserAddress, ccUserAddress,
							subject, sb.toString());
				}
				catch (Exception e) {
					log.error(e.getMessage());
					throw e;
				}
				emailCount.setRequestid(bean.getRequestid());
				countlist = countDAO.get(emailCount);
				if (countlist != null && countlist.size() > 0) {
					countlist.get(0).setCount(Long.parseLong("1"));
					countDAO.update(countlist.get(0));
				}
			}
		}
		
		else if ("new".equals(status)) {
			log.debug(request);
			// sb.append( "Request name: "+bean.getRequestname()+"<p><p>");
			// sb.append(
			// "A new request requiring your sign off has been created on the Vault portal<p><p>");
			toUserAddress += getOwner(bean);
			// sb.append( "These are the detailed description:<p><p>");
			// sb.append( "<ul><li>Created by: " + bean.getCreatedby() +
			// "</li>");
			// sb.append( "<li>Created: " + format.format(bean.getCreatedtime())
			// + "</li>");
			// if(bean.getRequesttime() != null){
			// sb.append( "<li>Due date: " +
			// dayFormat.format(bean.getRequesttime()) + "</li>");
			// }else{
			// sb.append( "<li>Due date: " + dayFormat.format(new Date()) +
			// "</li>");
			// }
			// String detail = bean.getDetail();
			//
			// detail = StringUtil.showInEmail(detail);
			//
			// detail = StringUtil.convertToHref(detail);
			//
			// sb.append( "<li>Detailed description: <p><p>" + detail +
			// "<p></li></ul><p><p>");
			// addDetailLink(sb,url);
			// sb.append( "Request name: "+bean.getRequestname()+"");
			// addLine(sb);
			// sb.append(
			// "A new request requiring your sign off has been created on the Vault portal");
			// addLine(sb);
			// addPlainDetailLink(sb,url);
			// addPlainDetail(sb,bean);
			// sb.append(
			// "To learn more about the Vault Sign Off Process follow the attached link:"+"<p><p>");
			// sb.append(
			// "<a href='https://riddler.bne.redhat.com/User_Guides/Vault/Book_Sign_Off_Process/tmp/en-US/html-single/index.html#sect-Book_Sign_Off_Process-Sign_off_request'>Vault Sign Off Process</a>"+"<p><p>");
			// addRegards(sb);
			addPlainDetails(sb, bean, url);
			addPlainNewQuestions(sb);
			addPlainRegards(sb);
			if (!"".equals(url)) {
				try {
					Mail.sendMail(prop.getSMTPAddress(), fromUserAddress,
							fromUserName, toUserAddress, ccUserAddress,
							subject, sb.toString());
				}
				catch (Exception e) {
					log.error(e.getMessage());
					throw e;
				}
				emailCount.setRequestid(bean.getRequestid());
				countlist = countDAO.get(emailCount);
				if (countlist != null && countlist.size() > 0) {
					countlist.get(0).setCount(Long.parseLong("1"));
					countDAO.update(countlist.get(0));
				}
			}
		}
		else if ("reject".equals(status)) {
			toUserAddress = getCreatorEmail(bean);
			if (bean.getSignedby() != null) {
				VAUser vauser = new VAUser();
				List<VAUser> users = null;
				vauser.setUsername(bean.getSignedby());
				users = userDAO.get(vauser);
				if (users != null && users.size() > 0) {
					ccUserAddress += "," + users.get(0).getUseremail();
				}
				else {
					vauser.setUseremail(bean.getSignedby() + "@redhat.com");
					vauser.setCreatedtime(DateUtil.getLocalUTCTime());
					userDAO.save(vauser);
					users = userDAO.get(vauser);
					ccUserAddress += "," + users.get(0).getUseremail();
				}
			}
			// sb.append(
			// "Your Vault request \""+bean.getRequestid()+"  "+bean.getRequestname()+"\" has been rejected by "+bean.getSignedby()+"@redhat.com at "+format.format(bean.getSignedtime())+".<p><p>");
			// sb.append( "Comment: <p>" + bean.getComment() + "<p><p>");
			// sb.append(
			// "You can find the request detailed description:<p><p>");
			// sb.append( "<ul><li>Created by: " + bean.getCreatedby() +
			// "</li>");
			// sb.append( "<li>Created: " + format.format(bean.getCreatedtime())
			// + "</li>");
			// if(bean.getRequesttime() != null){
			// sb.append( "<li>Due date: " +
			// dayFormat.format(bean.getRequesttime()) + "</li>");
			// }else{
			// sb.append( "<li>Due date: " + dayFormat.format(new Date()) +
			// "</li>");
			// }
			// //sb.append( "<li>Reject date: " +
			// format.format(bean.getSignedtime()) + "</li>";
			// //sb.append( "<li>Reject by: " + bean.getSignedby() +
			// "@redhat.com</li>";
			// String detail = bean.getDetail();
			// detail = StringUtil.showInEmail(detail);
			// detail = StringUtil.convertToHref(detail);
			//
			// sb.append( "<li>Detailed description: <p><p>" + detail +
			// "</li></ul><p><p>");
			// addDetailLink(sb,url);
			// addRegards(sb);
			// sb.append(
			// "Your Vault request \""+bean.getRequestid()+"  "+bean.getRequestname()+"\" has been rejected by "+bean.getSignedby()+"@redhat.com at "+format.format(bean.getSignedtime())+".");
			// addLine(sb);
			showComment(sb, bean);
			// addPlainDetailLink(sb,url);
			// addPlainDetail(sb,bean);
			addPlainDetails(sb, bean, url);
			addPlainQuestions(sb);
			addPlainRegards(sb);
			if (!"".equals(url)) {
				try {
					Mail.sendMail(prop.getSMTPAddress(), fromUserAddress,
							fromUserName, toUserAddress, ccUserAddress,
							subject, sb.toString());
				}
				catch (Exception e) {
					log.error(e.getMessage());
					throw e;
				}
			}
		}
		else if ("change".equals(status)) {
			String notifyReSignoffEmails = bean.getNeedSignOffList();// Sign off
																		// again,
																		// send
																		// out
																		// email,
																		// yyy@redhat.com
			String notifyNoSignoffEmails = bean.getNotifySignOffList();// No
																		// need
																		// sign
																		// off,
																		// just
																		// send
																		// out
																		// email,
																		// xxx@redhat.com

			StringBuffer reSignOffContent = new StringBuffer(sb.toString());
			compare = StringUtil.showInEmail(compare);
			if (notifyReSignoffEmails.length() > 0) {
				reSignOffContent
						.append("Requeset \""
								+ bean.getRequestname()
								+ "\"  has been modified in Vault and requires your review and sign-off by "
								+ dayFormat.format(bean.getRequesttime())
								+ " .");
				addLine(reSignOffContent);
				// addPlainDetailLink(reSignOffContent,url);
				// reSignOffContent.append("Changed by "+bean.getCreatedby()+"@redhat.com ["+format.format(bean.getCreatedtime())+"]:");
				// addLine(reSignOffContent);
				// reSignOffContent.append(compare+"");
				// addLine(reSignOffContent);
				addLine(reSignOffContent);
				reSignOffContent.append("Change details");
				addLine(reSignOffContent);
				reSignOffContent.append("-----------------------");
				addLine(reSignOffContent);
				reSignOffContent.append(compare);
				toUserAddress = notifyReSignoffEmails;
				// addPlainDetail(reSignOffContent,bean);
				addPlainDetails(reSignOffContent, bean, url);
				addPlainQuestions(reSignOffContent);
				addPlainRegards(reSignOffContent);

				// reSignOffContent.append("You can find the request detailed description:<p><p>");
				// if(bean.getRequesttime() != null){
				// reSignOffContent.append("<ul><li>Due date: " +
				// dayFormat.format(bean.getRequesttime()) + "</li>");
				// }else{
				// reSignOffContent.append("<li>Due date: " +
				// dayFormat.format(new Date()) + "</li>");
				// }
				// String detail = bean.getDetail();
				//
				// detail = StringUtil.showInEmail(detail);
				// detail = StringUtil.convertToHref(detail);
				//
				// reSignOffContent.append("<li>Detailed description: <p><p>" +
				// detail + "</li></ul><p><p><p>");
				// addDetailLink(reSignOffContent,url);
				// addRegards(reSignOffContent);
				if (!"".equals(url)) {
					try {
						Mail.sendMail(prop.getSMTPAddress(), fromUserAddress,
								fromUserName, toUserAddress, ccUserAddress,
								subject, reSignOffContent.toString());
					}
					catch (Exception e) {
						log.error(e.getMessage());
						throw e;
					}
					emailCount.setRequestid(bean.getRequestid());
					countlist = countDAO.get(emailCount);
					if (countlist != null && countlist.size() > 0
							&& notifyNoSignoffEmails.length() < 1) {
						countlist.get(0).setCount(Long.parseLong("1"));
						countDAO.update(countlist.get(0));
					}
				}
			}

			if (notifyNoSignoffEmails.length() > 0) {
				// sb.append( " which is NOT  requiring your re-sign off.");
				// // sb.append(
				// " which is <strong>NOT</strong> requiring your re-sign off.<p><p>");
				// addLine(sb);
				// sb.append(
				// "Changed by "+bean.getCreatedby()+"@redhat.com ["+format.format(bean.getCreatedtime())+"]:");
				// // sb.append(
				// "Changed by "+bean.getCreatedby()+"@redhat.com ["+format.format(bean.getCreatedtime())+"]:<p><p>");
				// addLine(sb);
				// sb.append( compare+"");
				// addLine(sb);
				// addLine(sb);
				// // sb.append(
				// "You can find the request detailed description:<p><p>");
				// // if(bean.getRequesttime() != null){
				// // sb.append( "<ul><li>Due date: " +
				// dayFormat.format(bean.getRequesttime()) + "</li>");
				// // }else{
				// // sb.append( "<li>Due date: " + dayFormat.format(new Date())
				// + "</li>");
				// // }
				// // String detail = bean.getDetail();
				// // detail = StringUtil.showInEmail(detail);
				// // detail = StringUtil.convertToHref(detail);
				// // sb.append( "<li>Detailed description: <p><p>" + detail +
				// "</li></ul><p><p>");
				// // addDetailLink(sb,url);
				// // addRegards(sb);
				// addPlainDetail(sb,bean);
				// addPlainDetailLink(sb,url);
				// addPlainRegards(sb);
				sb.append("Requeset \""
						+ bean.getRequestname()
						+ "\"  has been modified in Vault and requires your review.");
				addLine(sb);
				// addPlainDetailLink(sb,url);
				// reSignOffContent.append("Changed by "+bean.getCreatedby()+"@redhat.com ["+format.format(bean.getCreatedtime())+"]:");
				// addLine(reSignOffContent);
				// reSignOffContent.append(compare+"");
				// addLine(reSignOffContent);
				addLine(sb);
				sb.append("Change details");
				addLine(sb);
				sb.append("-----------------------");
				addLine(sb);
				sb.append(compare);
				// addPlainDetail(sb,bean);
				addPlainDetails(sb, bean, url);
				addPlainQuestions(sb);
				addPlainRegards(sb);
				toUserAddress = notifyNoSignoffEmails;
				if (!"".equals(url)) {
					try {
						Mail.sendMail(prop.getSMTPAddress(), fromUserAddress,
								fromUserName, toUserAddress, ccUserAddress,
								subject, sb.toString());
					}
					catch (Exception e) {
						log.error(e.getMessage());
						throw e;
					}
					emailCount.setRequestid(bean.getRequestid());
					countlist = countDAO.get(emailCount);
					if (countlist != null && countlist.size() > 0) {
						countlist.get(0).setCount(Long.parseLong("1"));
						countDAO.update(countlist.get(0));
					}
				}
			}
		}
		else if ("delete".equals(status)) {
			toUserAddress += getOwner(bean);
			// sb.append( "Request name: "+bean.getRequestname()+"<p><p>");
			// sb.append(
			// "A  request requiring your sign off has been deleted on the Vault portal<p><p>");
			// addDetail(sb,bean);
			// //addDetailLink(sb,url);
			// addRegards(sb);
			// sb.append( "Request name: "+bean.getRequestname()+"");
			// addLine(sb);
			// sb.append(
			// "A  request requiring your sign off has been deleted on the Vault portal");
			// addLine(sb);
			// addPlainDetail(sb,bean);
			addPlainDetails(sb, bean, url);
			addPlainQuestions(sb);
			addPlainRegards(sb);
			if (!"".equals(url)) {
				try {
					Mail.sendMail(prop.getSMTPAddress(), fromUserAddress,
							fromUserName, toUserAddress, ccUserAddress,
							subject, sb.toString());
				}
				catch (Exception e) {
					log.error(e.getMessage());
					throw e;
				}
			}
		}
		else if ("withdraw".equals(status)) {
			toUserAddress += getOwner(bean);
			addPlainDetails(sb, bean, url);
			addPlainQuestions(sb);
			addPlainRegards(sb);
			if (!"".equals(url)) {
				try {
					Mail.sendMail(prop.getSMTPAddress(), fromUserAddress,
							fromUserName, toUserAddress, ccUserAddress,
							subject, sb.toString());
				}
				catch (Exception e) {
					log.error(e.getMessage());
					throw e;
				}
			}
		}
		else if ("dueDate".equals(status)) {
			toUserAddress = getUnSigned(bean);
			ccUserAddress = "";
			// sb.append( "Request name: "+bean.getRequestname()+"");
			// addLine(sb);
			// sb.append(
			// "A  request requiring your sign off will be expired today on the Vault portal");
			// addLine(sb);
			// sb.append( "Request name: "+bean.getRequestname()+"<p><p>");
			// sb.append(
			// "A  request requiring your sign off will be expired today on the Vault portal <p><p>");
			// addDetail(sb,bean);
			// addDetailLink(sb,url);
			// addRegards(sb);
			// addPlainDetailLink(sb,url);
			// addPlainDetail(sb,bean);
			addPlainDetails(sb, bean, url);
			addPlainQuestions(sb);
			addPlainRegards(sb);
			if (!"".equals(url)) {
				try {
					Mail.sendMail(prop.getSMTPAddress(), fromUserAddress,
							fromUserName, toUserAddress, ccUserAddress,
							subject, sb.toString());
				}
				catch (Exception e) {
					log.error(e.getMessage());
					throw e;
				}
			}
		}
		log.debug(METHOD + ": success");
	}

	public String getUnSigned(Request request) {
		log.debug(request);
		String unsigned = "";
		String owner = request.getOwner().trim();
		if (owner != null) {
			String[] owners = owner.split(",");
			RequestHistory history = new RequestHistory();
			List<RequestHistory> signHistorys = null;
			RequestHistoryDAO requestHistoryDAO = new RequestHistoryDAO();
			for (int i = 0; i < owners.length; i++) {
				history.setRequestid(request.getRequestid());
				history.setIsHistory(false);
				history.setUseremail(owners[i]);
				history.setStatus(Request.SIGNED);
				signHistorys = requestHistoryDAO.get(history, false);
				if (signHistorys == null || signHistorys.size() == 0) {
					history.setStatus(Request.REJECTED);
					List<RequestHistory> rejectHistorys = null;
					rejectHistorys = requestHistoryDAO.get(history, false);

					history.setStatus(Request.SIGNED_BY);
					List<RequestHistory> signedByHistorys = null;
					signedByHistorys = requestHistoryDAO.get(history, false);
					if (rejectHistorys == null || rejectHistorys.size() == 0
							|| signedByHistorys == null
							|| signedByHistorys.size() == 0) {
						unsigned += owners[i] + ",";
					}
					else {
						Date rejectDate = rejectHistorys.get(0).getEditedtime();
						history.setStatus(Request.COMMENTS);
						List<RequestHistory> commentHistorys = null;
						commentHistorys = requestHistoryDAO.get(history, false);
						boolean edit = false;
						if (commentHistorys != null
								&& commentHistorys.size() > 0) {
							for (RequestHistory his : commentHistorys) {
								if (his.getEditedtime().compareTo(rejectDate) > 0) {
									edit = true;
									break;
								}
							}
						}
						if (edit) {
							unsigned += owners[i] + ",";
						}
					}
				}
				else {

				}
			}
			if (unsigned.length() != 0) {
				unsigned.substring(0, unsigned.lastIndexOf(','));
				log.debug(unsigned);
				return unsigned;
			}
		}
		return "";
	}

	public void addDetail(StringBuffer sb, Request bean) {
		sb.append("This is the detailed description:<p><p>");
		sb.append("<ul><li>Created by: " + bean.getCreatedby() + "</li>");
		sb.append("<li>Created: " + format.format(bean.getCreatedtime())
				+ "</li>");
		if (bean.getRequesttime() != null) {
			sb.append("<li>Due date: "
					+ dayFormat.format(bean.getRequesttime()) + "</li>");
		}
		else {
			sb.append("<li>Due date: " + dayFormat.format(DateUtil.getLocalUTCTime()) + "</li>");
		}
		String detail = bean.getDetail();
		detail = StringUtil.showInEmail(detail);
		detail = StringUtil.convertToHref(detail);
		sb.append("<li>Detailed description: <p><p>" + detail
				+ "</li></ul><p><p>");
	}

	public void addPlainDetail(StringBuffer sb, Request bean) {
		addLine(sb);
		addLine(sb);
		sb.append("Request details");
		addLine(sb);
		sb.append("Created by:  " + bean.getCreatedby() + "");
		addLine(sb);
		sb.append("Created on:  " + format.format(bean.getCreatedtime()) + "");
		addLine(sb);
		if (bean.getRequesttime() != null) {
			sb.append("Due date:    " + dayFormat.format(bean.getRequesttime())
					+ "");
		}
		else {
			sb.append("Due date:    " + dayFormat.format(DateUtil.getLocalUTCTime()) + "");
		}
		addBlankLine(sb);
		String detail = bean.getDetail();
		detail = StringUtil.showInEmail(detail);
		// detail = StringUtil.convertToHref(detail);
		sb.append("Description:");
		addLine(sb);
		sb.append(detail + "");
		addLine(sb);
	}

	public void addPlainDetails(StringBuffer sb, Request bean, String url) {
		addLine(sb);
		addLine(sb);
		sb.append("Vault Request #" + bean.getRequestid());
		addLine(sb);
		sb.append("------------------------------");
		addLine(sb);
		addLine(sb);

		sb.append(replaceDetail(bean.getDetail()));
		addLine(sb);
		addLine(sb);
		sb.append("==============================");
		addLine(sb);
		sb.append("Vault Request Details");
		addLine(sb);
		addLine(sb);
		sb.append(url + "");
		addLine(sb);
		sb.append("Created by: " + bean.getCreatedby() + "");
		addLine(sb);
		sb.append("Created on: " + format.format(bean.getCreatedtime()) + "");
		addLine(sb);
		if (bean.getRequesttime() != null) {
			sb.append("Due date: " + dayFormat.format(bean.getRequesttime())
					+ "");
		}
		else {
			sb.append("Due date: " + dayFormat.format(DateUtil.getLocalUTCTime()) + "");
		}
		addLine(sb);
		sb.append("==============================");
	}

	public void addRegards(StringBuffer sb) {
		sb.append("Regards,<p><p>");
		sb.append("<a href='mailto:");
		sb.append(email);
		sb.append("'>");
		sb.append(email);
		sb.append("</a> <p><p>");
	}

	public void addPlainQuestions(StringBuffer sb) {
		addLine(sb);
		addLine(sb);
		sb.append("Questions?");
		addLine(sb);
		sb.append("-----------------");
		addLine(sb);
		sb.append("To learn more about using Vault, visit:");
		addLine(sb);
		sb.append("https://riddler.bne.redhat.com/Vault-User_Guide/index.html");
		addLine(sb);
		sb.append("Or contact us at ");
		sb.append(email);
		addLine(sb);

	}

	public void addPlainNewQuestions(StringBuffer sb) {
		addLine(sb);
		addLine(sb);
		sb.append("Questions?");
		addLine(sb);
		sb.append("-----------------");
		addLine(sb);
		sb.append("To learn more about the Vault sign-off process, visit:");
		addLine(sb);
		sb.append("https://riddler.bne.redhat.com/User_Guides/Vault/Book_Sign_Off_Process/tmp/en-US/html-single/index.html");
		addLine(sb);
		sb.append("Or contact us at ");
		sb.append(email);
		addLine(sb);
	}

	public void addPlainRegards(StringBuffer sb) {
		addLine(sb);
		sb.append("Regards,");
		addLine(sb);
		sb.append("Vault service Team");
		addLine(sb);
	}

	public void addDetailLink(StringBuffer sb, String url) {
		sb.append("To read and respond to the request, please follow the attached link:<p><p>");
		sb.append(url + "<p><p>");
	}

	public void addPlainDetailLink(StringBuffer sb, String url) {
		addLine(sb);
		sb.append("To read and respond to the request, visit:");
		addLine(sb);
		sb.append(url + "");
		addLine(sb);
	}

	public String getOwner(Request bean) {
		String owner = "";
		if (bean.getOwner() != null) {
			String[] emailArr = bean.getOwner().split(",");
			for (int i = 0; i < emailArr.length; i++) {
				if (i == emailArr.length - 1) {
					owner += emailArr[i];
				}
				else {
					owner += emailArr[i] + ",";
				}
			}
		}
		return owner;
	}

	public void addLine(StringBuffer sb) {
		sb.append(nextLine);
	}

	public void addBlankLine(StringBuffer sb) {
		sb.append(nextLine).append(nextLine);
	}

	public void showComment(StringBuffer sb, Request bean) {
		sb.append("Comment:");
		addLine(sb);
		sb.append(StringUtil.showInEmail(bean.getComment()));
		addLine(sb);
	}

	private String replaceDetail(String detail) {
		detail = detail.replaceAll("<br>", "\n");
		detail = detail.replaceAll("&amp;amp;", "&");
		detail = detail.replaceAll("&amp;", "&");
		detail = detail.replaceAll("&quot;", "\"");
		detail = detail.replaceAll("&nbsp;", " ");
		detail = detail.replaceAll("&copy;", "©");
		detail = detail.replaceAll("&lt;", "<");
		detail = detail.replaceAll("&gt;", ">");
		return detail;
	}
}
