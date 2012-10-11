package com.redhat.tools.vault.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.jboss.logging.Logger;

import com.redhat.tools.vault.bean.CommentRelationship;

/**
 * @author wezhao
 *
 */
public class CommentRelationshipDAO {

	/** The logger. */
	protected static final Logger log = Logger.getLogger(ReplyCommentDAO.class);

	DAOFactory dao = null;

	public CommentRelationshipDAO() {
		dao = DAOFactory.getInstance();
	}

	public Long save(CommentRelationship commentRelation) throws Exception {
		Session session = null;
		Transaction trans = null;
		Long id = null;
		try {
			session = dao.getSession();
			//trans = session.beginTransaction();
			id = (Long) session.save(commentRelation);
			//trans.commit();
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
		return id;
	}

	public List<Long> getReplyIdListByHistoryId(Long historyid)
			throws Exception {
		Session session = null;
		try {
			session = dao.getSession();
			Criteria c = session.createCriteria(CommentRelationship.class);
			c.add(Restrictions.eq("historyid", historyid));
			c.addOrder(Order.desc((CommentRelationship.PROPERTY_REPLYID)));
			List<CommentRelationship> commentRelationList = c.list();
			List<Long> returnV = new ArrayList<Long>();
			if (commentRelationList != null) {
				for (CommentRelationship commentRelation : commentRelationList) {
					Long replyid = commentRelation.getReplyid();
					returnV.add(replyid);
				}
				return returnV;
			}
			else {
				return Collections.emptyList();
			}
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
			}
		}
	}

	public Long getHistoryidByreplyId(Long replyid) throws Exception {
		Session session = null;
		try {
			session = dao.getSession();
			Criteria c = session.createCriteria(CommentRelationship.class);
			c.add(Restrictions.eq("replyid", replyid));
			CommentRelationship reply = (CommentRelationship) c.uniqueResult();
			Long returnV = reply.getHistoryid();
			return returnV;
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
			}
		}
	}
	
	public void deleteRelationByHistoryid(Long historyid) throws Exception {
		Session session = null;
		try {
			session = dao.getSession();
			Criteria c = session.createCriteria(CommentRelationship.class);
			c.add(Restrictions.eq("historyid", historyid));
			c.addOrder(Order.desc((CommentRelationship.PROPERTY_REPLYID)));
			List<CommentRelationship> commentRelationList = c.list();
			//trans = session.beginTransaction();
			if(commentRelationList != null){
				for(CommentRelationship relation : commentRelationList){
					session.delete(relation);
				}
			}
			//trans.commit();
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
				dao.closeSession(session);
			}
			catch (Exception e) {
				log.error(e.getMessage());
				throw e;
			}
		}
	}
}
