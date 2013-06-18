package com.redhat.tools.vault.dao;

import javax.persistence.Persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.jboss.logging.Logger;

/**
 * @author wezhao
 */
public class DAOFactory {

    /** SessionFactory. */
    SessionFactory factory = null;

    /** DAOFactory. */
    private static DAOFactory instance = null;

    /** The logger. */
    protected static final Logger log = Logger.getLogger(DAOFactory.class);

    private DAOFactory() {
        init();
    }

    public static DAOFactory getInstance() {
        if (DAOFactory.instance == null) {
            DAOFactory.instance = new DAOFactory();
        }
        return DAOFactory.instance;
    }

    private void init() {
        try {
            Configuration cfg = new Configuration().configure("hibernate_vault.cfg.xml");
            // factory = new AnnotationConfiguration().configure().buildSessionFactory();
            factory = cfg.buildSessionFactory();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public Session getSession() throws Exception {
        Session session = null;
        try {
            // session = this.factory.openSession();
            session = (Session) Persistence.createEntityManagerFactory("MyVtDS").createEntityManager().getDelegate();
        } catch (Exception e) {
            throw e;
        } finally {
        }
        return session;
    }

    public void closeSession(Session session) throws Exception {
        try {
            if (session.isOpen()) {
                session.close();
            }
        } catch (Exception e) {
            throw e;
        } finally {
        }
    }

}
