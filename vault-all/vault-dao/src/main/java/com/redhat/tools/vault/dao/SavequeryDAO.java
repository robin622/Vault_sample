package com.redhat.tools.vault.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.jboss.logging.Logger;

import com.redhat.tools.vault.bean.Savequery;


/**
 * 
 * @author wguo@redhat.com
 *
 */
public class SavequeryDAO {

	/** The logger. */
	protected 	static final Logger log 	= 	Logger.getLogger(SavequeryDAO.class);

	DAOFactory 					  	dao 	= 	null;
	Session 						session = 	null;

	public SavequeryDAO() {
		dao = DAOFactory.getInstance();
	}

	public List<Savequery> get(Savequery condition) throws Exception {
		try {
			log.debug("get. Savequery=" + condition);
			session = dao.getSession();
			Criteria criteria = session.createCriteria(Savequery.class);
			if (condition.getQueryid() != null) {
				criteria.add(Expression.eq(Savequery.PROPERTY_QUERYID, new Long(condition.getQueryid())));
			}
			if (condition.getQueryname() != null) {
				criteria.add(Expression.eq(Savequery.PROPERTY_QUERYNAME, condition.getQueryname()));
			}
			if (condition.getSearchname() != null) {
				criteria.add(Expression.eq(Savequery.PROPERTY_SEARCHNAME, condition.getSearchname()));
			}
			if (condition.getCreatedby() != null) {
				criteria.add(Expression.eq(Savequery.PROPERTY_CREATEDBY, condition.getCreatedby()));
			}
			if (condition.getStatus() != null) {
				criteria.add(Expression.eq(Savequery.PROPERTY_STATUS, condition.getStatus()));
			}
			if (condition.getSearchcreator() != null) {
				criteria.add(Expression.eq(Savequery.PROPERTY_SEARCHCREATOR, condition.getSearchcreator()));
			}
			
			criteria.addOrder(Order.desc((Savequery.PROPERTY_CREATEDTIME)));
			List<Savequery> list = criteria.list();
			for (Savequery s : list) {
				log.debug("Review=" + s);
			}
			return list;
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

	public Long save(Savequery saveQuery) throws Exception {
		Transaction trans = null;
		Long id = null;
		try {
			session = dao.getSession();
			//trans = session.beginTransaction();
			id = (Long) session.save(saveQuery);
			//trans.commit();
			//trans = null;
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
		return id;
	}
	
	public void delete(Savequery condition) throws Exception {
		Savequery deletedSavequery = null;
		Transaction trans = null;
		try {
			session = dao.getSession();
			//trans = session.beginTransaction();
			deletedSavequery = (Savequery)session.load(Savequery.class, new Long(condition.getQueryid()));
			session.delete(deletedSavequery);
	        //trans.commit();
			//trans = null;
			session.flush();
		}
		catch(HibernateException e) {
			log.error(e.getMessage());
			throw e;
		}
		catch(IllegalStateException ie) {
			log.error(ie.getMessage());
		}
		finally {
			try {
				if(trans != null) {
					trans.rollback();
				}
				dao.closeSession(session);
			}
			catch(Exception e) {
				log.error(e.getMessage());
				throw e;
			}
		}
	}
}
