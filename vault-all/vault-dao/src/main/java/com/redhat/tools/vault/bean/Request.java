package com.redhat.tools.vault.bean;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.redhat.tools.vault.util.DateUtil;

/**
 * @author wezhao
 * 
 */
public class Request {
	private static final long serialVersionUID = 1L;

	public static String WAITING = "Waiting";

	public static String SIGNED = "Signed";

	public static String COMMENTS = "Comments";

	public static String REJECTED = "Rejected";

	public static String SIGNED_ONBEHALF = "SignedOnBehalf";

	public static String SIGNED_BY = "SignedByRequestor";

	public static String ACTIVE = "In progress";

	public static String INACTIVE = "Approved";
	
	public static String WITHDRAW = "withdrawn";

	public static String COMMON = "Common";

	public static String ADVANCE = "Advance";

	public static Integer IS_PUBLIC = 1;

	public static Integer IS_NOT_PUBLIC = 0;

	public static String PROPERTY_REQUEST_ATTACHMENT = "RequestAttachment";

	public static String PROPERTY_REQUEST_NOTIFYCATION = "RequestNotification";

	public static String NOTIFICATION_TYPE_DEFAULT = "0"; // -----, same as "1"

	public static String NOTIFICATION_TYPE_REQUIRED_SIGNOFF = "1"; // Will send
																	// email to
																	// owners,
																	// and need
																	// signoff
																	// again.

	public static String NOTIFICATION_TYPE_NOTIFY_CHANGE = "2"; // Will send
																// email to
																// owners, and
																// need no more
																// signoff.

	public static String NOTIFICATION_TYPE_DO_NOTHING = "3"; // Will not send
																// email to
																// request
																// owners.

	/** Map Properties. */
	public static String PROPERTY_ATTACHMENT_USER = "AttachmentUser";

	public static String PROPERTY_ATTACHMENT_SIZE = "AttachmentSize";

	public static String PROPERTY_ATTACHMENT_DATE = "AttachmentDate";

	public static String PROPERTY_ATTACHMENT_MD5 = "AttachmentMD5";

	public static String PROPERTY_REQUESTID = "requestid";

	public static String PROPERTY_REQUESTNAME = "requestname";

	public static String PROPERTY_SUMMARY = "summary";

	public static String PROPERTY_DETAIL = "detail";

	public static String PROPERTY_OWNER = "owner";

	public static String PROPERTY_CREATEDBY = "createdby";

	public static String PROPERTY_CREATEDTIME = "createdtime";

	public static String PROPERTY_REQUESTTIME = "requesttime";

	public static String PROPERTY_SIGNEDBY = "signedby";

	public static String PROPERTY_SIGNEDTIME = "signedtime";

	public static String PROPERTY_STATUS = "status";

	public static String PROPERTY_COMMET = "comment";

	public static String PROPERTY_VALUE = "value";

	public static String PROPERTY_EDITEDBY = "editedby";
	
	public static String PROPERTY_FORWARD = "forward";

	public static String PROPERTY_EDITEDTIME = "editedtime";

	public static String PROPERTY_DYNAMIC = "dynamic";

	public static String PROPERTY_VERSIONID = "versionid";

	public static String PROPERTY_IS_PUBLIC = "is_public";

	public static String PROPERTY_CC_LIST = "forward";
	
	public static String PROPERTY_FROM = "from";

	public static String PROPERTY_REQUEST_VERSION = "requestVersion";

	private Long requestid = null;

	private String requestname = null;

	private String summary = null;

	private String detail = null;

	private String owner = null;

	private String createdby = null;

	private Date createdtime = null;

	private Date requesttime = null;

	private String signedby = null;

	private Date signedtime = null;

	private String status = null;

	private String comment = null;

	private Set<RequestMap> maps = null;

	private Long versionid = null;

	private Integer is_public = null;

	private String canEdit = null;

	private String canSignAndReject = null;

	private Long value = null;

	private String editedby = null;

	private Date editedtime = null;

	private String productname = null;

	private String versiondesc = null;

	private String forward = null;// CC List, for bug:634627

	private Integer requestVersion = null; // request version
	
	private Integer from = null;

	public Integer getFrom() {
		return from;
	}

	public void setFrom(Integer from) {
		this.from = from;
	}

