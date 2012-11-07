package com.redhat.tools.vault.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.jboss.logging.Logger;

import com.redhat.tools.vault.bean.Request;
import com.redhat.tools.vault.bean.RequestRelationship;

public class RequestRelationshipDAO {
	protected static final Logger log = Logger
			.getLogger(RequestRelationshipDAO.class);

	DAOFactory dao = null;

	Session session = null;

	public RequestRelationshipDAO() {
		dao = DAOFactory.getInstance();
	}

	public List<RequestRelationship> get(RequestRelationship condition)
			throws Exception {
		try {
			session = dao.getSession();
			Criteria criteria = session
					.createCriteria(RequestRelationship.class);
			if (condition.getId() != null) {
				criteria.add(Expression.eq(RequestRelationship.PROPERTY_MAPID,
						new Long(condition.getId())));
			}
			if (condition.getRequestId() != null) {
				criteria.add(Expression.eq(
						RequestRelationship.PROPERTY_REQUESTID,
						condition.getRequestId()));
			}
			if (condition.getRelationshipId() != null) {
				criteria.add(Expression.eq(
						RequestRelationship.PROPERTY_RELATIONSHIPID,
						condition.getRelationshipId()));
			}
			if (condition.getIsParent() != null) {
				criteria.add(Expression.eq(
						RequestRelationship.PROPERTY_ISPARENT,
						condition.getIsParent()));
			}

			if (condition.getRequestVersion() != null) {
				criteria.add(Expression.eq(
						RequestRelationship.PROPERTY_REQUEST_VERSION,
						condition.getRequestVersion()));
			}
			if (condition.getEnable() != null) {
				criteria.add(Expression.eq(RequestRelationship.PROPERTY_ENABLE,
						condition.getEnable()));
			}

			/*
			 * if(condition.getRequestName() != null) {
			 * criteria.add(Expression.eq
			 * (RequestRelationship.PROPERTY_REQUEST_NAME,
			 * condition.getRequestName())); }
			 * if(condition.getRelationshipRequestName() != null) {
			 * criteria.add(
			 * Expression.eq(RequestRelationship.PROPERTY_RELATIONSHIP_REQUEST_NAME
			 * , condition.getRelationshipRequestName())); }
			 */
			criteria.addOrder(Order.desc((RequestRelationship.PROPERTY_MAPID)));
			List<RequestRelationship> list = criteria.list();
			for (RequestRelationship s : list) {
				log.debug("RequestRelationship=" + s);
			}
			return list;
		}
		catch (Exception e) {
			log.error(e.getMessage());
		}
		finally {
			if (session != null) {
				session.close();
			}
		}
		return null;
	}

	public List<RequestRelationship> getRelationShips(RequestRelationship condition){
		Session sess = null;
		List<RequestRelationship> ships = new ArrayList<RequestRelationship>();
		try {
			sess = dao.getSession();
			String queryString = "from RequestRelationship as a where (a.requestId = ? or a.relationshipId = ?) and a.enable = true";
			
			Query query = sess.createQuery(queryString);
			query.setLong(0, condition.getRequestId());
			query.setLong(1, condition.getRelationshipId());
			ships = query.list();

			return ships;
		}
		catch (Exception re) {
			log.error("find requests failed", re);
		}
		finally {
			if (sess != null) {
				sess.close();
			}
		}
		return ships;
	}
	
	public RequestRelationship update(RequestRelationship relation) {
		try {
			session = dao.getSession();
			session.saveOrUpdate(relation);
			session.flush();
		}
		catch (Exception e1) {
			log.error(e1.getMessage());
		}
		finally {
			if (session != null) {
				session.close();
			}
		}
		return relation;
	}

	public Long save(RequestRelationship relationship) {
		Session sess = null;
		Long id = null;

		try {
			sess = dao.getSession();
			id = (Long) sess.save(relationship);
		}
		catch (Exception e) {
			log.error(e.getMessage());
		}
		finally {
			if (sess != null) {
				sess.close();
			}
		}
		return id;
	}

	public void delete(RequestRelationship condition) {
		Transaction trans = null;
		Session sess = null;
		try {
			List<RequestRelationship> rls = get(condition);
			if (rls != null && rls.size() > 0) {
				sess = dao.getSession();
				//trans = sess.beginTransaction();
				for (RequestRelationship r : rls) {
					sess.delete(r);
				}
				//trans.commit();
				//trans = null;
				sess.flush();
			}

		}
		catch (Exception ex) {
			log.error(ex.getMessage());
			if (trans != null) {
				trans.rollback();
			}
		}
		finally {
			if (sess != null) {
				sess.close();
			}
		}
	}

	public void disable(RequestRelationship condition) {
		Transaction trans = null;
		Session sess = null;
		Long parentId = null;
		try {
			List<RequestRelationship> rls = get(condition);
			if (rls != null && rls.size() > 0) {
				sess = dao.getSession();
				//trans = sess.beginTransaction();
				for (RequestRelationship r : rls) {
					// sess.delete(r);
					r.setEnable(false);
					sess.update(r);
				}
				//trans.commit();
				trans = null;
				sess.flush();
			}

		}
		catch (Exception ex) {
			log.error(ex.getMessage());
			if (trans != null) {
				trans.rollback();
			}
		}
		finally {
			if (sess != null) {
				sess.close();
			}
		}
	}

	public Long getParentId(Request request) throws Exception {
		RequestRelationship condition = new RequestRelationship();
		condition.setRequestId(request.getRequestid());
		condition.setIsParent(true);
		// condition.setRequestVersion(request.getRequestVersion());
		condition.setEnable(true);
		List<RequestRelationship> rList = get(condition);
		Long parentId = null;
		if (rList != null && rList.size() > 0) {
			condition = rList.get(0);
			parentId = condition.getRelationshipId();
			return parentId;
		}

		condition = new RequestRelationship();
		condition.setRelationshipId(request.getRequestid());
		condition.setIsParent(false);
		// condition.setRequestVersion(request.getRequestVersion());
		condition.setEnable(true);
		rList = get(condition);
		if (rList != null && rList.size() > 0) {
			condition = rList.get(0);
			parentId = condition.getRequestId();
			return parentId;
		}
		return null;
	}

	public List<Long> getChildId(Request request) throws Exception {

		List<Long> ids = new ArrayList<Long>();

		RequestRelationship condition = new RequestRelationship();
		condition.setRequestId(request.getRequestid());
		condition.setIsParent(false);
		// condition.setRequestVersion(request.getRequestVersion());
		condition.setEnable(true);
		List<RequestRelationship> rList = get(condition);

		if (rList != null && rList.size() > 0) {
			for (RequestRelationship r : rList) {
				ids.add(r.getRelationshipId());
			}
		}

		condition = new RequestRelationship();
		condition.setRelationshipId(request.getRequestid());
		condition.setIsParent(true);
		// condition.setRequestVersion(request.getRequestVersion());
		condition.setEnable(true);
		rList = get(condition);
		if (rList != null && rList.size() > 0) {
			for (RequestRelationship r : rList) {
				if (!ids.contains(r.getRequestId())) {
					ids.add(r.getRequestId());
				}
			}
		}
		return ids;
	}
}