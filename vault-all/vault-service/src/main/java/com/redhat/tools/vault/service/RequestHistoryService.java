package com.redhat.tools.vault.service;

import net.sf.json.JSONObject;

public interface RequestHistoryService {
	
	public JSONObject RequestHistory(String requestid);
}
