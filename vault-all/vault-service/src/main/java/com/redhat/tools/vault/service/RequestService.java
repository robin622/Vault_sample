package com.redhat.tools.vault.service;

import java.util.List;
import net.sf.json.JSONObject;
import com.redhat.tools.vault.bean.Request;

public interface RequestService {
	
	public List<Request> getMyRequest(String username);
	
	public List<Request> getDetailRequest(String requestId);
	
	public boolean displaySignButton(String userName, long requestId);

	public boolean displaySignOnBehalfButton(String userName, Long requestId);

	public JSONObject findRequest();
	
	public JSONObject showAllEmails();
	
	public JSONObject listNoneSign(String requestId);
	
	public JSONObject SignedOrRject(String requestId,String username,String comment, String type,String useremail,String flag,String onBehalfUsers);
}
