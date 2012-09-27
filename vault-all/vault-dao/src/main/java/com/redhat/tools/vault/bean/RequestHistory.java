package com.redhat.tools.vault.bean;

import java.util.Date;

/**
 * @author <a href="mailto:speng@redhat.com">Peng Song</a>
 * @version $Revision$
 */
public class RequestHistory {
	private static final long serialVersionUID = 1L;

	public static String PROPERTY_HISTORYID = "historyid";

	public static String PROPERTY_REQUESTID = "requestid";

	public static String PROPERTY_EDITEDBY = "editedby";

	public static String PROPERTY_EDITEDTIME = "editedtime";

	public static String PROPERTY_STATUS = "status";

	public static String PROPERTY_COMMET = "comment";

	public static String PROPERTY_USEREMAIL = "useremail";

	public static String PROPERTY_REQUEST_VERSION = "requestVersion";

	public static String PROPERTY_IS_HISTORY = "isHistory";
	
	public static String PROPERTY_STARTEDATE = "startDate";
	
	public static String PROPERTY_ENDDATE = "endDate";

	private Long historyid = null;

	private Long requestid = null;

	private String editedby = null;

	private Date editedtime = null;

	private String status = null;

	private String comment = null;

	private String useremail = null;

	private String reportStatus = null;

	private String notifiyOptionValue = null; // 0,1,2,3

	private String displayFlag = null; // 0,1

	private Integer requestVersion = null; // request version

	private Boolean isHistory = null;// comment or sign off is history

	private String editDate = null;
	
	private String startDate = null;
	
	private String endDate = null;

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getEditDate() {
		return editDate;
	}

	public void setEditDate(String editDate) {
		this.editDate = editDate;
	}

	public Boolean getIsHistory() {
		return isHistory;
	}

	public void setIsHistory(Boolean isHistory) {
		this.isHistory = isHistory;
	}

	public Integer getRequestVersion() {
		return requestVersion;
	}

	public void setRequestVersion(Integer requestVersion) {
		this.requestVersion = requestVersion;
	}

	public String getDisplayFlag() {
		return displayFlag;
	}

	public void setDisplayFlag(String displayFlag) {
		this.displayFlag = displayFlag;
	}

	public String getReportStatus() {
		return reportStatus;
	}

	public void setReportStatus(String reportStatus) {
		this.reportStatus = reportStatus;
	}

	public String getUseremail() {
		return useremail;
	}

	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}

	public Long getRequestid() {
		return requestid;
	}

	public void setRequestid(Long requestid) {
		this.requestid = requestid;
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

	public Long getHistoryid() {
		return historyid;
	}

	public void setHistoryid(Long historyid) {
		this.historyid = historyid;
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

	public String getNotifiyOptionValue() {
		return notifiyOptionValue;
	}

	public void setNotifiyOptionValue(String notifiyOptionValue) {
		this.notifiyOptionValue = notifiyOptionValue;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getName()).append("[");
		sb.append("historyid=" + historyid);
		sb.append(" ,requestid=" + requestid);
		sb.append(" ,editedby=" + editedby);
		sb.append(" ,editedtime=" + editedtime);
		sb.append(" ,status=" + status);
		sb.append(" ,comment=" + comment);
		sb.append(" ,useremail=" + useremail);
		sb.append("]");
		return sb.toString();
	}
}
