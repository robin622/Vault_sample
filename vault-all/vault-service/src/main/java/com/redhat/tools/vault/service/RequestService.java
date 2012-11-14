package com.redhat.tools.vault.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.redhat.tools.vault.bean.Request;
import com.redhat.tools.vault.bean.RequestHistory;
import com.redhat.tools.vault.bean.RequestRelationship;

public interface RequestService {
	
	public List<Request> getMyRequest(String username);
	
	public List<Request> getDetailRequest(String requestId);
	
	public boolean displaySignButton(String userName, long requestId);

	public boolean displaySignOnBehalfButton(String userName, Long requestId);

	public JSONObject findRequest(String userName, String userEmail);
	
	public JSONObject showAllEmails();
	
	public JSONObject listNoneSign(String requestId);
	
	public JSONObject SignedOrRject(String requestId,String username,String comment, String type,String useremail,String flag,String onBehalfUsers);
	
	public List<Request> getWaitRequests(String userName, String userEmail);
	
	public List<Request> getCanViewRequests(String userName, String userEmail);
	
	public List<Request> getSignedOffRequests(String userName, String userEmail);
	
	public List<Request> getCCToMeRequests(String userName, String userEmail);
	
    public List<Request> advanceSearch(String requestName,String creator,String versionid,String productid,String status,
            String owneremail,String userName,String userEmail);

    public Long save(Request request);

    public void update(Request request);

    public Long saveRelationShip(RequestRelationship rls);

    public String[] generateParent(Long requestId);

    public void disableRelationShip(RequestRelationship childR);
    
    public JSONObject EditVersion(String versionid);

    public void deleteRequest(Request request);

    public List<Request> findByIds(Long[] requestIds);

    public void updateEmailCount(Request request);
    
    public Map<String,Long> getRequestCount(String userName,String userEmail);

    public Request getRequest(Request request);

    public Long getParentId(Request current);

    public List<Long> getChildId(Request current);

    public List<RequestHistory> getHistory(RequestHistory history,boolean b);

    public void disableHistory(RequestHistory history);

    public String compare(Request current,Request request);
    
    public void withdrawRequest(Request current, String requestid, String requestVersion, String userName, String userEmail);

}
