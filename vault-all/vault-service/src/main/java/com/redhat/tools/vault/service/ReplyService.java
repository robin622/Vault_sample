package com.redhat.tools.vault.service;

import java.util.List;
import net.sf.json.JSONObject;
import com.redhat.tools.vault.bean.Request;

public interface ReplyService {
	public JSONObject AddReply(String baseid,String replyComment,String editedby,String requestid,String historyid);
	public JSONObject ShowReply(String requestid,String historyid);
}
