package com.redhat.tools.vault.service;

import net.sf.json.JSONObject;

public interface ReplyService {
	public JSONObject AddReply(String baseid,String replyComment,String editedby,String requestid,String historyid);
	public JSONObject ShowReply(String requestid,String historyid);
}