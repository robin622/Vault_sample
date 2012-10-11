package com.redhat.tools.vault.service;

import net.sf.json.JSONObject;

public interface CommentService {

	JSONObject AddComment(String requestid, String username, String useremail,
			String actionURL, String comment);
	
}
