package com.redhat.tools.vault.bean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.redhat.tools.vault.util.DateUtil;


/**
 * @author wezhao
 */
public class ReplyComment {

	public static String PROPERTY_REPLYID = "replyid";

	public static String PROPERTY_BASEID = "baseid";

	public static String PROPERTY_REPLYCOMMENT = "replycomment";

	public static String PROPERTY_EDITEDBY = "editedby";

	public static String PROPERTY_EDITEDTIME = "editedtime";

	private Long replyid = null;

	private Long baseid = null;

	private String replycomment = null;

	private String editedby = null;

	private String editedtime = null;
	
	public ReplyComment() {

	}

	public ReplyComment(Long baseid, String replycomment, String editedby, String editedtime) {
		this.baseid = baseid;
		this.replycomment = replycomment;
		this.editedby = editedby;
		this.editedtime = editedtime;
	}

	public Long getReplyid() {
		return replyid;
	}

	public void setReplyid(Long replyid) {
		this.replyid = replyid;
	}

	public Long getBaseid() {
		return baseid;
	}

	public void setBaseid(Long baseid) {
		this.baseid = baseid;
	}

	public String getReplycomment() {
		return replycomment;
	}

	public void setReplycomment(String replycomment) {
		this.replycomment = replycomment;
	}

	public String getEditedby() {
		return editedby;
	}

	public void setEditedby(String editedby) {
		this.editedby = editedby;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		this.setEditedtime(format.format(DateUtil.getLocalUTCTime()).toString());
	}

	public String getEditedtime() {
		return editedtime;
	}

	public void setEditedtime(String editedtime) {
		this.editedtime = editedtime;
	}

}
