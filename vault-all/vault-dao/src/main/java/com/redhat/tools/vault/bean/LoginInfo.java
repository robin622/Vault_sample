package com.redhat.tools.vault.bean;

import java.util.Date;

/**
 * 
 * @author yizhai
 *
 */
public class LoginInfo {

	public static String PROPERTY_LOGINID = "loginid";
	public static String PROPERTY_USERNAME = "username";
	public static String PROPERTY_LOGINTIME = "logintime";
	
	private Long loginid;
	private String username;
	private Date logintime;
	
	public Long getLoginid() {
		return loginid;
	}
	
	public void setLoginid(Long loginid) {
		this.loginid = loginid;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getLogintime() {
		return logintime;
	}
	
	public void setLogintime(Date logintime) {
		this.logintime = logintime;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("loginid=" + loginid);
		sb.append("userid=" + username);
		sb.append("logintime=" + logintime);
		return sb.toString();
	}
	
}
