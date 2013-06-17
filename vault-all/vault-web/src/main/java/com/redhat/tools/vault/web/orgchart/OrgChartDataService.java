package com.redhat.tools.vault.web.orgchart;

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
import javax.persistence.EntityManager;

import org.jboss.logging.Logger;

/**
 * @author cdou
 * 
 */
@ApplicationScoped
public class OrgChartDataService {
    @Inject
    @Vdb
    private EntityManager em;

    @Inject
    private Logger log;

    private final Map<Integer, User> orgChartUsersMap = new ConcurrentHashMap<>();

    public void loadOrgChartUsers() {
        orgChartUsersMap.clear();
        @SuppressWarnings("unchecked")
        List<User> orgChartUsers = em.createQuery("select ocu from User ocu where ocu.specialUser = false").getResultList();

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
            members = new HashMap<>();
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