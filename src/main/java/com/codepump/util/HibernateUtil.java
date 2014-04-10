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
	public static SessionFactory sessionFactory;
	private static Session session;

	static {
		try {
			String databaseUrl = System.getenv("DATABASE_URL");
	        if (databaseUrl != null) {
	             herokuConnection(databaseUrl);
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
	
	private static void herokuConnection(String databaseUrl) {
		String [] a = databaseUrl.split("@");
		String [] b = a[0].split(":");
		String username = b[1].replace("//", "");
		String password = b[2];
		String db = "jdbc:postgresql://" +a[1];
		
		Configuration configuration = new Configuration()
	    .addAnnotatedClass(com.codepump.data.User.class)
	    .addAnnotatedClass(com.codepump.data.CodeItem.class)
	    .addAnnotatedClass(com.codepump.tempobject.RecentItem.class)
	    .addAnnotatedClass(com.codepump.tempobject.MyStuffListItem.class)
	    .addAnnotatedClass(com.codepump.tempobject.UserLanguageStatisticsItem.class)
	    .setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
	    .setProperty("hibernate.connection.driver_class", "org.postgresql.Driver")
	    .setProperty("hibernate.connection.username", username)
	    .setProperty("hibernate.connection.password", password)
	    .setProperty("hibernate.connection.url", db);
		
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
