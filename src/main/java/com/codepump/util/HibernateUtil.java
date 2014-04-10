package com.codepump.util;

import java.net.URI;

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
	public static SessionFactory sessionFactory;
	private static Session session;

	static {
		try {
//	        String databaseUrl = System.getenv("HEROKU_POSTGRESQL_BRONZE_SQL");
			String databaseUrl = System.getenv("DATABASE_URL");
	        if (databaseUrl != null) {
	             herokuConnection(new URI(databaseUrl));
	        } else {
	             localConnection();
	        }

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
	
	private static void herokuConnection(URI dbUri) {
		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:" + dbUri;
		
		Configuration configuration = new Configuration()
	    .addClass(com.codepump.data.User.class)
	    .addClass(com.codepump.data.CodeItem.class)
	    .addClass(com.codepump.tempobject.RecentItem.class)
	    .addClass(com.codepump.tempobject.MyStuffListItem.class)
	    .addClass(com.codepump.tempobject.UserLanguageStatisticsItem.class)
	    .setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
	    .setProperty("hibernate.connection.driver_class", "org.postgresql.Driver")
	    .setProperty("hibernate.connection.username", username)
	    .setProperty("hibernate.connection.password", password)
	    .setProperty("hibernate.connection.url", dbUrl);
		
		serviceRegistry = new StandardServiceRegistryBuilder()
		.applySettings(configuration.getProperties()).build();
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);
	}
	
	private static void localConnection() {
		Configuration configuration = new Configuration();
		configuration.configure();
		serviceRegistry = new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties()).build();
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);		
	}
}