	/****** transparent *****/
	private String parent = null;

	private String children = null;// 170,171,172

	private String waitingList = null;

	private String commentList = null;

	private String signoffList = null;

	private String rejectedList = null;

	private Set<RequestRelationship> relations = new HashSet<RequestRelationship>();

	private String needSignOffList = null;

	private String notifySignOffList = null;

	private String sendEmailList = null;

	private Boolean isSignOnBehalf = null;

	public Request() {
	}

	public Request(Long requestId) {
		this.requestid = requestId;
	}

	public Boolean getIsSignOnBehalf() {
		return isSignOnBehalf;
	}

	public void setIsSignOnBehalf(Boolean isSignOnBehalf) {
		this.isSignOnBehalf = isSignOnBehalf;
	}

	public String getNeedSignOffList() {
		return needSignOffList;
	}

	public void setNeedSignOffList(String needSignOffList) {
		this.needSignOffList = needSignOffList;
	}

	public String getNotifySignOffList() {
		return notifySignOffList;
	}

	public void setNotifySignOffList(String notifySignOffList) {
		this.notifySignOffList = notifySignOffList;
	}

	public String getSendEmailList() {
		return sendEmailList;
	}

	public void setSendEmailList(String sendEmailList) {
		this.sendEmailList = sendEmailList;
	}

	public Integer getRequestVersion() {
		return requestVersion;
	}

	public void setRequestVersion(Integer requestVersion) {
		this.requestVersion = requestVersion;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public void removeRelationship(RequestRelationship o) {
		relations.remove(o);
	}

	public void addRelationship(RequestRelationship o) {
		relations.add(o);
	}

	public Set<RequestRelationship> getRelations() {
		return relations;
	}

	public void setRelations(Set<RequestRelationship> relations) {
		this.relations = relations;
	}

	public String getChildren() {
		return children;
	}

	public void setChildren(String children) {
		this.children = children;
	}

	public String getWaitingList() {
		return waitingList;
	}

	public void setWaitingList(String waitingList) {
		this.waitingList = waitingList;
	}

	public String getCommentList() {
		return commentList;
	}

	public void setCommentList(String commentList) {
		this.commentList = commentList;
	}

	public String getSignoffList() {
		return signoffList;
	}

	public void setSignoffList(String signoffList) {
		this.signoffList = signoffList;
	}

	public String getRejectedList() {
		return rejectedList;
	}

	public void setRejectedList(String rejectedList) {
		this.rejectedList = rejectedList;
	}

	public String getForward() {
		return forward;
	}

	public void setForward(String forward) {
		this.forward = forward;
	}

	public Long getRequestid() {
		return requestid;
	}

	public void setRequestid(Long requestid) {
		this.requestid = requestid;
	}

	public String getRequestname() {
		return requestname;
	}

	public void setRequestname(String requestname) {
		this.requestname = requestname;
	}

	public Long getVersionid() {
		return versionid;
	}

	public void setVersionid(Long versionid) {
		this.versionid = versionid;
	}

	public Integer getIs_public() {
		return is_public;
	}

	public void setIs_public(Integer isPublic) {
		is_public = isPublic;
	}

	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
	}

	public String getVersiondesc() {
		return versiondesc;
	}

	public void setVersiondesc(String versiondesc) {
		this.versiondesc = versiondesc;
	}

	public String getEditedby() {
		return editedby;
	}

	public void setEditedby(String editedby) {
		this.editedby = editedby;
	}

	public Date getEditedtime() {
		return editedtime;
	}

