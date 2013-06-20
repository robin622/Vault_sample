package com.redhat.tools.vault.bean;

import java.io.Serializable;
import java.util.Map;

public class User implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3698267147401665439L;

    private int userId;

    private String userUid;

    private String userName;

    private Integer manager;

    private boolean disabled;

    private boolean specialUser;

    private int groupId;

    /**
     * @return the groupId
     */
    public int getGroupId() {
        return groupId;
    }

    /**
     * @param groupId the groupId to set
     */
    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    private Map<String, User> members;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public Integer getManager() {
        return manager;
    }

    public void setManager(Integer manager) {
        this.manager = manager;
    }

    public boolean isSpecialUser() {
        return specialUser;
    }

    public void setSpecialUser(boolean specialUser) {
        this.specialUser = specialUser;
    }

    public Map<String, User> getMembers() {
        return members;
    }

    public void setMembers(Map<String, User> members) {
        this.members = members;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "OrgChartUser [userId=" + userId + ", userUid=" + userUid + ", userName=" + userName + ", manager=" + manager
                + ", disabled=" + disabled + ", specialUser=" + specialUser + "]";
    }

}
