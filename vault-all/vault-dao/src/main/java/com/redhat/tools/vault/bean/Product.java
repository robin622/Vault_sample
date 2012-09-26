package com.redhat.tools.vault.bean;

/**
 * @author <a href="mailto:speng@redhat.com">Peng Song</a>
 * @version $Revision$
 */
public class Product {

	public static String PROPERTY_ID = "id";

	public static String PROPERTY_NAME = "name";

	public static String PROPERTY_DESCRIPTION = "description";

	public static String PROPERTY_MILESTONEURL = "milestoneurl";

	public static String PROPERTY_VOTESPERUSER = "votesperuser";

	public static String PROPERTY_MAXVOTESPERBUG = "maxvotesperbug";

	public static String PROPERTY_VOTESTOCONFIRM = "votestoconfirm";

	public static String PROPERTY_DEFAULTMILESTONE = "defaultmilestone";

	public static String PROPERTY_DEPENDS = "depends";

	public static String PROPERTY_CLASSIFICATION_ID = "classification_id";

	public static String PROPERTY_ISACTIVE = "isactive";

	private Long id = null;

	private String name = null;

	private String description = null;

	private String milestoneurl = null;

	private Integer votesperuser = null;

	private Integer maxvotesperbug = null;

	private Integer votestoconfirm = null;

	private String defaultmilestone = null;

	private Integer depends = null;

	private Integer classification_id = null;

	private Integer isactive = null;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMilestoneurl() {
		return milestoneurl;
	}

	public void setMilestoneurl(String milestoneurl) {
		this.milestoneurl = milestoneurl;
	}

	public Integer getVotesperuser() {
		return votesperuser;
	}

	public void setVotesperuser(Integer votesperuser) {
		this.votesperuser = votesperuser;
	}

	public Integer getMaxvotesperbug() {
		return maxvotesperbug;
	}

	public void setMaxvotesperbug(Integer maxvotesperbug) {
		this.maxvotesperbug = maxvotesperbug;
	}

	public Integer getVotestoconfirm() {
		return votestoconfirm;
	}

	public void setVotestoconfirm(Integer votestoconfirm) {
		this.votestoconfirm = votestoconfirm;
	}

	public String getDefaultmilestone() {
		return defaultmilestone;
	}

	public void setDefaultmilestone(String defaultmilestone) {
		this.defaultmilestone = defaultmilestone;
	}

	public Integer getDepends() {
		return depends;
	}

	public void setDepends(Integer depends) {
		this.depends = depends;
	}

	public Integer getClassification_id() {
		return classification_id;
	}

	public void setClassification_id(Integer classificationId) {
		classification_id = classificationId;
	}

	public Integer getIsactive() {
		return isactive;
	}

	public void setIsactive(Integer isactive) {
		this.isactive = isactive;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getName()).append("[");
		sb.append("id=" + id);
		sb.append(" ,name=" + name);
		sb.append(" ,description=" + description);
		sb.append(" ,milestoneurl=" + milestoneurl);
		sb.append(" ,votesperuser=" + votesperuser);
		sb.append(" ,maxvotesperbug=" + maxvotesperbug);
		sb.append(" ,votestoconfirm=" + votestoconfirm);
		sb.append(" ,defaultmilestone=" + defaultmilestone);
		sb.append(" ,depends=" + depends);
		sb.append(" ,classification_id=" + classification_id);
		sb.append(" ,isactive=" + isactive);
		sb.append("]");
		return sb.toString();
	}
}
