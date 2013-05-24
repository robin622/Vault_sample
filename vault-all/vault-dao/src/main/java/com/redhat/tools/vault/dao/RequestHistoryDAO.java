package com.redhat.tools.vault.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.jboss.logging.Logger;

import com.redhat.tools.vault.bean.Request;
import com.redhat.tools.vault.bean.RequestHistory;
import com.redhat.tools.vault.util.DateUtil;
import com.redhat.tools.vault.util.StringUtil;

/**
 * @author wezhao
 * 
 */
public class RequestHistoryDAO {

	/** The logger. */
	protected static final Logger log = Logger
			.getLogger(RequestHistoryDAO.class);

	DAOFactory dao = null;

	Session session = null;

	public RequestHistoryDAO() {
		dao = DAOFactory.getInstance();
	}

	public List<RequestHistory> get(RequestHistory condition,
			boolean notSignedBy) {
		try {
			session = dao.getSession();
			Criteria criteria = session.createCriteria(RequestHistory.class);
			if (condition.getHistoryid() != null) {
				criteria.add(Expression.eq(RequestHistory.PROPERTY_HISTORYID,
						new Long(condition.getHistoryid())));
			}
			if (condition.getRequestid() != null) {
				criteria.add(Expression.eq(RequestHistory.PROPERTY_REQUESTID,
						condition.getRequestid()));
			}
			if (condition.getEditedby() != null) {
				criteria.add(Expression.eq(RequestHistory.PROPERTY_EDITEDBY,
						condition.getEditedby()));
			}
			if(condition.getStartDate() !=null){
//					criteria.add(Expression.gt(RequestHistory.PROPERTY_EDITEDTIME,
//							DateUtil.toVaultDate(condition.getStartDate())));
			}
			if(condition.getEndDate() != null){
//				criteria.add(Expression.lt(RequestHistory.PROPERTY_EDITEDTIME,
//						DateUtil.toVaultDate(condition.getEndDate())));
			}
			if (condition.getUseremail() != null) {
				criteria.add(Expression.eq(RequestHistory.PROPERTY_USEREMAIL,
						condition.getUseremail()));
			}
			if (condition.getRequestVersion() != null) {
				criteria.add(Expression.eq(
						RequestHistory.PROPERTY_REQUEST_VERSION,
						condition.getRequestVersion()));
			}
			if (condition.getStatus() != null) {
				criteria.add(Expression.eq(RequestHistory.PROPERTY_STATUS,
						condition.getStatus()));
			}
			if (condition.getIsHistory() != null) {
				criteria.add(Expression.eq(RequestHistory.PROPERTY_IS_HISTORY,
						condition.getIsHistory()));
			}
			criteria.addOrder(Order.desc((RequestHistory.PROPERTY_HISTORYID)));
			List<RequestHistory> list = criteria.list();
			List<RequestHistory> notSignedByList = new ArrayList<RequestHistory>();
			if (list != null && list.size() > 0) {
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				for (RequestHistory h : list) {
					h.setEditDate(format.format(h.getEditedtime()));
					h.setComment(StringUtil.escapeHTMLXMLJavaScript(h
							.getComment()));

					if (notSignedBy && !h.getStatus().equals(Request.SIGNED_BY)) {
						notSignedByList.add(h);
					}
				}

				if (notSignedBy) {
					return notSignedByList;
				}

				return list;
			}
			return null;
		}
		catch (Exception e) {
			log.error(e.getMessage());
		}
		finally {
			try {
				dao.closeSession(session);
			}
			catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		return null;
	}

	public List<List<RequestHistory>> getReports(List<String> owners,
			Long requestid) {
		try {

			List<List<RequestHistory>> reports = new ArrayList<List<RequestHistory>>();
			RequestHistory history = new RequestHistory();
			RequestHistory addHistory = null;
			List<RequestHistory> historys = new ArrayList<RequestHistory>();

			Request rq = new RequestDAO().find(requestid);
			if (rq == null) {
				return null;
			}
			if (owners != null && owners.size() > 0) {
				for (String email : owners) {
					history.setRequestid(requestid);
					history.setUseremail(email);
					// history.setIsHistory(false);
					historys = get(history, false);
					if (historys == null || historys.size() < 1) {
						addHistory = new RequestHistory();
						addHistory.setRequestid(requestid);
						addHistory.setEditedby(email);
						addHistory.setStatus(Request.WAITING);
						addHistory.setUseremail(email);
						addHistory.setComment("");
						addHistory.setIsHistory(Boolean.FALSE);
						historys = new ArrayList<RequestHistory>();
						historys.add(addHistory);
					}
					if (!reports.contains(historys)) {
						reports.add(historys);
					}
				}
				// return reports;
			}
			if (!owners.contains(rq.getCreatedby() + "@redhat.com")
					&& !owners.contains(rq.getCreatedby() + "@REDHAT.COM")) {
				historys = new ArrayList<RequestHistory>();
				RequestHistory rHist = new RequestHistory();
				rHist.setEditedby(rq.getCreatedby());
				rHist.setRequestid(requestid);
				// rHist.setIsHistory(false);
				List<RequestHistory> rHists = new RequestHistoryDAO().get(
						rHist, false);
				if (rHists != null && rHists.size() > 0) {
					for (RequestHistory rh : rHists) {
						if (rh.getStatus().equals(Request.SIGNED_ONBEHALF)) {
							// rh.setDisplayFlag("1");
							historys.add(rh);
							break;
						}
					}
				}
				if (historys != null && historys.size() > 0
						&& !reports.contains(historys)) {
					reports.add(historys);
				}
			}

			return reports;

		}
		catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	public Long save(RequestHistory history) throws Exception {
		Transaction trans = null;
		Long id = null;
		try {
			session = dao.getSession();
			//trans = session.beginTransaction();
			id = (Long) session.save(history);
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
		return id;
	}

	public void deleteHistory(RequestHistory condition) throws Exception {
		RequestHistory deletedRequestHistory = null;
		Transaction trans = null;
		try {
			session = dao.getSession();
			//trans = session.beginTransaction();
			deletedRequestHistory = (RequestHistory) session.load(
					RequestHistory.class, new Long(condition.getHistoryid()));
			if (condition.getHistoryid() != null)
				deletedRequestHistory.setHistoryid(condition.getHistoryid());
			session.delete(deletedRequestHistory);
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

	public void disableHistory(RequestHistory condition) throws Exception {
		RequestHistory disableRequestHistory = null;
		Transaction trans = null;
		try {
			session = dao.getSession();
			//trans = session.beginTransaction();
			disableRequestHistory = (RequestHistory) session.load(
					RequestHistory.class, new Long(condition.getHistoryid()));
			if (condition.getHistoryid() != null)
				// session.delete(disableRequestHistory);
				disableRequestHistory.setIsHistory(true);
			session.update(disableRequestHistory);

			//trans.commit();
			//session.flush();
		}
		catch (HibernateException e) {
			log.error(e.getMessage());
			if (trans != null) {
				trans.rollback();
			}
		}
		catch (IllegalStateException ie) {
			log.error(ie.getMessage());
			if (trans != null) {
				trans.rollback();
			}
		}
		finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public RequestHistory getRequestHistoryByid(Long historyid)
			throws Exception {
		try {
			session = dao.getSession();
			Criteria c = session.createCriteria(RequestHistory.class);
			c.add(Restrictions.eq("historyid", historyid));
			RequestHistory returnV = (RequestHistory) c.uniqueResult();
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
	
	

	/*
	 * public List<RequestHistory> get(Long requestId, boolean isHistory){
	 * List<RequestHistory> histories = null; Session sess = null; try { sess =
	 * dao.getSession(); String queryString =
	 * "from RequestHistory as a where a.requestid = ? and a.isHistory = ? order by a.historyid desc"
	 * ; Query queryObject = sess.createQuery(queryString);
	 * queryObject.setLong(0, requestId); queryObject.setBoolean(1, isHistory);
	 * histories = queryObject.list(); return histories; } catch (Exception re)
	 * { log.error("find RequestHistory failed", re); } finally{ if(sess !=
	 * null){ sess.close(); } } return histories; }
	 * 
	 * public List<RequestHistory> get(Long requestId){ List<RequestHistory>
	 * histories = null; Session sess = null; try { sess = dao.getSession();
	 * String queryString =
	 * "from RequestHistory as a where a.requestid = ? order by a.historyid desc"
	 * ; Query queryObject = sess.createQuery(queryString);
	 * queryObject.setLong(0, requestId); histories = queryObject.list(); return
	 * histories; } catch (Exception re) {
	 * log.error("find RequestHistory failed", re); } finally{ if(sess != null){
	 * sess.close(); } } return histories; }
	 */
}
