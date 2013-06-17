package com.redhat.tools.vault.web.orgchart;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "User", schema = "OrgChart2S")
public class User implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3698267147401665439L;

    @Id
    @NotNull
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private int userId;

    @NotNull
    @Column(name = "uid")
    private String userUid;

    @NotNull
    @Column(name = "realname")
    private String userName;

    @Column(name = "manager", nullable = true)
    private Integer manager;

    @Column(name = "disabled")
    private boolean disabled;

    @Column(name = "special_user")
    private boolean specialUser;

    @Column(name = "group_id")
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

    @Transient
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
