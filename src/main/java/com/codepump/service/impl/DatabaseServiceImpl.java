package com.codepump.service.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

import com.codepump.data.CodeItem;
import com.codepump.data.User;
import com.codepump.service.DatabaseService;
import com.codepump.tempobject.MyStuffListItem;
import com.codepump.tempobject.RecentItem;
import com.codepump.tempobject.UserLanguageStatisticsItem;
import com.codepump.util.HibernateUtil;

/**
 * Note: sessions started with getCurrentSession() are flushed and closed
 * automatically. https://community.jboss.org/wiki/SessionsAndTransactions
 * 
 * @author TKasekamp
 * 
 */
public class DatabaseServiceImpl implements DatabaseService {
	private Session session;
	private static SessionFactory sessionFactory;

	public DatabaseServiceImpl() {
		sessionFactory = HibernateUtil.getSessionFactory();

	}

	@Override
	public void deleteCodeItem(int codeId) {
		session = sessionFactory.getCurrentSession();
		FullTextSession fullTextSession = Search.getFullTextSession(session);
		fullTextSession.purge(CodeItem.class, codeId);
		// session.delete(Integer.toString(codeId), CodeItem.class);
		session.createSQLQuery("delete from codeitem where code_id =:id")
				.setParameter("id", codeId).executeUpdate();
	}

	@Override
	public CodeItem findCodeItemById(int codeId) {
		session = sessionFactory.getCurrentSession();
		return (CodeItem) session.load(CodeItem.class, codeId);
	}

	@Override
	public List<CodeItem> getAllCodeItems(int limit, int offset) {
		session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<CodeItem> dataset = session
				.createQuery(
						"from CodeItem where PRIVACY='Public' order by create_date desc ")
				.setMaxResults(limit).setFirstResult(offset).list();
		return dataset;
	}

	@Override
	public List<MyStuffListItem> getAllUserItems(int userId, int limit,
			int offset) {
		session = sessionFactory.getCurrentSession();
		Query q = session.getNamedQuery("thisUserCodeByID");
		q.setParameter("t_id", userId).setParameter("limit", limit)
				.setParameter("offset", offset);
		@SuppressWarnings("unchecked")
		List<MyStuffListItem> dataset = q.list();
		return dataset;
	}

	@Override
	public List<RecentItem> getRecentItems(int limit, int offset) {
		session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<RecentItem> results = session
				.getNamedQuery("findRecentItemsInOrder").setMaxResults(limit)
				.list();
		return results;
	}

	@Override
	public void updateCodeItem(CodeItem code) {
		session = sessionFactory.getCurrentSession();
		session.update(code);
	}

	@Override
	public void saveCodeItem(CodeItem code) {
		session = sessionFactory.getCurrentSession();
		session.save(code);
	}

	@Override
	public User findUserById(int userId) {
		session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<User> dataset = session.createQuery("from User where USER_ID=:id")
				.setParameter("id", userId).list();
		if (dataset.size() == 1) {
			return dataset.get(0);
		} else {
			return null;
		}
	}

	@Override
	public User findUserByEmail(String email) {
		session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<User> dataset = session
				.createQuery("from User where USER_EMAIL = :email")
				.setParameter("email", email).list();
		if (dataset.size() == 1) {
			return dataset.get(0);
		} else {
			return null;
		}
	}

	@Override
	public User findUserByName(String username) {
		session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<User> dataset = session
				.createQuery("from User where USER_NAME = :userName")
				.setParameter("userName", username).list();
		if (dataset.size() == 1) {
			return dataset.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<UserLanguageStatisticsItem> findUserLanguageStatistics(
			int userId) {
		session = sessionFactory.getCurrentSession();
		Query q = session.getNamedQuery("thisUserLanguageStatistics");
		q.setParameter("t_id", userId);
		@SuppressWarnings("unchecked")
		List<UserLanguageStatisticsItem> dataset = q.list();
		return dataset;
	}

	@Override
	public void saveUser(User user) {
		session = sessionFactory.getCurrentSession();
		session.save(user);
	}


	@Override
	public void deleteUser(int userId) {
		session = sessionFactory.getCurrentSession();
		session.createSQLQuery("delete from webapp_user where user_id =:id")
				.setParameter("id", userId).executeUpdate();
	}

	@Override
	public void updateUser(User user) {
		session = sessionFactory.getCurrentSession();
		session.update(user);

	}

}
