package com.redhat.tools.vault.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.jboss.logging.Logger;

import com.redhat.tools.vault.bean.SendemailCount;

/**
 * @author wezhao
 * 
 */
public class SendemailCountDAO {

	/** The logger. */
	protected static final Logger log = Logger
			.getLogger(SendemailCountDAO.class);

	DAOFactory dao = null;

	Session session = null;

	public SendemailCountDAO() {
		dao = DAOFactory.getInstance();
	}

	public List<SendemailCount> get(SendemailCount condition) throws Exception {
		try {
			session = dao.getSession();
			Criteria criteria = session.createCriteria(SendemailCount.class);
			if (condition.getSendid() != null) {
				criteria.add(Expression.eq(SendemailCount.PROPERTY_SENDID,
						new Long(condition.getSendid())));
			}
			if (condition.getRequestid() != null) {
				criteria.add(Expression.eq(SendemailCount.PROPERTY_REQUESTID,
						new Long(condition.getRequestid())));
			}
			if (condition.getRequesttime() != null) {
				criteria.add(Expression.eq(SendemailCount.PROPERTY_REQUESTTIME,
						condition.getRequesttime()));
			}
			if (condition.getCount() != null) {
				criteria.add(Expression.eq(SendemailCount.PROPERTY_COUNT,
						new Long(condition.getCount())));
			}
			List<SendemailCount> list = criteria.list();
			for (SendemailCount s : list) {
				log.debug("SendemailCount=" + s);
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

	public SendemailCount update(SendemailCount condition) throws Exception {
		SendemailCount updatedCount = null;
		Transaction trans = null;
		try {
			log.debug("update. condition=" + condition);
			session = dao.getSession();
			trans = session.beginTransaction();
			updatedCount = (SendemailCount) session.load(SendemailCount.class,
					new Long(condition.getSendid()));
			log.debug("original SendemailCount=" + updatedCount);
			if (condition.getRequestid() != null)
				updatedCount.setRequestid(condition.getRequestid());
			if (condition.getCount() != null)
				updatedCount.setCount(condition.getCount());
			if (condition.getRequesttime() != null)
				updatedCount.setRequesttime(condition.getRequesttime());
			session.update(updatedCount);
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
		return updatedCount;
	}

	public Long save(SendemailCount condition) throws Exception {
		Transaction trans = null;
		Long id = null;
		try {
			log.debug("save. SendemailCount=" + condition);
			session = dao.getSession();
			trans = session.beginTransaction();
			id = (Long) session.save(condition);
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

	public void deleteEmailCount(SendemailCount condition) throws Exception {
		SendemailCount deletedSendemailCount = null;
		Transaction trans = null;
		try {
			session = dao.getSession();
			trans = session.beginTransaction();
			deletedSendemailCount = (SendemailCount) session.load(
					SendemailCount.class, new Long(condition.getSendid()));
			if (condition.getRequestid() != null)
				deletedSendemailCount.setRequestid(condition.getRequestid());
			session.delete(deletedSendemailCount);
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
	}
}
