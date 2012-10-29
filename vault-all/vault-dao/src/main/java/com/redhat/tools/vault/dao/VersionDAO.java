package com.redhat.tools.vault.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.jboss.logging.Logger;

import com.redhat.tools.vault.bean.Version;

/**
 * @author wezhao
 */
public class VersionDAO {

	/** The logger. */
	protected static final Logger log = Logger.getLogger(VersionDAO.class);

	DAOFactory dao = null;

	Session session = null;

	public VersionDAO() {
		dao = DAOFactory.getInstance();
	}

	public List<Version> get(Version condition) throws Exception {
		try {
			log.debug("get. Version=" + condition);
			session = dao.getSession();
			Criteria criteria = session.createCriteria(Version.class);
			if (condition.getId() != null) {
				criteria.add(Expression.eq(Version.PROPERTY_ID, new Long(
						condition.getId())));
			}
			if (condition.getProduct_id() != null) {
				criteria.add(Expression.eq(Version.PROPERTY_PRODUCT_ID,
						new Long(condition.getProduct_id())));
			}
			criteria.addOrder(Order.asc((Version.PROPERTY_VALUE)));
			List<Version> list = criteria.list();
			for (Version s : list) {
				log.debug("Version=" + s);
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
	@SuppressWarnings("unchecked")
	public List<Version> getAllVersions() throws Exception {
		try {
			session = dao.getSession();
			Criteria criteria = session.createCriteria(Version.class);
			criteria.addOrder(Order.asc((Version.PROPERTY_ID)));
			List<Version> versions = criteria.list();
			return versions;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		} finally {
			try {
				dao.closeSession(session);
			} catch (Exception e) {
				log.error(e.getMessage());
				throw e;
			}
		}
	}
	
	public void updateVersion(Version version) throws Exception{
//		Transaction trans = null;
		try{
			session = dao.getSession();
//			trans = session.beginTransaction();
			session.update(version);
//			trans.commit();
//			trans = null;
			session.flush();
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		} finally {
			try {
//				if (trans != null) {
//					trans.rollback();
//				}
				dao.closeSession(session);
			} catch (Exception e) {
				log.error(e.getMessage());
				throw e;
			}
		}
	}
	
	public void saveVersion(Version version) throws Exception{
//		Transaction trans = null;
		try{
			session = dao.getSession();
//			trans = session.beginTransaction();
			session.save(version);
//			trans.commit();
//			trans = null;
			session.flush();
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		} finally {
			try {
//				if (trans != null) {
//					trans.rollback();
//				}
				dao.closeSession(session);
			} catch (Exception e) {
				log.error(e.getMessage());
				throw e;
			}
		}
	}
}
