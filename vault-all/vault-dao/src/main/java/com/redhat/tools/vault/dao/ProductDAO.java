package com.redhat.tools.vault.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.jboss.logging.Logger;

import com.redhat.tools.vault.bean.Product;

/**
 * @author <a href="mailto:speng@redhat.com">Peng Song</a>
 * @version $Revision$
 */
public class ProductDAO {

	/** The logger. */
	protected static final Logger log = Logger.getLogger(ProductDAO.class);

	DAOFactory dao = null;

	Session session = null;

	public ProductDAO() {
		dao = DAOFactory.getInstance();
	}

	public List<Product> get(Product condition) throws Exception {
		try {
			log.debug("get. Product=" + condition);
			session = dao.getSession();
			Criteria criteria = session.createCriteria(Product.class);
			if (condition.getId() != null) {
				criteria.add(Expression.eq(Product.PROPERTY_ID, new Long(
						condition.getId())));
			}
			criteria.addOrder(Order.asc((Product.PROPERTY_NAME)));
			List<Product> list = criteria.list();
			for (Product s : list) {
				log.debug("Product=" + s);
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
}
