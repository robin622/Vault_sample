package com.redhat.tools.vault.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.jboss.logging.Logger;

import com.redhat.tools.vault.bean.Product;
import com.redhat.tools.vault.bean.Request;
import com.redhat.tools.vault.bean.RequestMap;
import com.redhat.tools.vault.bean.RequestRelationship;
import com.redhat.tools.vault.bean.Version;
import com.redhat.tools.vault.util.DateUtil;
import com.redhat.tools.vault.util.StringUtil;

/**
 * @author wezhao
 */
public class RequestDAO {

    /** The logger. */
    protected static final Logger log = Logger.getLogger(RequestDAO.class);

    DAOFactory dao = null;

    Session session = null;

    private static final String QUERY_SIGNEDREQ = "from Request as a where (a.owner like ? or a.owner like ?) and a.requestid in(select distinct b.requestid from RequestHistory b where b.editedby = ? and (b.status = ? or b.status = ? or b.status = ? or b.status = ?) and b.isHistory = 0 ) order by requestid desc";
    private static final String QUERY_WAITINGREQ = "from Request as a where (a.owner like ? or a.owner like ?) and a.status = ? and a.requestid not in(select distinct b.requestid from RequestHistory b where b.editedby = ? and (b.status = ? or b.status = ? or b.status = ?) and b.isHistory = 0 ) order by requestid desc";
    private static final String QUERY_CANVIEWREQ = "from Request as a where a.is_public = 1 or a.createdby = ? or (a.owner like ? or a.owner like ?) or (a.forward like ? or a.forward like ?) order by requestid desc";
    private static final String QUERY_CCTOMEREQ = "from Request as a where (a.forward like ? or a.forward like ?) order by requestid desc";

    public RequestDAO() {
        dao = DAOFactory.getInstance();
    }

    public List<Request> get(Request condition) {
        Session sess = null;
        try {
            sess = dao.getSession();
            Criteria criteria = sess.createCriteria(Request.class);
            if (condition.getRequestid() != null) {
                criteria.add(Expression.eq(Request.PROPERTY_REQUESTID, new Long(condition.getRequestid())));
            }
            if (condition.getRequestname() != null) {
                criteria.add(Expression.eq(Request.PROPERTY_REQUESTNAME, condition.getRequestname()));
            }
            if (condition.getValue() != null) {
                criteria.add(Expression.eq(Request.PROPERTY_VALUE, condition.getValue()));
            }
            if (condition.getStatus() != null) {
                criteria.add(Expression.eq(Request.PROPERTY_STATUS, condition.getStatus()));
            }
            if (condition.getCreatedby() != null) {
                criteria.add(Expression.eq(Request.PROPERTY_CREATEDBY, condition.getCreatedby()));
            }
            if (condition.getOwner() != null) {
                criteria.add(Expression.eq(Request.PROPERTY_OWNER, condition.getOwner()));
            }
            if (condition.getVersionid() != null) {
                criteria.add(Expression.eq(Request.PROPERTY_VERSIONID, condition.getVersionid()));
            }
            if (condition.getIs_public() != null) {
                criteria.add(Expression.eq(Request.PROPERTY_IS_PUBLIC, condition.getIs_public()));
            }
            if (condition.getForward() != null) {
                criteria.add(Expression.eq(Request.PROPERTY_CC_LIST, condition.getForward()));
            }
            if (condition.getFrom() != null) {
                criteria.add(Expression.eq(Request.PROPERTY_FROM, condition.getFrom()));
            }
            if (condition.getRequestVersion() != null) {
                criteria.add(Expression.eq(Request.PROPERTY_REQUEST_VERSION, condition.getRequestVersion()));
            }
            if (condition.getRequesttime() != null) {
                criteria.add(Expression.ge(Request.PROPERTY_REQUESTTIME, condition.getRequesttime()));
                criteria.add(Expression.lt(Request.PROPERTY_REQUESTTIME, DateUtil.dateTomorrow(condition.getRequesttime())));
            }
            // criteria.addOrder(Order.desc((Request.PROPERTY_EDITEDTIME)));
            criteria.addOrder(Order.desc((Request.PROPERTY_REQUESTID)));
            List<Request> list = criteria.list();
            if (list != null && list.size() > 0) {
                for (Request s : list) {
                    log.debug("Request=" + s);
                    String detail = s.getDetail();
                    detail = StringUtil.escapeHTMLForDesc(detail);
                    s.setDetail(detail);
                    String forward = s.getForward();
                    s.setForward(StringUtil.formartCCList(forward));
                }
            }

            return list;
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (sess != null) {
                sess.close();
            }
        }
        return null;
    }

    private List<Request> getPeriodCreatedRequestPublic(String username, Date start, Date end) {
        Session sess = null;
        try {
            sess = dao.getSession();
            Criteria criteria = sess.createCriteria(Request.class);
            if (username != null && username.trim().length() != 0) {
                criteria.add(Expression.eq(Request.PROPERTY_CREATEDBY, username));
            }
            if (start != null) {
                criteria.add(Expression.gt(Request.PROPERTY_CREATEDTIME, start));
            }
            if (end != null) {
                criteria.add(Expression.lt(Request.PROPERTY_CREATEDTIME, end));
            }
            if ((username == null || username.trim().length() == 0) && start == null && end == null) {
                return findAll();
            }
            criteria.addOrder(Order.desc((Request.PROPERTY_CREATEDTIME)));
            List<Request> list = criteria.list();
            if (list != null && list.size() > 0) {
                for (Request s : list) {
                    log.debug("Request=" + s);
                    String detail = s.getDetail();
                    detail = StringUtil.escapeHTMLForDesc(detail);
                    s.setDetail(detail);
                    String forward = s.getForward();
                    s.setForward(StringUtil.formartCCList(forward));
                }
            }
            return list;
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (sess != null) {
                sess.close();
            }
        }
        return null;
    }

    public List<Request> getPeriodCreatedRequest(String username, Date start, Date end) {
        return getPeriodCreatedRequestPublic(username, start, end);
    }

    public List<Request> getPeriodCreatedRequestOfOthers(String user, Date start, Date end, String currentuser) {
        List<Request> list = getPeriodCreatedRequestPublic(user, start, end);
        List<Request> list_ready = new ArrayList<Request>();
        Iterator<Request> iter = list.iterator();
        while (iter.hasNext()) {
            Request request = iter.next();
            if (Request.IS_PUBLIC.equals(request.getIs_public()) || request.getCreatedby().contains(currentuser)
                    || request.getOwner().contains(currentuser) || request.getForward().contains(currentuser)) {
                list_ready.add(request);
            }
        }
        return list_ready;
    }