	public void setEditedtime(Date editedtime) {
		this.editedtime = editedtime;
	}

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}

	public String getCanEdit() {
		return canEdit;
	}

	public void setCanEdit(String canEdit) {
		this.canEdit = canEdit;
	}

	public String getCanSignAndReject() {
		return canSignAndReject;
	}

	public void setCanSignAndReject(String canSignAndReject) {
		this.canSignAndReject = canSignAndReject;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getCreatedby() {
		return createdby;
	}

	public void setCreatedby(String createdby) {
		this.createdby = createdby;
		//this.setCreatedtime(DateUtil.getLocalUTCTime());
	}

	public Date getCreatedtime() {
		return createdtime;
	}

	public void setCreatedtime(Date createdtime) {
		this.createdtime = createdtime;
	}

	public Date getRequesttime() {
		return requesttime;
	}

	public void setRequesttime(Date requesttime) {
		this.requesttime = requesttime;
	}

	public String getSignedby() {
		return signedby;
	}

	public void setSignedby(String signedby) {
		this.signedby = signedby;
		//this.setSignedtime(DateUtil.getLocalUTCTime());
	}

	public Date getSignedtime() {
		return signedtime;
	}

	public void setSignedtime(Date signedtime) {
		this.signedtime = signedtime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Set<RequestMap> getMaps() {
		return maps;
	}

	public void setMaps(Set<RequestMap> maps) {
		this.maps = maps;
	}

	public boolean displayEditButton(String userName) {
		if(WITHDRAW.equals(status)){
			return false;
		}
		return userName.equals(this.getCreatedby());
	}

	public boolean displaySignoffRejectButton(String userName) {
		if (status.equals(INACTIVE) || status.equals(WITHDRAW)) {
			return false;
		}
		if (this.getOwner() == null)
			return false;

		String[] owners = this.getOwner().split(",");
		if (owners == null || owners.length < 1) {
			return false;
		}

		for (int i = 0; i < owners.length; i++) {
			if ((userName + "@redhat.com").toUpperCase().equals(
					owners[i].toUpperCase())) {
				return true;
			}
		}
		return false;
	}

	public boolean isSignatory(String userName) {
		if (this.getOwner() == null)
			return false;
		String[] owners = this.getOwner().split(",");
		if (owners == null || owners.length < 1) {
			return false;
		}

		for (int i = 0; i < owners.length; i++) {
			if ((userName + "@redhat.com").toUpperCase().equals(
					owners[i].toUpperCase())
					|| userName.toUpperCase().equals(
							this.getCreatedby().toUpperCase())) {
				return true;
			}
		}
		return false;
	}

	public boolean displaySignoffOnBehalfButton(String userName) {
		if (status.equals(INACTIVE) || status.equals(WITHDRAW)) {
			return false;
		}
		if (userName == null || "".equals(userName.trim())) {
			return false;
		}
		return userName.toUpperCase().equals(createdby.toUpperCase());
		/*
		 * if(!userName.toUpperCase().equals(createdby.toUpperCase())){ return
		 * false; } String[] owners = this.getOwner().split(","); if(owners ==
		 * null || owners.length < 1){ return false; } //boolean
		 * onlySelfInSignatories = true; for(int i=0;i<owners.length;i++){
		 * if(!(userName
		 * +"@redhat.com").toUpperCase().equals(owners[i].toUpperCase())){
		 * //onlySelfInSignatories = false; return true; } } return false;
		 */
	}

	// start from 0
	public int getCcCount() {
		if (forward != null && !"".equals(forward)) {
			return forward.split(",").length - 1;
		}
		return 0;
	}

	// start from 0
	public int getSignatoryCount() {
		if (owner != null && !"".equals(owner)) {
			return owner.split(",").length - 1;
		}
		return 0;
	}

	// start from 0
	public int getChildCount() {
		if (children != null && !"".equals(children)) {
			return children.split(",").length - 1;
		}
		return 0;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getName()).append("[");
		sb.append("requestid=" + requestid);
		sb.append(" ,requestname=" + requestname);
		sb.append(" ,summary=" + summary);
		sb.append(" ,detail=" + detail);
		sb.append(" ,owner=" + owner);
		sb.append(" ,createdby=" + createdby);
		sb.append(" ,createdtime=" + createdtime);
		sb.append(" ,signedby=" + signedby);
		sb.append(" ,signedtime=" + signedtime);
		sb.append(" ,status=" + status);
		sb.append(" ,comment=" + comment);
		sb.append(" ,maps=" + maps);
		sb.append(" ,canEdit=" + canEdit);
		sb.append(" ,canSignAndReject=" + canSignAndReject);
		sb.append(" ,value=" + value);
		sb.append(" ,editedby=" + editedby);
		sb.append(" ,editedtime=" + editedtime);
		sb.append(" ,versionid=" + versionid);
		sb.append(" ,is_public=" + is_public);
		sb.append(" ,forward=" + forward);
		sb.append(" ,from=" + from);
		sb.append("]");
		return sb.toString();
	}
}
