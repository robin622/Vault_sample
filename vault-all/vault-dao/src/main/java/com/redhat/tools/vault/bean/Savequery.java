package com.redhat.tools.vault.bean;

import java.util.Date;
/**
 * 
 * @author wguo@redhat.com
 *
 */
public class Savequery{

	public 	static 		String 		OPERTAION_SAVE 			= 	"save";
	public 	static 		String 		OPERTAION_SEARCH 		= 	"search";
	
	public 	static 		String 		PROPERTY_QUERYID 		= 	"queryid";
	public 	static 		String 		PROPERTY_QUERYNAME 		= 	"queryname";
	public 	static 		String 		PROPERTY_SEARCHNAME 	= 	"searchname";
	public 	static 		String 		PROPERTY_CREATEDBY 		= 	"createdby";
	public 	static 		String 		PROPERTY_CREATEDTIME	= 	"createdtime";
	public 	static 		String 		PROPERTY_VERSIONID 		= 	"versionid";
	public 	static 		String 		PROPERTY_PRODUCTID 		= 	"productid";
	public 	static 		String 		PROPERTY_STATUS 		= 	"status";
	public 	static 		String 		PROPERTY_OWNER 			= 	"owner";
	public 	static 		String 		PROPERTY_SEARCHCREATOR 	= 	"searchcreator";
	public 	static 		String 		PROPERTY_TYPE 			= 	"type";
	
    private 			Long 		queryid 				= 	null;
    private 			String 		queryname 				= 	null;
    private 			String 		searchname 				= 	null;
    private 			String 		createdby 				= 	null;
    private 			Date 		createdtime 			= 	null;
    private 			Long 		versionid 				= 	null;
    private 			String 		status 					= 	null;
    private 			String 		owner 					= 	null;
    private 			String 		searchcreator 			= 	null;
    private 			String 		type 					= 	null;
    private 			Long 		productid				= 	null;

	

	public Long getProductid() {
		return productid;
	}

	public void setProductid(Long productid) {
		this.productid = productid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getCreatedtime() {
		return createdtime;
	}

	public void setCreatedtime(Date createdtime) {
		this.createdtime = createdtime;
	}

	public String getSearchcreator() {
		return searchcreator;
	}

	public void setSearchcreator(String searchcreator) {
		this.searchcreator = searchcreator;
	}

	public Long getQueryid() {
		return queryid;
	}

	public void setQueryid(Long queryid) {
		this.queryid = queryid;
	}

	public String getQueryname() {
		return queryname;
	}

	public void setQueryname(String queryname) {
		this.queryname = queryname;
	}

	public String getSearchname() {
		return searchname;
	}

	public void setSearchname(String searchname) {
		this.searchname = searchname;
	}

	public String getCreatedby() {
		return createdby;
	}

	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}

	public Long getVersionid() {
		return versionid;
	}

	public void setVersionid(Long versionid) {
		this.versionid = versionid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getName()).append("[");
		sb.append("queryid=" + queryid);
		sb.append(" ,queryname=" + queryname);
		sb.append(" ,searchname=" + searchname);
		sb.append(" ,createdby=" + createdby);
		sb.append(" ,createdtime=" + createdtime);
		sb.append(" ,versionid=" + versionid);
		sb.append(" ,productid=" + productid);
		sb.append(" ,status=" + status);
		sb.append(" ,owner=" + owner);
		sb.append(" ,searchcreator=" + searchcreator);
		sb.append(" ,type=" + type);
		sb.append("]");
		return sb.toString();
	}
}