    private List<Request> getPeriodSignedRequestPublic(String username, Date start, Date end) {
        Session sess = null;
        try {
            sess = dao.getSession();
            Criteria criteria = sess.createCriteria(Request.class);

            if (username != null && username.trim().length() != 0) {
                List<Request> requests = null;
                StringBuffer queryStringBuffer = new StringBuffer("from Request as a where ");
                if (start != null || end != null) {
                    if (start != null) {
                        queryStringBuffer.append("a.signedtime > ?");
                        if (end != null) {
                            queryStringBuffer.append(" and a.signedtime < ?");
                        }
                    } else {
                        if (end != null) {
                            queryStringBuffer.append("a.signedtime < ?");
                        }
                    }
                    queryStringBuffer
                            .append(" and a.requestid in(select b.requestid from RequestHistory b where b.editedby=? and (b.status = ? or b.status = ? or b.status = ?) and b.isHistory = 0 )");
                } else {
                    queryStringBuffer
                            .append("a.requestid in(select b.requestid from RequestHistory b where b.editedby=? and (b.status = ? or b.status = ? or b.status = ?) and b.isHistory = 0 )");
                }
                queryStringBuffer.append(" order by a.signedtime,a.editedtime");
                Query queryObject;
                int i = 0;
                try {
                    queryObject = sess.createQuery(queryStringBuffer.toString());

                    if (start != null) {
                        queryObject.setDate(i++, start);
                    }
                    if (end != null) {
                        queryObject.setDate(i++, end);
                    }
                    queryObject.setString(i++, username);
                    queryObject.setString(i++, Request.SIGNED_BY);
                    queryObject.setString(i++, Request.SIGNED);
                    queryObject.setString(i++, Request.SIGNED_ONBEHALF);
                    requests = queryObject.list();
                    if (requests != null && requests.size() > 0) {
                        for (Request s : requests) {
                            Hibernate.initialize(s.getMaps());
                            Hibernate.initialize(s.getRelations());
                        }
                    }
                    return requests;
                } catch (Exception e) {
                    log.error(e.getMessage());
                    return null;
                }

            } else {
                List<Request> requests = null;
                StringBuffer queryStringBuffer = new StringBuffer("from Request as a where ");
                if (start != null || end != null) {
                    if (start != null) {
                        queryStringBuffer.append("a.signedtime > ?");
                        if (end != null) {
                            queryStringBuffer.append(" and a.signedtime < ?");
                        }
                    } else {
                        if (end != null) {
                            queryStringBuffer.append("a.signedtime < ?");
                        }
                    }
                    queryStringBuffer.append(" or ");
                    if (start != null) {
                        queryStringBuffer.append("(a.editedtime > ?");
                        if (end != null) {
                            queryStringBuffer.append(" and a.editedtime < ?");
                        }
                        queryStringBuffer.append(" and a.signedtime is null)");
                    } else {
                        if (end != null) {
                            queryStringBuffer.append("(a.editedtime < ?");
                        }
                        queryStringBuffer.append(" and a.signedtime is null)");
                    }

                } else {
                    queryStringBuffer.append(" 1=1 ");
                }
                queryStringBuffer.append(" order by a.signedtime,a.editedtime");
                // String queryString = (a.signedtime between ? and ?) or (a.editedtime between ? and ?)";
                Query queryObject;
                int i = -1;
                try {
                    queryObject = sess.createQuery(queryStringBuffer.toString());
                    if (start != null) {
                        queryObject.setDate(++i, start);
                    }
                    if (end != null) {
                        queryObject.setDate(++i, end);
                    }
                    if (start != null) {
                        queryObject.setDate(++i, start);
                    }
                    if (end != null) {
                        queryObject.setDate(++i, end);
                    }
                    requests = queryObject.list();
                    if (requests != null && requests.size() > 0) {
                        for (Request s : requests) {
                            Hibernate.initialize(s.getMaps());
                            Hibernate.initialize(s.getRelations());
                        }
                    }
                    return requests;
                } catch (Exception e) {
                    log.error(e.getMessage());
                    return null;
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        } finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    public List<Request> getPeriodSignedRequest(String username, Date start, Date end) {
        return getPeriodSignedRequestPublic(username, start, end);
    }

    public List<Request> getPeriodSignedRequestOfOthers(String user, Date start, Date end, String currentuser) {
        List<Request> list = getPeriodSignedRequestPublic(user, start, end);
        List<Request> list_ready = new ArrayList<Request>();
        Iterator<Request> iter = list.iterator();
        while (iter.hasNext()) {
            Request request = iter.next();
            if (Request.IS_PUBLIC.equals(request.getIs_public()) || request.getCreatedby().contains(currentuser)
                    || request.getOwner().contains(currentuser) || request.getForward().contains(currentuser)) {
                list_ready.add(request);
            }
        }
        return list_ready;
    }

    public Request update(Request request) {
        Request updatedRequest = null;
        Transaction trans = null;
        Session sess = null;
        try {
            log.debug("update. request=" + request);
            sess = dao.getSession();
            // trans = sess.beginTransaction();
            updatedRequest = (Request) sess.load(Request.class, new Long(request.getRequestid()));
            log.debug("original request=" + updatedRequest);
            if (request.getRequestname() != null) {
                updatedRequest.setRequestname(request.getRequestname());
            }
            if (request.getSummary() != null) {
                updatedRequest.setSummary(request.getSummary());
            }
            if (request.getDetail() != null) {
                updatedRequest.setDetail(request.getDetail());
            }
            if (request.getOwner() != null) {
                updatedRequest.setOwner(request.getOwner());
            }
            if (request.getRequesttime() != null) {
                updatedRequest.setRequesttime(request.getRequesttime());
            }
            if (request.getSignedtime() != null) {
                updatedRequest.setSignedtime(request.getSignedtime());
            }
            if (request.getSignedby() != null) {
                updatedRequest.setSignedby(request.getSignedby());
            }
            if (request.getStatus() != null) {
                updatedRequest.setStatus(request.getStatus());
            }
            if (request.getValue() != null) {
                updatedRequest.setValue(request.getValue());
            }
            if (request.getEditedby() != null) {
                updatedRequest.setEditedby(request.getEditedby());
            }
            if (request.getEditedtime() != null) {
                updatedRequest.setEditedtime(request.getEditedtime());
            }
            if (request.getComment() != null) {
                updatedRequest.setComment(request.getComment());
            }
            if (request.getIs_public() != null) {
                updatedRequest.setIs_public(request.getIs_public());
            }
            if (request.getVersionid() != null) {
                updatedRequest.setVersionid(request.getVersionid());
            }
            if (request.getMaps() != null) {
                updatedRequest.setMaps(request.getMaps());
            }
            if (request.getForward() != null) {
                String forward = StringUtil.deFormartCCList(request.getForward());
                updatedRequest.setForward(request.getForward());
            }
            if (request.getRelations() != null) {
                updatedRequest.setRelations(request.getRelations());
            }
            if (request.getRequestVersion() != null) {
                updatedRequest.setRequestVersion(request.getRequestVersion());
            }
            sess.update(updatedRequest);
            // trans.commit();
            // trans = null;
            sess.flush();
        } catch (Exception e) {
            log.error(e.getMessage());
            if (trans != null) {
                trans.rollback();
            }
        } finally {
            if (sess != null) {
                sess.close();
            }
        }
        return updatedRequest;
    }

    public Long save(Request request) throws Exception {
        Transaction trans = null;
        Session sess = null;
        Long id = null;
        try {
            log.debug("save. request=" + request);
            sess = dao.getSession();
            // trans = sess.beginTransaction();
            id = (Long) sess.save(request);
            log.debug("sess success");
            // trans.commit();
            trans = null;
            // sess.flush();
            log.debug("transaction commit success");
        } catch (HibernateException e) {
            log.error(e.getMessage());
            throw e;
        } catch (IllegalStateException ie) {
            log.error(ie.getMessage());
        } finally {
            try {
                if (trans != null) {
                    trans.rollback();
                }
                dao.closeSession(sess);
            } catch (Exception e) {
                log.error(e.getMessage());
                throw e;
            }
        }
        return id;
    }

    public void deleteRequest(Request condition) throws Exception {
        Request deletedRequest = null;
        Transaction trans = null;
        try {
            session = dao.getSession();
            // trans = session.beginTransaction();
            deletedRequest = (Request) session.load(Request.class, new Long(condition.getRequestid()));
            if (condition.getRequestid() != null) {
                deletedRequest.setRequestid(condition.getRequestid());
            }
            session.delete(deletedRequest);
            // trans.commit();
            trans = null;
            session.flush();
        } catch (HibernateException e) {
            log.error(e.getMessage());
            throw e;
        } catch (IllegalStateException ie) {
            log.error(ie.getMessage());
        } finally {
            try {
                if (trans != null) {
                    trans.rollback();
                }
                dao.closeSession(session);
            } catch (Exception e) {
                log.error(e.getMessage());
                throw e;
            }
        }
    }

    public List<Request> getAllByStatus(String userEmail, String userName, String operation) throws Exception {
        Request condition = new Request();
        List<Request> allRequests = null;
        // allRequests = get(condition);
        Session sess = null;

        try {
            sess = dao.getSession();
        } catch (Exception re) {
            log.error("Create session failed", re);
        }

        allRequests = findAll(sess);
        List<Request> list1 = new ArrayList<Request>();
        if (allRequests != null && allRequests.size() > 0) {
            String owner = null;
            String itemStatus = null;
            String[] ownerstr = null;
            String creater = null;
            for (Request request : allRequests) {
                Hibernate.initialize(request.getMaps());
                // Hibernate.initialize(request.getRelations());
                creater = request.getCreatedby();
                if (creater != null && creater.equals(userName)) {
                    request.setCanEdit("canEdit");
                }
                itemStatus = request.getStatus();
                owner = request.getOwner();
                if (operation != null && !"".equals(operation)) {
                    if ("canView".equals(operation)) {
                        if (request.getIs_public() == 1) {
                            if (!list1.contains(request)) {
                                list1.add(request);
                            }
                        }
                        if (creater != null && creater.equals(userName)) {
                            if (!list1.contains(request)) {
                                list1.add(request);
                            }
                        }
                    } else if ("waitingSignOff".equals(operation)) {
                        if (itemStatus != null && !Request.INACTIVE.equals(itemStatus)) {
                            if (owner != null) {
                                ownerstr = owner.split(",");
                                for (int j = 0; j < ownerstr.length; j++) {
                                    if (userEmail.equals(ownerstr[j])) {
                                        if (!list1.contains(request)) {
                                            list1.add(request);
                                        }
                                    }
                                }
                            }
                        }
                    } else if ("beenSignOffed".equals(operation)) {
                        if (owner != null) {
                            ownerstr = owner.split(",");
                            for (int j = 0; j < ownerstr.length; j++) {
                                if (userEmail.equals(ownerstr[j])) {
                                    if (!list1.contains(request)) {
                                        list1.add(request);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (sess != null) {
            sess.close();
        }

        return list1;
    }

    public List<Request> getSignedOff(String userName, String userEmail) {
        log.debug("finding all Log instances");
        Session sess = null;
        try {
            sess = dao.getSession();
        } catch (Exception re) {
            log.error("Create session failed", re);
        }
        List<Request> requests = null;
        try {
            Query queryObject;
            try {
                queryObject = sess.createQuery(QUERY_SIGNEDREQ);
                queryObject.setString(0, "%," + userEmail + "%");
                queryObject.setString(1, userEmail + "%");
                queryObject.setString(2, userName);
                queryObject.setString(3, Request.SIGNED);
                queryObject.setString(4, Request.REJECTED);
                queryObject.setString(5, Request.WITHDRAW);
                queryObject.setString(6, Request.SIGNED_BY);
                requests = queryObject.list();
                if (requests != null && requests.size() > 0) {
                    for (Request s : requests) {
                        Hibernate.initialize(s.getMaps());
                        Hibernate.initialize(s.getRelations());
                        // log.debug("flow=" + s);
                    }
                }
                return requests;
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        } finally {
            if (sess != null) {
                sess.close();
            }
        }

        return null;
    }

    public List<Request> getWaitRequests(String userName, String userEmail) {
        log.debug("finding all Log instances");
        Session sess = null;
        try {
            sess = dao.getSession();
        } catch (Exception re) {
            log.error("Create session failed", re);
        }
        List<Request> requests = null;
        try {
            Query queryObject;
            try {
                queryObject = sess.createQuery(QUERY_WAITINGREQ);
                queryObject.setString(0, "%," + userEmail + "%");
                queryObject.setString(1, userEmail + "%");
                queryObject.setString(2, Request.ACTIVE);
                queryObject.setString(3, userName);
                queryObject.setString(4, Request.SIGNED);
                queryObject.setString(5, Request.SIGNED_BY);
                queryObject.setString(6, Request.REJECTED);
                requests = queryObject.list();
                if (requests != null && requests.size() > 0) {
                    for (Request s : requests) {
                        Hibernate.initialize(s.getMaps());
                        Hibernate.initialize(s.getRelations());
                        // log.debug("flow=" + s);
                    }
                }
                return requests;
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        } finally {
            if (sess != null) {
                sess.close();
            }
        }

        return null;
    }

    public List<Request> getCanView(String userName, String userEmail) {
        log.debug("finding all Log instances");
        Session sess = null;
        try {
            sess = dao.getSession();
        } catch (Exception re) {
            log.error("Create session failed", re);
        }
        List<Request> requests = null;
        try {
            Query queryObject;
            try {
                queryObject = sess.createQuery(QUERY_CANVIEWREQ);
                queryObject.setString(0, userName);
                queryObject.setString(1, userEmail + "%");
                queryObject.setString(2, "%," + userEmail + "%");
                queryObject.setString(3, userEmail + "%");
                queryObject.setString(4, "%," + userEmail + "%");
                requests = queryObject.list();
                if (requests != null && requests.size() > 0) {
                    for (Request s : requests) {
                        // Hibernate.initialize(s.getMaps());
                        // Hibernate.initialize(s.getRelations());
                        // log.debug("flow=" + s);
                    }
                }
                return requests;
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        } finally {
            if (sess != null) {
                sess.close();
            }
        }

        return null;
    }

    public List<Request> getCCToMe(String userName, String userEmail) {
        log.debug("finding all Log instances");
        Session sess = null;
        try {
            sess = dao.getSession();
        } catch (Exception re) {
            log.error("Create session failed", re);
        }
        List<Request> requests = null;
        try {
            Query queryObject;
            try {
                queryObject = sess.createQuery(QUERY_CCTOMEREQ);
                queryObject.setString(0, "%" + userEmail + "%");
                queryObject.setString(1, userEmail + "%");
                requests = queryObject.list();
                if (requests != null && requests.size() > 0) {
                    for (Request s : requests) {
                        Hibernate.initialize(s.getMaps());
                        Hibernate.initialize(s.getRelations());
                    }
                }
                return requests;
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        } finally {
            if (sess != null) {
                sess.close();
            }
        }

        return null;
    }

    public List<Request> getAllByStatus(List<Request> allRequests, String userEmail, String userName, String operation)
            throws Exception {
        List<Request> list = new ArrayList<Request>();
        if (allRequests != null && allRequests.size() > 0) {
            String owner = null;
            String itemStatus = null;
            String[] ownerstr = null;
            String creater = null;
            for (Request request : allRequests) {
                // Hibernate.initialize(request.getMaps());
                // Hibernate.initialize(request.getRelations());
                creater = request.getCreatedby();
                if (creater != null && creater.equals(userName)) {
                    request.setCanEdit("canEdit");
                }
                itemStatus = request.getStatus();
                owner = request.getOwner();
                if (operation != null && !"".equals(operation)) {
                    if ("canView".equals(operation)) {
                        if (request.getIs_public() == 1) {
                            if (!list.contains(request)) {
                                list.add(request);
                            }
                        } else if (creater != null && creater.equals(userName)) {
                            if (!list.contains(request)) {
                                list.add(request);
                            }
                        } else if (isInForward(userEmail, request) || isInSignOff(userEmail, request)) {
                            list.add(request);
                        }
                    } else if ("waitingSignOff".equals(operation)) {
                        if (itemStatus != null && !Request.INACTIVE.equals(itemStatus)) {
                            if (owner != null) {
                                ownerstr = owner.split(",");
                                for (int j = 0; j < ownerstr.length; j++) {
                                    if (userEmail.equals(ownerstr[j])) {
                                        if (!list.contains(request)) {
                                            list.add(request);
                                        }
                                    }
                                }
                            }
                        }
                    } else if ("beenSignOffed".equals(operation)) {
                        if (owner != null) {
                            ownerstr = owner.split(",");
                            for (int j = 0; j < ownerstr.length; j++) {
                                if (userEmail.equals(ownerstr[j])) {
                                    if (!list.contains(request)) {
                                        list.add(request);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return list;
    }

    public List<Request> searchByFlowName(String requestName, String userName, String userEmail) {
        log.debug("finding all Log instances");
        Session sess = null;
        try {
            sess = dao.getSession();
        } catch (Exception re) {
            log.error("Create session failed", re);
        }
        List<Request> requests = null;
        try {
            // String queryString =
            // "from Request as a where a.requestname like ? and (a.createdby = ? or a.is_public = 1)";
            String queryString = "from Request as a where a.requestname like ? and (a.createdby = ? or a.is_public = 1 or a.owner like ? or a.forward like ?)";
            Query queryObject;
            try {
                queryObject = sess.createQuery(queryString);
                queryObject.setString(0, "%" + requestName + "%");
                queryObject.setString(1, userName);
                queryObject.setString(2, "%" + userEmail + "%");
                queryObject.setString(3, "%" + userEmail + "%");
                requests = queryObject.list();
                if (requests != null && requests.size() > 0) {
                    for (Request s : requests) {
                        Hibernate.initialize(s.getMaps());
                        Hibernate.initialize(s.getRelations());
                        // log.debug("flow=" + s);
                    }
                }
                return requests;
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        } finally {
            if (sess != null) {
                sess.close();
            }
        }

        return null;
    }

    public List<Request> advanceSearch(String requestName, String creator, String versionid, String productid, String status,
            String owneremail, String userName, String userEmail) {
        // //bug:638039, select * from VA_request where versionid in (select id
        // from versions where product_id = 141) //if versionid = -1 and
        // productid != -1
        log.debug("finding all Log instances");
        Session sess = null;
        try {
            sess = dao.getSession();
        } catch (Exception re) {
            log.error("Create session failed", re);
        }
        List<Request> requests = null;
        try {
            String queryString = "from Request as a where 1=1";
            Query queryObject;
            int i = -1;

            if (requestName != null && !"".equals(requestName)) {
                queryString += " and a.requestname like ?";
            }
            if (creator != null && !"".equals(creator)) {
                queryString += " and a.createdby like ?";
            }
            if (versionid != null && !"-1".equals(versionid)) {
                queryString += " and a.versionid = ?";
            }
            if (productid != null && !"-1".equals(productid)) {// bug:638039
                queryString += " and a.versionid in (select v.id from Version as v where v.product_id = ?) ";
            }
            if (status != null && !"-1".equals(status)) {
                queryString += " and a.status = ?";
            }
            if (owneremail != null && !"".equals(owneremail)) {
                queryString += " and a.owner like ?";
            }
            queryString += " and (a.createdby = ? or a.is_public = 1 or a.owner like ? or a.forward like ?)";
            queryString += " order by requestid desc";
            queryObject = sess.createQuery(queryString);
            if (requestName != null && !"".equals(requestName)) {
                queryObject.setString(++i, "%" + replaceSpecialSQLChar(requestName) + "%");
            }
            if (creator != null && !"".equals(creator)) {
                queryObject.setString(++i, "%" + replaceSpecialSQLChar(creator) + "%");
            }
            if (versionid != null && !"-1".equals(versionid)) {
                queryObject.setLong(++i, Long.parseLong(versionid));
            }
            if (productid != null && !"-1".equals(productid)) {
                queryObject.setLong(++i, Long.parseLong(productid));
            }
            if (status != null && !"-1".equals(status)) {
                queryObject.setString(++i, status);
            }
            if (owneremail != null && !"".equals(owneremail)) {
                queryObject.setString(++i, "%" + replaceSpecialSQLChar(owneremail) + "%");
            }
            queryObject.setString(++i, userName);
            queryObject.setString(++i, "%" + userEmail + "%");
            queryObject.setString(++i, "%" + userEmail + "%");
            requests = queryObject.list();
            if (requests != null && requests.size() > 0) {
                for (Request s : requests) {
                    Hibernate.initialize(s.getMaps());
                    Hibernate.initialize(s.getRelations());
                    // log.debug("flow=" + s);
                }
            }
            return requests;

        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (sess != null) {
                sess.close();
            }
        }
        return null;
    }

    private String replaceSpecialSQLChar(String source) {
        // mysql specialchar % _
        return source.replace("%", "\\%").replace("_", "\\_");
    }

    @SuppressWarnings("unchecked")
    public List<Request> findByIds(Long[] requestIds) {
        Session sess = null;
        try {
            sess = dao.getSession();
        } catch (Exception re) {
            log.error("Create session failed", re);
        }
        String inString = "( ";
        for (int i = 0; i < requestIds.length; i++) {
            if (i < requestIds.length - 1) {
                inString += requestIds[i].toString() + ",";
            } else {
                inString += requestIds[i].toString() + " )";
            }
        }
        List<Request> requests = new ArrayList<Request>();
        try {
            String queryString = "from Request as a where a.requestid in " + inString + " order by a.requestid desc";
            Query queryObject;
            queryObject = sess.createQuery(queryString);
            requests = queryObject.list();
            if (requests != null && requests.size() > 0) {
                for (Request s : requests) {
                    Hibernate.initialize(s.getMaps());
                    Hibernate.initialize(s.getRelations());
                }
            }
            return requests;
        } catch (Exception re) {
            log.error("find requests failed", re);
        } finally {
            if (sess != null) {
                sess.close();
            }
        }
        return requests;
    }

    public Request find(Long id) {
        Session sess = null;
        try {
            sess = dao.getSession();
            Request request = (Request) sess.get(Request.class, id);
            /*
             * if(request != null){ Hibernate.initialize(request.getMaps()); Hibernate.initialize(request.getRelations()); }
             */
            return request;

        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (sess != null) {
                sess.close();
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<Request> findAll() {
        Session sess = null;
        List<Request> requests = new ArrayList<Request>();
        try {
            sess = dao.getSession();
            String queryString = "from Request as a order by a.requestid desc";
            Query query = sess.createQuery(queryString);
            requests = query.list();

            if (requests != null && requests.size() > 0) {
                for (Request s : requests) {
                    // Hibernate.initialize(s.getMaps());
                }
            }
            return requests;
        } catch (Exception re) {
            log.error("find requests failed", re);
        } finally {
            if (sess != null) {
                sess.close();
            }
        }
        return requests;
    }

    @SuppressWarnings("unchecked")
    public List<Request> findAll(Session sess) {
        List<Request> requests = new ArrayList<Request>();
        if (sess == null) {
            return requests;
        }

        String queryString = "from Request as a order by a.requestid desc";
        Query query = sess.createQuery(queryString);
        requests = query.list();
        /*
         * if(requests != null && requests.size() > 0){ for (Request s : requests) { Hibernate.initialize(s.getMaps());
         * Hibernate.initialize(s.getChildSet()); } }
         */
        return requests;
    }

    public void saveOrUpdate(Request request) {
        Session sess = null;
        try {
            sess = dao.getSession();
            sess.saveOrUpdate(request);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

    public String[] generateParent(Long requestId) throws Exception {
        Request request = new Request();
        request.setRequestid(requestId);
        List<Request> requests = get(request);
        if (requests != null && requests.size() > 0) {
            request = requests.get(0);
            return generateParent(request);
        }
        return null;
    }

    public String[] generateParent(Request request) throws Exception {
        String[] result = new String[2];
        String parent = "";
        String children = "";
        RequestRelationship condition = new RequestRelationship();
        condition.setRequestId(request.getRequestid());
        condition.setRelationshipId(request.getRequestid());
        condition.setEnable(true);
        RequestRelationshipDAO rDao = new RequestRelationshipDAO();

        List<RequestRelationship> rls = rDao.getRelationShips(condition);
        if (rls != null && rls.size() > 0) {
            for (RequestRelationship r : rls) {
                String rlStatus = Request.ACTIVE;
                String rlRequestName = "";
                Request rlRequest = null;
                if (r.getRequestId().equals(request.getRequestid())) {
                    rlRequest = find(r.getRelationshipId());
                    if (rlRequest != null) {
                        rlStatus = rlRequest.getStatus();
                        rlRequestName = rlRequest.getRequestname();

                    }
                    if (r.getIsParent()) {
                        parent += rlStatus + "##" + r.getRelationshipId().toString() + "  "
                                + StringEscapeUtils.escapeHtml(rlRequestName) + ",";
                    } else {
                        children += rlStatus + "##" + r.getRelationshipId().toString() + "  "
                                + StringEscapeUtils.escapeHtml(rlRequestName) + ",";
                    }
                } else if (r.getRelationshipId().equals(request.getRequestid())) {
                    rlRequest = find(r.getRequestId());
                    if (rlRequest != null) {
                        rlStatus = rlRequest.getStatus();
                        rlRequestName = rlRequest.getRequestname();
                    }
                    if (r.getIsParent()) {
                        children += rlStatus + "##" + r.getRequestId() + "  " + rlRequestName + ",";
                    } else {
                        parent += rlStatus + "##" + r.getRequestId() + "  " + rlRequestName + ",";
                    }
                }

            }
        }

        if (!"".equals(parent)) {
            parent = parent.substring(0, parent.length() - 1);
            // latest parent
            // parent = parent.split(",")[0];
        }
        if (!"".equals(children)) {
            children = children.substring(0, children.length() - 1);
        }

        result[0] = parent;
        result[1] = children;
        return result;
    }

    public String compare(Request current, Request change) throws Exception {
        String result = "";
        if (current.getEditedby() != null) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            result += "Changed by:      " + change.getEditedby() + "[" + format.format(change.getEditedtime()) + "]" + "<br>";
        }
        if (!current.getRequestname().equals(change.getRequestname())) {
            result += "Request name:    changed from " + current.getRequestname() + " to " + change.getRequestname() + "<br>";
        }

        String productCompare = compareProduct(current, change);
        if (productCompare != null) {
            result += productCompare;
        }

        if (current.getRequesttime().compareTo(change.getRequesttime()) == -1) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            result += "Due date:        changed from " + format.format(current.getRequesttime()) + " to "
                    + format.format(change.getRequesttime()) + "<br>";
        }

        if (current.getIs_public().intValue() != change.getIs_public().intValue()) {
            result += "IsPublic:        changed from \"" + publicString(current.getIs_public()) + "\" to \""
                    + publicString(change.getIs_public()) + "\" <br>";
        }

        String signatoryCompare = compareEmails(current.getOwner(), change.getOwner(), "signatory");
        if (signatoryCompare != null) {
            result += signatoryCompare;
        }

        String notifyCompare = compareNotificationType(current, change);
        if (notifyCompare.length() > 0) {
            result += notifyCompare;
        }

        String currentForward = StringUtil.deFormartCCList(current.getForward());
        String changeForward = StringUtil.deFormartCCList(change.getForward());

        // String ccCompare =
        // compareEmails(current.getForward(),change.getForward(),"forward");
        String ccCompare = compareEmails(currentForward, changeForward, "forward");
        if (ccCompare != null) {
            result += ccCompare;
        }

        String parentCompare = compareParent(current, change);
        if (parentCompare != null) {
            result += parentCompare;
        }

        String childCompare = compareChild(current, change);
        if (childCompare != null) {
            result += childCompare;
        }

        String fileCompare = compareFiles(current, change);
        if (!StringUtil.isEmpty(fileCompare)) {
            result += "<br>";
            result += fileCompare;
        }
        String changeDetail_replace = replaceDetail(change.getDetail());
        if (!current.getDetail().trim().equals(changeDetail_replace.trim())) {
            // String currentDetail =
            // StringUtil.convertToHref(StringUtil.showInEmail(current.getDetail()));
            // String changeDetail =
            // StringUtil.convertToHref(StringUtil.showInEmail(change.getDetail()));
            String currentDetail = StringUtil.showInEmail(current.getDetail());
            String changeDetail = StringUtil.showInEmail(change.getDetail());
            result += "<br>";
            result += "Description:<br><br>changed from: <br> " + currentDetail + "<br><br>to: <br> " + changeDetail
                    + "<br><br>";
        }

        return result;

    }

    private String compareProduct(Request current, Request change) throws Exception {
        if (current.getVersionid() == null && change.getVersionid() == null) {
            return null;
        }
        VersionDAO vDao = new VersionDAO();
        ProductDAO pDao = new ProductDAO();
        Version changeVersion = null;
        Product changeProduct = null;
        Version currVersion = null;
        Product currProduct = null;
        if (change.getVersionid() != null && change.getVersionid() != -1) {
            changeVersion = new Version();
            changeVersion.setId(change.getVersionid());
            changeVersion = vDao.get(changeVersion).get(0);
            changeProduct = new Product();
            changeProduct.setId(changeVersion.getProduct_id());
            changeProduct = pDao.get(changeProduct).get(0);
        }

        if (current.getVersionid() != null && current.getVersionid() != -1) {
            currVersion = new Version();
            currVersion.setId(current.getVersionid());
            currVersion = vDao.get(currVersion).get(0);
            currProduct = new Product();
            currProduct.setId(currVersion.getProduct_id());
            currProduct = pDao.get(currProduct).get(0);
        }

        if (currVersion == null && changeVersion != null) {
            return "Added product:   " + changeProduct.getName() + "(version:" + changeVersion.getValue() + ") <br>";
        }
        if (currVersion != null && changeVersion == null) {
            return "Removed product: " + currProduct.getName() + "(version:" + currVersion.getValue() + ") <br>";
        }
        if (currVersion != null && changeVersion != null
                && currVersion.getId().longValue() != changeVersion.getId().longValue()) {
            return "Product:   changed from " + currProduct.getName() + "(version:" + currVersion.getValue() + ")" + " to "
                    + changeProduct.getName() + "(version:" + changeVersion.getValue() + ") <br>";
        }
        return null;
    }

    private String publicString(int value) {
        if (value == Request.IS_PUBLIC) {
            return "Share This Request";
        }
        if (value == Request.IS_NOT_PUBLIC) {
            return "Not Share This Request";
        }
        return null;
    }

    private String compareEmails(String current, String change, String type) {
        String changeName = "Signatory list:  ";
        if (type.equals("forward")) {
            changeName = "CC list:         ";
            if (StringUtil.isEmpty(current) && StringUtil.isEmpty(change)) {
                return null;
            }
            if (!StringUtil.isEmpty(current) && StringUtil.isEmpty(change)) {
                return "remove " + current + " <br> ";
            }
            if (StringUtil.isEmpty(current) && !StringUtil.isEmpty(change)) {
                return "add " + change + " <br> ";
            }
        }
        String[] currArray = current.split(",");
        String[] changeArray = change.split(",");

        if (currArray.length != changeArray.length) {
            return changeName + "changed from " + current + " to " + change + " <br>";
        }

        int count = 0;
        for (int i = 0; i < currArray.length; i++) {
            for (int j = 0; j < changeArray.length; j++) {
                if (currArray[i].equals(changeArray[j])) {
                    count++;
                }
            }
        }
        if (count < currArray.length) {
            return changeName + "changed from " + current + " to " + change + " <br>";
        }
        return null;
    }

    private String compareParent(Request current, Request change) throws Exception {
        RequestRelationshipDAO rDao = new RequestRelationshipDAO();
        RequestDAO requestDao = null;

        Long currParentId = null;
        if (!StringUtil.isNull(current.getParent())) {
            currParentId = Long.parseLong(current.getParent());
        }

        // Long changeParentId = rDao.getParentId(change);
        Long changeParentId = null;
        if (!StringUtil.isNull(change.getParent())) {
            changeParentId = Long.parseLong(change.getParent().split("  ")[0]);
        }

        if (currParentId == null && changeParentId == null) {
            return null;
        }
        String parent = "Parent request:  ";
        if (currParentId == null && changeParentId != null) {
            requestDao = new RequestDAO();
            Request changeParent = requestDao.find(changeParentId);
            if (changeParent != null) {
                return parent + "added \"" + changeParentId + "  " + changeParent.getRequestname() + "\" <br>";
            }
        }

        if (currParentId != null && changeParentId == null) {
            requestDao = new RequestDAO();
            Request currParent = requestDao.find(currParentId);
            if (currParent != null) {
                return parent + "removed \"" + currParentId + "  " + currParent.getRequestname() + "\" <br>";
            }
        }

        if (currParentId.longValue() != changeParentId.longValue()) {
            requestDao = new RequestDAO();
            Request currParent = requestDao.find(currParentId);
            Request changeParent = requestDao.find(changeParentId);
            if (currParent != null && changeParent != null) {
                return parent + "changed from \"" + currParentId + "  " + currParent.getRequestname() + "\"   to   \""
                        + changeParentId + "  " + changeParent.getRequestname() + "\" <br>";
            }
        }

        return null;
    }

    private String compareChild(Request current, Request change) throws Exception {
        RequestRelationshipDAO rDao = new RequestRelationshipDAO();
        RequestDAO requestDao = null;
        // List<Long> currChildIdList = rDao.getChildId(current);
        String[] currChildIdList = null;
        List<Long> changeChildIdList = null;
        String childre = "Child requests:  ";
        if (!StringUtil.isEmpty(current.getChildren())) {
            currChildIdList = current.getChildren().split(",");
        }

        if ((currChildIdList == null || currChildIdList.length < 1) && StringUtil.isEmpty(change.getChildren())) {
            return null;
        }
        if ((currChildIdList == null || currChildIdList.length < 1) && !StringUtil.isEmpty(change.getChildren())) {
            return childre + "added \"" + change.getChildren() + "\" <br>";
        }

        String currChildList = "";
        if (currChildIdList != null && currChildIdList.length > 0) {
            requestDao = new RequestDAO();
            int index = 0;
            for (String id : currChildIdList) {
                if (!StringUtil.isEmpty(id)) {
                    String requestName = requestDao.find(Long.parseLong(id)).getRequestname();
                    if (index < currChildIdList.length - 1) {
                        currChildList += id + "  " + requestName + ",";
                    } else {
                        currChildList += id + "  " + requestName;
                    }
                    index++;
                }
            }
        }

        if (StringUtil.isEmpty(change.getChildren())) {
            return childre + "removed \"" + currChildList + "\" <br>";
        }

        String[] childArray = change.getChildren().trim().split(",");
        if (currChildIdList.length != childArray.length) {
            return childre + "changed from \"" + currChildList + "\"   to   \"" + change.getChildren() + "\" <br>";
        }

        int index = 0;
        for (String currId : currChildIdList) {
            for (String child : childArray) {
                String changeChildId = child.split("  ")[0];
                if (Long.parseLong(currId) == Long.parseLong(changeChildId)) {
                    index++;
                }
            }
        }
        if (index != currChildIdList.length) {
            return childre + "changed from \"" + currChildList + "\"   to   \"" + change.getChildren() + "\" <br>";
        }

        return null;
    }

    private String compareNotificationType(Request current, Request change) {
        String result = "";
        Set<RequestMap> changeMaps = change.getMaps();
        if (changeMaps == null || changeMaps.size() < 1) {
            return null;
        }

        Set<RequestMap> maps = current.getMaps();
        if (maps != null && maps.size() > 0) {
            for (RequestMap m : maps) {

                if (m.getMapname().equals(Request.PROPERTY_REQUEST_NOTIFYCATION)
                        && m.getRequestVersion().intValue() == current.getRequestVersion().intValue()) {
                    for (RequestMap cm : changeMaps) {
                        if (cm.getMapname().equals(Request.PROPERTY_REQUEST_NOTIFYCATION)
                                && cm.getRequestVersion().intValue() == change.getRequestVersion().intValue()) {
                            String currentEmail = m.getMapvalue().split(":")[0];
                            String currentType = m.getMapvalue().split(":")[1];
                            String changeEmail = cm.getMapvalue().split(":")[0];
                            String changeType = cm.getMapvalue().split(":")[1];
                            if (currentEmail.equals(changeEmail)) {
                                if (!currentType.equals(changeType)) {
                                    result += "Signatory:       " + currentEmail + ", changed from \""
                                            + getNotificationType(currentType) + "\"  to  \"" + getNotificationType(changeType)
                                            + "\" <br>";
                                }
                            }
                        }

                    }
                }
            }
        } else if (changeMaps != null && changeMaps.size() > 0) {
            for (RequestMap cm : changeMaps) {
                if (cm.getMapname().equals(Request.PROPERTY_REQUEST_NOTIFYCATION)
                        && cm.getRequestVersion().intValue() == change.getRequestVersion().intValue()) {
                    String changeEmail = cm.getMapvalue().split(":")[0];
                    String changeType = cm.getMapvalue().split(":")[1];
                    result += "Signatory:       " + changeEmail + ", changed to  \"" + getNotificationType(changeType)
                            + "\" <br>";
                }
            }
        }

        return result;
    }

    private String getNotificationType(String type) {

        if (type.equals(Request.NOTIFICATION_TYPE_DEFAULT)) {
            return "Default";
        }
        if (type.equals(Request.NOTIFICATION_TYPE_REQUIRED_SIGNOFF)) {
            return "Request for sign off";
        }
        if (type.equals(Request.NOTIFICATION_TYPE_NOTIFY_CHANGE)) {
            return "Notify the change";
        }
        if (type.equals(Request.NOTIFICATION_TYPE_DO_NOTHING)) {
            return "Don't send an email";
        }
        return null;
    }

    private String compareFiles(Request current, Request change) {

        String result = "";
        Set<RequestMap> currMaps = current.getMaps();
        Set<RequestMap> changeMaps = change.getMaps();

        Map<String, String> currFileMap = new HashMap<String, String>();
        Map<String, String> changeFileMap = new HashMap<String, String>();

        if (currMaps != null && currMaps.size() > 0) {
            for (RequestMap m : currMaps) {
                if (m.getRequestVersion().intValue() == current.getRequestVersion().intValue()
                        && m.getMapname().equals(Request.PROPERTY_REQUEST_ATTACHMENT)) {
                    currFileMap.put(m.getMapvalue(), m.getDynamic().get(Request.PROPERTY_ATTACHMENT_MD5));
                }
            }
        }
        if (changeMaps != null && changeMaps.size() > 0) {
            for (RequestMap m : changeMaps) {
                if (m.getRequestVersion().intValue() == change.getRequestVersion().intValue()
                        && m.getMapname().equals(Request.PROPERTY_REQUEST_ATTACHMENT)) {
                    changeFileMap.put(m.getMapvalue(), m.getDynamic().get(Request.PROPERTY_ATTACHMENT_MD5));
                }
            }
        }

        if (currFileMap.isEmpty() && changeFileMap.isEmpty()) {
            return null;
        }
        /*
         * if(!currFileMap.isEmpty() && changeFileMap.isEmpty()){ if(currFileMap.size()>1){ result +=
         * "Following files were deleted: <br> "; }else{ result += "Following file was deleted: <br> "; } result +=
         * convert(currFileMap)+" <br>"; }
         * 
         * if(currFileMap.isEmpty() && !changeFileMap.isEmpty()){ if(changeFileMap.size()>1){ result +=
         * "Following files were added: <br> "; }else{ result += "Following file was added: <br> "; } result +=
         * convert(changeFileMap)+" <br>"; } if(currFileMap.size() != changeFileMap.size()){
         * result+="Attachment was changed from \""+convert (currFileMap)+"\" to \""+convert(changeFileMap)+"\" <br>"; }
         */

        for (String currFileName : currFileMap.keySet()) {
            boolean removed = true;
            for (String changeFileName : changeFileMap.keySet()) {
                if (currFileName.equals(changeFileName)) {
                    removed = false;
                    if (!currFileMap.get(currFileName).equals(changeFileMap.get(changeFileName))) {
                        result += "File: \"" + currFileName + " MD5:" + currFileMap.get(currFileName) + "\" changed to \""
                                + changeFileName + " MD5:" + changeFileMap.get(changeFileName) + "\"<br>";
                    }
                }
            }

            if (removed) {
                result += "File: \"" + currFileName + " MD5:" + currFileMap.get(currFileName) + "\" was deleted <br>";
            }
        }

        // check if has new files
        for (String changeFileName : changeFileMap.keySet()) {
            boolean added = true;
            for (String currFileName : currFileMap.keySet()) {
                if (currFileName.equals(changeFileName)) {
                    added = false;
                }
            }
            if (added) {
                result += "File: \"" + changeFileName + " MD5:" + changeFileMap.get(changeFileName) + "\" was added <br>";
            }
        }

        return result;
    }

    public boolean isInForward(String userEmail, Request request) {
        String forward = request.getForward();
        if (StringUtils.isNotBlank(forward) && forward.indexOf("<br>") > -1) {
            forward = forward.replaceAll("<br>", "");
        }
        if (forward != null) {
            String[] forwardstr = forward.split(",");
            for (int j = 0; j < forwardstr.length; j++) {
                if (userEmail.equals(forwardstr[j])) {
                    return true;
                }
            }
        }
        return false;

    }

    public boolean isInSignOff(String userEmail, Request request) {
        String owner = request.getOwner();
        if (owner != null) {
            String[] ownerstr = owner.split(",");
            for (int j = 0; j < ownerstr.length; j++) {
                if (userEmail.equals(ownerstr[j])) {
                    return true;
                }
            }
        }
        return false;
    }

    private String replaceDetail(String detail) {
        detail = detail.replaceAll("&amp;amp;", "&");
        detail = detail.replaceAll("&amp;", "&");
        detail = detail.replaceAll("&quot;", "\"");
        detail = detail.replaceAll("&nbsp;", " ");
        detail = detail.replaceAll("&copy;", "©");
        detail = detail.replaceAll("&lt;", "<");
        detail = detail.replaceAll("&gt;", ">");
        return detail;
    }

    public Map<String, Long> getRequestCount(String userName, String userEmail) {
        Map<String, Long> counts = new HashMap<String, Long>();
        log.debug("finding all Log instances");
        Session sess = null;
        try {
            sess = dao.getSession();
        } catch (Exception re) {
            log.error("Create session failed", re);
        }

        String queryString = "select count(*) " + QUERY_SIGNEDREQ;
        Query queryObject;
        try {
            // sigend
            queryObject = sess.createQuery(queryString);
            queryObject.setString(0, "%," + userEmail + "%");
            queryObject.setString(1, userEmail + "%");
            queryObject.setString(2, userName);
            queryObject.setString(3, Request.SIGNED);
            queryObject.setString(4, Request.REJECTED);
            queryObject.setString(5, Request.WITHDRAW);
            queryObject.setString(6, Request.SIGNED_BY);
            List<Long> temp = queryObject.list();
            if (temp != null && temp.size() > 0) {
                counts.put("signed", temp.get(0));
            } else {
                counts.put("signed", 0l);
            }
            // waiting
            queryString = "select count(*) " + QUERY_WAITINGREQ;
            queryObject = sess.createQuery(queryString);
            queryObject.setString(0, "%," + userEmail + "%");
            queryObject.setString(1, userEmail + "%");
            queryObject.setString(2, Request.ACTIVE);
            queryObject.setString(3, userName);
            queryObject.setString(4, Request.SIGNED);
            queryObject.setString(5, Request.SIGNED_BY);
            queryObject.setString(6, Request.REJECTED);
            temp = queryObject.list();
            if (temp != null && temp.size() > 0) {
                counts.put("waiting", temp.get(0));
            } else {
                counts.put("waiting", 0l);
            }
            // canview
            queryString = "select count(*) " + QUERY_CANVIEWREQ;
            queryObject = sess.createQuery(queryString);
            queryObject.setString(0, userName);
            queryObject.setString(1, userEmail + "%");
            queryObject.setString(2, "%," + userEmail + "%");
            queryObject.setString(3, userEmail + "%");
            queryObject.setString(4, "%," + userEmail + "%");
            temp = queryObject.list();
            if (temp != null && temp.size() > 0) {
                counts.put("canview", temp.get(0));
            } else {
                counts.put("canview", 0l);
            }
            // cc to me
            queryString = "select count(*) " + QUERY_CCTOMEREQ;
            queryObject = sess.createQuery(queryString);
            queryObject.setString(0, "%," + userEmail + "%");
            queryObject.setString(1, userEmail + "%");
            temp = queryObject.list();
            if (temp != null && temp.size() > 0) {
                counts.put("cc", temp.get(0));
            } else {
                counts.put("cc", 0l);
            }
            // my request
            Request condition = new Request();
            condition.setCreatedby(userName);
            List<Request> myRequests = get(condition);
            if (myRequests != null) {
                counts.put("myrequest", Long.valueOf(myRequests.size()));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (sess != null) {
                sess.close();
            }
        }
        return counts;
    }

}