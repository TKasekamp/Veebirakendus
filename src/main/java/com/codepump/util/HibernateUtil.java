package com.codepump.util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * Utility class that makes sure we has a single open hibernate session.
 */
public class HibernateUtil {
	private static ServiceRegistry serviceRegistry;
	public static final SessionFactory sessionFactory;
	private static Session session;

	static {
		try {
			Configuration configuration = new Configuration();
			configuration.configure();
			serviceRegistry = new StandardServiceRegistryBuilder()
					.applySettings(configuration.getProperties()).build();
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		} catch (Throwable ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static Session currentSession() throws HibernateException {
		if (session == null) {
			session = sessionFactory.openSession();
		}
		return session;
	}

	public static void closeSession() throws HibernateException {
		if (session != null)
			session.close();
		session = null;
	}
}
