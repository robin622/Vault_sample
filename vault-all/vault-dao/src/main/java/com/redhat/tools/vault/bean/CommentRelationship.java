package com.redhat.tools.vault.bean;

/**
 * @author wezhao
 * 
 */
public class CommentRelationship {
	
	public static String PROPERTY_ID = "id";
	
	public static String PROPERTY_HISTORYID = "historyid";

	public static String PROPERTY_REQUESTID = "requestid";

	public static String PROPERTY_REPLYID = "replyid";

	private Long id = null;

	private Long historyid = null;

	private Long requestid = null;

	private Long replyid = null;
	
	public CommentRelationship() {
		
	}

	public CommentRelationship(Long historyid, Long requestid, Long replyid) {
		this.historyid = historyid;
		this.requestid = requestid;
		this.replyid = replyid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getHistoryid() {
		return historyid;
	}

	public void setHistoryid(Long historyid) {
		this.historyid = historyid;
	}

	public Long getRequestid() {
		return requestid;
	}

	public void setRequestid(Long requestid) {
		this.requestid = requestid;
	}

	public Long getReplyid() {
		return replyid;
	}

	public void setReplyid(Long replyid) {
		this.replyid = replyid;
	}

}
