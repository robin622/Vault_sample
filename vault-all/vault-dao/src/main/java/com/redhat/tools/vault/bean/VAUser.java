package com.redhat.tools.vault.bean;

import java.util.Date;

/**
 * @author <a href="mailto:speng@redhat.com">Peng Song</a>
 * @version $Revision$
 */
public class VAUser {

	public static String PROPERTY_USERID = "userid";

	public static String PROPERTY_USERNAME = "username";

	public static String PROPERTY_USEREMAIL = "useremail";

	public static String PROPERTY_CREATEDTIME = "createdtime";

	public static String PROPERTY_ISADMIN = "is_admin";

	public static String PROPERTY_LOGINCOUNT = "login_count";

	private Long userid = null;

	private String username = null;

	private String useremail = null;

	private Date createdtime = null;

	private Integer is_admin = null;

	private Integer login_count = null;

	public Integer getLogin_count() {
		return login_count;
	}

	public void setLogin_count(Integer loginCount) {
		login_count = loginCount;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUseremail() {
		return useremail;
	}

	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}

	public Date getCreatedtime() {
		return createdtime;
	}

	public void setCreatedtime(Date createdtime) {
		this.createdtime = createdtime;
	}

	public Integer getIs_admin() {
		return is_admin;
	}

	public void setIs_admin(Integer isAdmin) {
		is_admin = isAdmin;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getName()).append("[");
		sb.append("userid=" + userid);
		sb.append(" ,username=" + username);
		sb.append(" ,useremail=" + useremail);
		sb.append(" ,createdtime=" + createdtime);
		sb.append(" ,is_admin=" + is_admin);
		sb.append(" ,login_count=" + login_count);
		sb.append("]");
		return sb.toString();
	}
}
