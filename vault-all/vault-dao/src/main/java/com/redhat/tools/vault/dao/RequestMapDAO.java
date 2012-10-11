package com.redhat.tools.vault.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.jboss.logging.Logger;

import com.redhat.tools.vault.bean.RequestMap;

/**
 * @author wezhao
 *
 */
public class RequestMapDAO {

	/** The logger. */
	protected static final Logger log = Logger.getLogger(RequestMapDAO.class);

	DAOFactory dao = null;

	Session session = null;

	public RequestMapDAO() {
		dao = DAOFactory.getInstance();
	}

	public List<RequestMap> get(RequestMap condition) throws Exception {
		try {
			session = dao.getSession();
			Criteria criteria = session.createCriteria(RequestMap.class);
			if (condition.getMapid() != null) {
				criteria.add(Expression.eq(RequestMap.PROPERTY_MAPID, new Long(
						condition.getMapid())));
			}
			if (condition.getMapname() != null) {
				criteria.add(Expression.eq(RequestMap.PROPERTY_MAPNAME,
						condition.getMapname()));
			}
			if (condition.getMapvalue() != null) {
				criteria.add(Expression.eq(RequestMap.PROPERTY_MAPVALUE,
						condition.getMapvalue()));
			}
			if (condition.getRequestid() != null) {
				criteria.add(Expression.eq(RequestMap.PROPERTY_REQUESTID,
						condition.getRequestid()));
			}
			if (condition.getRequestVersion() != null) {
				criteria.add(Expression.eq(RequestMap.PROPERTY_REQUEST_VERSION,
						condition.getRequestVersion()));
			}
			List<RequestMap> list = criteria.list();
			for (RequestMap s : list) {
				log.debug("requestMap=" + s);
			}
			return list;
		}
		catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		finally {
			try {
				dao.closeSession(session);
			}
			catch (Exception e) {
				log.error(e.getMessage());
				throw e;
			}
		}
	}

	public RequestMap update(RequestMap requestMap) throws Exception {
		RequestMap updatedRequestMap = null;
		Transaction trans = null;
		try {
			log.debug("update. requestMap=" + requestMap);
			session = dao.getSession();
			trans = session.beginTransaction();
			updatedRequestMap = (RequestMap) session.load(RequestMap.class,
					new Long(requestMap.getMapid()));
			log.debug("original requestMap=" + updatedRequestMap);
			if (requestMap.getMapname() != null)
				updatedRequestMap.setMapname(requestMap.getMapname());
			if (requestMap.getMapvalue() != null)
				updatedRequestMap.setMapvalue(requestMap.getMapvalue());
			if (requestMap.getRequestid() != null)
				updatedRequestMap.setRequestid(requestMap.getRequestid());
			if (requestMap.getDynamic() != null)
				updatedRequestMap.setDynamic(requestMap.getDynamic());
			if (requestMap.getRequestVersion() != null)
				updatedRequestMap.setRequestVersion(requestMap
						.getRequestVersion());
			session.update(updatedRequestMap);
			trans.commit();
			trans = null;
			session.flush();
		}
		catch (HibernateException e) {
			log.error(e.getMessage());
			throw e;
		}
		catch (IllegalStateException ie) {
			log.error(ie.getMessage());
		}
		finally {
			try {
				if (trans != null) {
					trans.rollback();
				}
				dao.closeSession(session);
			}
			catch (Exception e) {
				log.error(e.getMessage());
				throw e;
			}
		}
		return updatedRequestMap;
	}

	public Long save(RequestMap requestMap) throws Exception {
		Transaction trans = null;
		Long id = null;
		try {
			log.debug("save. requestMap=" + requestMap);
			session = dao.getSession();
			trans = session.beginTransaction();
			id = (Long) session.save(requestMap);
			log.debug("session.save success");
			trans.commit();
			trans = null;
			session.flush();
			log.debug("transaction commit success");
		}
		catch (HibernateException e) {
			log.error(e.getMessage());
			throw e;
		}
		catch (IllegalStateException ie) {
			log.error(ie.getMessage());
		}
		finally {
			try {
				if (trans != null) {
					trans.rollback();
				}
				dao.closeSession(session);
			}
			catch (Exception e) {
				log.error(e.getMessage());
				throw e;
			}
		}
		return id;
	}
}
