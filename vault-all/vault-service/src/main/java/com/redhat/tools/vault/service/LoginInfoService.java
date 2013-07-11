package com.redhat.tools.vault.service;

import java.util.Date;

import net.sf.json.JSONObject;

public interface LoginInfoService {

	public JSONObject AddLoginInfo(String username, Date logintime);

}
