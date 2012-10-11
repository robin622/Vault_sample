package com.redhat.tools.vault.bean;

import java.util.Date;

/**
 * @author <a href="mailto:speng@redhat.com">Peng Song</a>
 * @version $Revision$
 */
public class SendemailCount {

	public static String PROPERTY_SENDID = "sendid";

	public static String PROPERTY_REQUESTID = "requestid";

	public static String PROPERTY_REQUESTTIME = "requesttime";

	public static String PROPERTY_COUNT = "count";

	private Long sendid = null;

	private Long requestid = null;

	private Date requesttime = null;

	private Long count = null;

	public Long getRequestid() {
		return requestid;
	}

	public void setRequestid(Long requestid) {
		this.requestid = requestid;
	}

	public Long getSendid() {
		return sendid;
	}

	public void setSendid(Long sendid) {
		this.sendid = sendid;
	}

	public Date getRequesttime() {
		return requesttime;
	}

	public void setRequesttime(Date requesttime) {
		this.requesttime = requesttime;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("sendid=" + sendid);
		sb.append(" requestid=" + requestid);
		sb.append(" requesttime=" + requesttime);
		sb.append(" count=" + count);
		return sb.toString();
	}
}
