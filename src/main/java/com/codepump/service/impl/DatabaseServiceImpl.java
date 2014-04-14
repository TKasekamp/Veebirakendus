package com.codepump.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.codepump.data.CodeItem;
import com.codepump.data.User;
import com.codepump.service.DatabaseService;
import com.codepump.tempobject.MyStuffListItem;
import com.codepump.tempobject.RecentItem;
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
							"from CodeItem where PRIVACY='Public' order by created_date desc")
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
	public void searchDatabase(String query, int limit, int offset) {
		try {
			session = sessionFactory.openSession();
			Query q = session.createSQLQuery("WITH q AS (SELECT plainto_tsquery('user') AS query), ranked AS ("
					+ " SELECT code_id, code_name, code_language, code_text, ts_rank('{0.1, 0.2, 0.4, 1.0}',tsv, query) AS rank "
					+ "FROM codeitem, q "
					+ "WHERE q.query @@ tsv "
					+ "ORDER BY rank DESC "
					+ "LIMIT 5 OFFSET 0 )"
					+ "SELECT code_id, code_name, code_language, ts_headline(code_text, q.query, 'MaxWords=75,MinWords=25,ShortWord=3,MaxFragments=3,FragmentDelimiter=\"||||\"') "
							+ "FROM ranked, q "
							+ "ORDER BY ranked DESC");
//			q.setParameter("query", query);
//			q.setParameter("limit", limit);
//			q.setParameter("offset", offset);
//			q.executeUpdate();
			ArrayList<String> s = (ArrayList<String>) q.list();
			System.out.println(s.toString());
//			String  s = (String) q.getFirstResult();
//			for (int i = 0; i < s.size(); i++) {
//				System.out.println(s.get(i));
//			}
			
		} finally {
			session.close();
		}
		
	}

}
