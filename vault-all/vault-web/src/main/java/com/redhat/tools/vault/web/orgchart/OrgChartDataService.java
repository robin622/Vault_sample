package com.redhat.tools.vault.web.orgchart;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import com.redhat.tools.vault.bean.User;

/**
 * @author cdou
 * 
 */
@ApplicationScoped
public class OrgChartDataService {

    Connection conn = null;

    public OrgChartDataService() {
        try {
            Class.forName("org.teiid.jdbc.TeiidDriver");
            conn = DriverManager.getConnection("jdbc:teiid:EngVDBF@mms://vdb.engineering.redhat.com:31000", "teiid", "teiid");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Inject
    private Logger log;

    private final Map<Integer, User> orgChartUsersMap = new ConcurrentHashMap<Integer, User>();

    public void loadOrgChartUsers() {
        List<User> orgChartUsers = new ArrayList<User>();
        try {
            orgChartUsersMap.clear();
            String sql = "select u.id,u.uid,u.realname,u.manager,u.disabled,u.special_user,u.group_id from OrgChart2S.User u";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setUserId(resultSet.getInt(1));
                user.setUserUid(resultSet.getString(2));
                user.setUserName(resultSet.getString(3));
                user.setManager(resultSet.getInt(4));
                user.setDisabled(resultSet.getBoolean(5));
                user.setSpecialUser(resultSet.getBoolean(6));
                user.setGroupId(resultSet.getInt(7));
                orgChartUsers.add(user);
            }
            for (User orgChartUser : orgChartUsers) {
                orgChartUsersMap.put(orgChartUser.getUserId(), orgChartUser);
            }

            for (Entry<Integer, User> ent : orgChartUsersMap.entrySet()) {
                User orgChartUser = ent.getValue();
                User manager = null;
                if (orgChartUser.getManager() != null) {
                    manager = orgChartUsersMap.get(orgChartUser.getManager());
                }

                if (null != manager) {
                    addMember(manager, orgChartUser);
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("OrgChart User Map is : " + orgChartUsersMap);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public Map<Integer, User> getAllOrgChartUsers() {
        return orgChartUsersMap;
    }

    public RoleType getRole(String userName) {
        User user = getUser(userName);
        if (null == user) {
            return RoleType.GUEST;
        } else {
            int groupId = user.getGroupId();
            if (log.isDebugEnabled()) {
                log.debug("roleId of user: " + userName + " is " + groupId);
            }
            if (528 == groupId || 441 == groupId || 529 == groupId || 627 == groupId || 531 == groupId || 530 == groupId
                    || 532 == groupId) {
                return RoleType.HSSUSER;
            } else if (481 == groupId) {
                return RoleType.AGENT;
            }
            return RoleType.GUEST;
        }
    }

    public void addMember(User manager, User member) {
        Map<String, User> members = manager.getMembers();
        if (null == members) {
            members = new HashMap<String, User>();
            manager.setMembers(members);
        }
        if (manager.getUserId() != member.getUserId()) {
            members.put(member.getUserUid(), member);
        }
    }

    public void removeMember(User manager, String member) {
        Map<String, User> members = manager.getMembers();
        if (null != members) {
            members.remove(member);
        }
    }

    public int getTotal(User user) {
        int total = user.isDisabled() ? 0 : 1;
        Map<String, User> members = user.getMembers();
        if (null != members) {
            for (String memberName : members.keySet()) {
                total += getTotal(members.get(memberName));
            }
        }
        return total;
    }

    public Set<String> getAllMembers(String userName) {
        User user = getUser(userName);
        return getAllMembers(user);
    }

    public Set<String> getAllMembers(User user) {
        if (user == null) {
            return Collections.emptySet();
        }
        Set<String> allMembers = new HashSet<String>();

        try {
            Integer.parseInt(user.getUserUid());
        } catch (NumberFormatException e) {
            allMembers.add(user.getUserUid());
        }
        Map<String, User> members = user.getMembers();
        if (members != null) {
            for (User member : members.values()) {
                allMembers.addAll(getAllMembers(member));
            }
        }
        return allMembers;
    }

    public User getUser(int userId) {
        return orgChartUsersMap.get(userId);
    }

    public User getUser(String userName) {
        for (User user : orgChartUsersMap.values()) {
            if (user.getUserUid().equalsIgnoreCase(userName)) {
                return user;
            }
        }
        return null;
    }

}