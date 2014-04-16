package com.codepump.service.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.codepump.data.CodeItem;
import com.codepump.data.User;
import com.codepump.service.DatabaseService;
import com.codepump.tempobject.MyStuffListItem;
import com.codepump.tempobject.RecentItem;
import com.codepump.tempobject.SearchItem;
import com.codepump.tempobject.UserLanguageStatisticsItem;
import com.codepump.util.HibernateUtil;

public class DatabaseServiceImpl implements DatabaseService {
	private Session session;
	private static SessionFactory sessionFactory;

	public DatabaseServiceImpl() {
		sessionFactory = HibernateUtil.sessionFactory;

	}

	@Override
	public void deleteCodeItem(int codeId) {
		try {
			session = sessionFactory.openSession();
			session.getTransaction().begin();
			session.createSQLQuery("delete from codeitem where code_id =:id")
					.setParameter("id", codeId).executeUpdate();
			session.getTransaction().commit();
		} finally {
			session.close();
		}
	}

	@Override
	public CodeItem findCodeItemById(int codeId) {
		try {
			session = sessionFactory.openSession();
			@SuppressWarnings("unchecked")
			List<CodeItem> dataset = session
					.createQuery("from CodeItem where CODE_ID=:id")
					.setParameter("id", codeId).list();
			if (dataset.size() == 1) {
				return dataset.get(0);
			} else {
				return null;
			}
		} finally {
			session.close();
		}
	}

	@Override
	public List<CodeItem> getAllCodeItems() {
		try {
			session = sessionFactory.openSession();
			@SuppressWarnings("unchecked")
			List<CodeItem> dataset = session
					.createQuery(
							"from CodeItem where PRIVACY='Public' order by create_date desc")
					.list();
			return dataset;
		} finally {
			session.close();
		}
	}

	@Override
	public List<MyStuffListItem> getAllUserItems(int userId) {
		try {
			session = sessionFactory.openSession();
			Query q = session.getNamedQuery("thisUserCodeByID");
			q.setParameter("t_id", userId);
			@SuppressWarnings("unchecked")
			List<MyStuffListItem> dataset = q.list();
			return dataset;
		} finally {
			session.close();
		}
	}

	@Override
	public List<RecentItem> getRecentItems() {
		try {
			session = sessionFactory.openSession();
			@SuppressWarnings("unchecked")
			List<RecentItem> results = session.getNamedQuery(
					"findRecentItemsInOrder").list();
			return results;
		} finally {
			session.close();
		}
	}

	@Override
	public void updateCodeItem(CodeItem code) {
		try {
			session = sessionFactory.openSession();
			session.getTransaction().begin();
			session.update(code);
			session.getTransaction().commit();
		} finally {
			session.close();
		}

	}

	@Override
	public void saveCodeItem(CodeItem code) {
		try {
			session = sessionFactory.openSession();
			session.getTransaction().begin();
			session.save(code);
			session.getTransaction().commit();
			session.clear();
		} finally {
			session.close();
		}

	}

	@Override
	public User findUserById(int userId) {
		try {
			session = sessionFactory.openSession();
			@SuppressWarnings("unchecked")
			List<User> dataset = session
					.createQuery("from User where USER_ID=:id")
					.setParameter("id", userId).list();
			if (dataset.size() == 1) {
				return dataset.get(0);
			} else {
				return null;
			}
		} finally {
			session.close();
		}
	}

	@Override
	public User findUserByEmail(String email) {
		try {
			session = sessionFactory.openSession();
			@SuppressWarnings("unchecked")
			List<User> dataset = session
					.createQuery("from User where USER_EMAIL = :email")
					.setParameter("email", email).list();
			if (dataset.size() == 1) {
				return dataset.get(0);
			} else {
				return null;
			}
		} finally {
			session.close();
		}
	}

	@Override
	public User findUserByName(String username) {
		try {
			session = sessionFactory.openSession();
			@SuppressWarnings("unchecked")
			List<User> dataset = session
					.createQuery("from User where USER_NAME = :userName")
					.setParameter("userName", username).list();
			if (dataset.size() == 1) {
				return dataset.get(0);
			} else {
				return null;
			}
		} finally {
			session.close();
		}
	}

	@Override
	public List<UserLanguageStatisticsItem> findUserLanguageStatistics(
			int userId) {
		try {
			session = sessionFactory.openSession();
			Query q = session.getNamedQuery("thisUserLanguageStatistics");
			q.setParameter("t_id", userId);
			@SuppressWarnings("unchecked")
			List<UserLanguageStatisticsItem> dataset = q.list();
			return dataset;
		} finally {
			session.close();
		}
	}

	@Override
	public void saveUser(User user) {
		try {
			session = sessionFactory.openSession();
			session.getTransaction().begin();
			session.save(user);
			session.getTransaction().commit();
		} finally {
			session.close();
		}
	}

	@Override
	public List<SearchItem> searchDatabasePublic(String query, int limit,
			int offset) {
		try {
			session = sessionFactory.openSession();
			Query q = session.getNamedQuery("publicSearchQuery")
					.setParameter("query", query).setParameter("limit", limit)
					.setParameter("offset", offset);
			@SuppressWarnings("unchecked")
			List<SearchItem> dataset = q.list();
			return dataset;

		} finally {
			session.close();
		}
	}

	@Override
	public List<SearchItem> searchDatabaseUser(String query, int limit,
			int offset, int userId) {
		try {
			session = sessionFactory.openSession();
			Query q = session.getNamedQuery("userSearchQuery")
					.setParameter("query", query).setParameter("limit", limit)
					.setParameter("offset", offset)
					.setParameter("user_id", userId);
			@SuppressWarnings("unchecked")
			List<SearchItem> dataset = q.list();
			return dataset;

		} finally {
			session.close();
		}
	}

	@Override
	public List<SearchItem> searchDatabaseAdmin(String query, int limit,
			int offset) {
		try {
			session = sessionFactory.openSession();
			Query q = session.getNamedQuery("adminSearchQuery")
					.setParameter("query", query).setParameter("limit", limit)
					.setParameter("offset", offset);
			@SuppressWarnings("unchecked")
			List<SearchItem> dataset = q.list();
			return dataset;

		} finally {
			session.close();
		}
	}

}
