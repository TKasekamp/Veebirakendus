package com.codepump.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import com.codepump.service.impl.DatabaseServiceImpl;

/**
 * Class to create and hold a Hibernate sessionFactory. SessionFactory is
 * created automatically the first time a reference is made in
 * {@link DatabaseServiceImpl} to it thanks to the static method. <br>
 * 
 * If there exists a System variable DATABASE_URL as is the case when deploying
 * to Heroku, all necessaty connection parameters are extracted from it so the
 * database password is not visible in code. Connection is configured entirely
 * in Java. <br>
 * 
 * For local connections a hibernate.cfg.xml config file is used, found in
 * src/main/resources. The configuration is similar to Heroku connection, only
 * the local database url, username and password are different.<br>
 * 
 * Data objects are mapped with annotations.
 */
public class HibernateUtil {
	private static ServiceRegistry serviceRegistry;
	private static SessionFactory sessionFactory;

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

	private static void herokuConnection(String databaseUrl) {
		String[] a = databaseUrl.split("@");
		String[] b = a[0].split(":");
		String username = b[1].replace("//", "");
		String password = b[2];
		String db = "jdbc:postgresql://" + a[1];

		Configuration configuration = new Configuration()
				.addAnnotatedClass(com.codepump.data.User.class)
				.addAnnotatedClass(com.codepump.data.CodeItem.class)
				.addAnnotatedClass(com.codepump.tempobject.RecentItem.class)
				.addAnnotatedClass(
						com.codepump.tempobject.MyStuffListItem.class)
				.addAnnotatedClass(
						com.codepump.tempobject.UserLanguageStatisticsItem.class)
				.setProperty("hibernate.dialect",
						"org.hibernate.dialect.PostgreSQLDialect")
				.setProperty("hibernate.connection.driver_class",
						"org.postgresql.Driver")
				.setProperty("hibernate.connection.username", username)
				.setProperty("hibernate.connection.password", password)
				.setProperty("hibernate.connection.url", db)
				.setProperty("hibernate.current_session_context_class", "thread")
				.setProperty("hibernate.search.default.directory_provider", "filesystem")
				.setProperty("hibernate.search.default.indexBase", "./indexes")
				.setProperty("hibernate.c3p0.min_size", "5")
				.setProperty("hibernate.c3p0.max_size", "20")
				.setProperty("hibernate.c3p0.timeout", "300")
				.setProperty("hibernate.c3p0.max_statements", "50")
				.setProperty("hibernate.c3p0.idle_test_period", "3000");

		serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
				configuration.getProperties()).build();
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);
	}

	private static void localConnection() {
		Configuration configuration = new Configuration();
		configuration.configure();
		serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
				configuration.getProperties()).build();
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}
