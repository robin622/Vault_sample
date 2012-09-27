package com.redhat.tools.vault.dao;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.jboss.logging.Logger;

import com.redhat.tools.vault.bean.VAUser;

/**
 * @author wezhao
 * 
 */
public class VAUserDAO {

	/** The logger. */
	protected static final Logger log = Logger.getLogger(VAUserDAO.class);

	DAOFactory dao = null;

	Session session = null;

	public VAUserDAO() {
		dao = DAOFactory.getInstance();
	}

	public List<VAUser> get(VAUser condition) throws Exception {
		try {
			log.debug("get. User=" + condition);
			session = dao.getSession();
			Criteria criteria = session.createCriteria(VAUser.class);
			if (condition.getUserid() != null) {
				criteria.add(Expression.eq(VAUser.PROPERTY_USERID, new Long(
						condition.getUserid())));
			}
			if (condition.getUsername() != null) {
				criteria.add(Expression.eq(VAUser.PROPERTY_USERNAME,
						condition.getUsername()));
			}
			if (condition.getUseremail() != null) {
				criteria.add(Expression.eq(VAUser.PROPERTY_USEREMAIL,
						condition.getUseremail()));
			}
			criteria.addOrder(Order.asc((VAUser.PROPERTY_CREATEDTIME)));
			List<VAUser> list = criteria.list();
			for (VAUser s : list) {
				log.debug("User=" + s);
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

	public Long save(VAUser condition) throws Exception {
		Transaction trans = null;
		Long id = null;
		try {
			log.debug("save. User=" + condition);
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

	public VAUser update(VAUser user) throws Exception {
		VAUser updatedVAUser = null;
		Transaction trans = null;
		try {
			log.debug("update. user=" + user);
			session = dao.getSession();
			trans = session.beginTransaction();
			updatedVAUser = (VAUser) session.load(VAUser.class,
					new Long(user.getUserid()));
			log.debug("original user=" + updatedVAUser);
			if (user.getIs_admin() != null)
				updatedVAUser.setIs_admin(user.getIs_admin());
			if (user.getLogin_count() != null)
				updatedVAUser.setLogin_count(user.getLogin_count());

			session.update(updatedVAUser);
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
		return updatedVAUser;
	}

	public List<String> findAllUserEmails() {
		List<String> emails = new ArrayList<String>();
		List<VAUser> users = null;
		VAUser user = new VAUser();
		try {
			users = this.get(user);
			if (users != null && users.size() > 0) {
				for (VAUser temp : users) {
					if (!emails.contains(temp.getUseremail())) {
						emails.add(temp.getUseremail());
					}
				}
			}
		}
		catch (Exception e) {
			log.error(e.getMessage());
		}
		return emails;
	}
}
