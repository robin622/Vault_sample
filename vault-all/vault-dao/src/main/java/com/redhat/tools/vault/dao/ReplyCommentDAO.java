package com.redhat.tools.vault.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.jboss.logging.Logger;

import com.redhat.tools.vault.bean.ReplyComment;

/**
 * @author wezhao
 */
public class ReplyCommentDAO {

	/** The logger. */
	protected static final Logger log = Logger.getLogger(ReplyCommentDAO.class);

	DAOFactory dao = null;

	public ReplyCommentDAO() {
		dao = DAOFactory.getInstance();
	}

	public Long save(ReplyComment replyComment) throws Exception {
		Transaction trans = null;
		Long replyid = null;
		Session session = null;
		try {
			session = dao.getSession();
			//trans = session.beginTransaction();
			replyid = (Long) session.save(replyComment);
			//trans.commit();
			trans = null;
			//session.flush();
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
		return replyid;
	}

	// public ReplyComment getReplyCommentByid(Long replyid) throws Exception {
	// try {
	// session = dao.getSession();
	// Criteria c = session.createCriteria(ReplyComment.class);
	// c.add(Restrictions.eq("replyid", replyid));
	// ReplyComment returnV = (ReplyComment) c.uniqueResult();
	// return returnV;
	// }
	// catch (Exception e) {
	// log.error(e.getMessage());
	// throw e;
	// }
	// finally {
	// try {
	// dao.closeSession(session);
	// }
	// catch (Exception e) {
	// log.error(e.getMessage());
	// }
	// }
	// }

	public List<ReplyComment> getReplyCommentListByIdList(List<Long> replyidList)
			throws Exception {
		Session session = null;
		try {
			session = dao.getSession();
			List<ReplyComment> replyList = new ArrayList<ReplyComment>();
			for (Long replyid : replyidList) {
				Query query = session
						.createQuery("from ReplyComment as reply where reply.replyid = "
								+ replyid);
				ReplyComment reply = (ReplyComment) query.uniqueResult();
				if (reply != null) {
					replyList.add(reply);
				}
			}
			Collections.sort(replyList, new Comparator<ReplyComment>() {
				public int compare(ReplyComment r1, ReplyComment r2) {
					return r1.getBaseid().compareTo(r2.getBaseid());
				}
			});
			return replyList;
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
	
	public void deleteReplyCommentList(List<ReplyComment> list) throws Exception {
		Transaction trans = null;
		Session session = null;
		try {
			session = dao.getSession();
			//trans = session.beginTransaction();
			if(list != null){
				for(ReplyComment reply : list){
					session.delete(reply);
				}
			}
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
	}
}
