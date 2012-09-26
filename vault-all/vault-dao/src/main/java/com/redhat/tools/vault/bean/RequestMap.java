package com.redhat.tools.vault.bean;

import java.util.Map;

/**
 * @author <a href="mailto:speng@redhat.com">Peng Song</a>
 * @version $Revision$
 */
public class RequestMap {

	public static String PROPERTY_MAPID = "mapid";

	public static String PROPERTY_MAPNAME = "mapname";

	public static String PROPERTY_MAPVALUE = "mapvalue";

	public static String PROPERTY_REQUESTID = "requestid";

	public static String PROPERTY_REQUEST_VERSION = "requestVersion";

	private Long mapid = null;

	private String mapname = null;

	private String mapvalue = null;

	private Long requestid = null;

	private Integer requestVersion = null; // request version

	public Integer getRequestVersion() {
		return requestVersion;
	}

	public void setRequestVersion(Integer requestVersion) {
		this.requestVersion = requestVersion;
	}

	private Map<String, String> dynamic = null;

	public Long getRequestid() {
		return requestid;
	}

	public void setRequestid(Long requestid) {
		this.requestid = requestid;
	}

	public Long getMapid() {
		return mapid;
	}

	public void setMapid(Long mapid) {
		this.mapid = mapid;
	}

	public String getMapname() {
		return mapname;
	}

	public void setMapname(String mapname) {
		this.mapname = mapname;
	}

	public String getMapvalue() {
		return mapvalue;
	}

	public void setMapvalue(String mapvalue) {
		this.mapvalue = mapvalue;
	}

	public Map<String, String> getDynamic() {
		return dynamic;
	}

	public void setDynamic(Map<String, String> dynamic) {
		this.dynamic = dynamic;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("mapid=" + mapid);
		sb.append(" mapname=" + mapname);
		sb.append(" mapvalue=" + mapvalue);
		sb.append(" requestid=" + requestid);
		sb.append(" dynamic=" + dynamic);
		return sb.toString();
	}
}
