package com.redhat.tools.vault.bean;

public class RequestRelationship {

	public static String PROPERTY_MAPID = "id";

	public static String PROPERTY_REQUESTID = "requestId";

	public static String PROPERTY_RELATIONSHIPID = "relationshipId";

	public static String PROPERTY_ISPARENT = "isParent";

	public static String PROPERTY_REQUEST_VERSION = "requestVersion";

	public static String PROPERTY_ENABLE = "enable";

	// public static String PROPERTY_REQUEST_NAME = "requestName";
	// public static String PROPERTY_RELATIONSHIP_REQUEST_NAME =
	// "relationshipRequestName";

	private Long id = null;

	private Long requestId = null;

	// private String requestName = null;

	private Long relationshipId = null;

	private Boolean isParent = null;

	// private String relationshipRequestName = null;

	private Integer requestVersion = null; // request version

	private Boolean enable = null;

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Integer getRequestVersion() {
		return requestVersion;
	}

	public void setRequestVersion(Integer requestVersion) {
		this.requestVersion = requestVersion;
	}

	public Boolean getIsParent() {
		return isParent;
	}

	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public Long getRelationshipId() {
		return relationshipId;
	}

	public void setRelationshipId(Long relationshipId) {
		this.relationshipId = relationshipId;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("id=" + id);
		sb.append(" requestId=" + requestId);
		// sb.append(" requestName=" + requestName);
		sb.append(" relationshipId=" + relationshipId);
		sb.append(" isParent=" + isParent);
		sb.append(" requestVersion=" + requestVersion);
		return sb.toString();
	}
}