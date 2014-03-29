package com.codepump.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

import com.codepump.controller.ServerController;
import com.codepump.data.User;
import com.codepump.service.UserService;
import com.codepump.tempobject.UserLanguageStatisticsItem;
import com.codepump.tempobject.UserStatisticsItem;
import com.codepump.util.HibernateUtil;

public class UserServiceImpl implements UserService {
	public static Map<Integer, User> users;
	private int userCounter;

	// Database connection stuff
	private final boolean USE_DATABASE = ServerController.USE_DATABASE;
	private Session session;

	public UserServiceImpl() {
		if (USE_DATABASE) {
			session = HibernateUtil.currentSession();

		} else {
			users = new HashMap<>();
			users.put(1, new User(1, "User", "fuckoff@gmail.com", "12345"));
			users.put(2, new User(2, "test", "the1whokn0cks@gmail.com",
					"qwerty"));
			userCounter = 3;
			for (User user : users.values()) {
				user.hashPassword();
			}
			System.out.println("Users are: " + users.toString());

		}

	}

	@Override
	public int addUser(User item) {
		int result = 0;
		if (USE_DATABASE) {
			if (findUserByName(item.getName()) == null) {
				session.getTransaction().begin();
				session.save(item);
				session.getTransaction().commit();
			} else {
				result = 1;
			}

		} else {
			item.setId(userCounter); // temp hack
			users.put(userCounter, item);
			userCounter++;
			System.out.println("Added user. List now contains:");
			System.out.println(users);
		}
		return result;

	}

	@SuppressWarnings("unchecked")
	@Override
	public User findUserById(int id) {
		if (USE_DATABASE) {
			List<User> dataset = session
					.createQuery("from User where USER_ID=:id")
					.setParameter("id", id).list();
			return dataset.get(0);
		} else {
			return users.get(id);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public UserStatisticsItem findUserStatistics(String SID) {
		int userID = ServerController.authenticationServer.getUserWithSID(SID);
		// userID will be set to -1 if no such SID can be found. This is the
		// public user and as such Statistics should not work
		if (userID == -1) {
			return null;
		}
		if (USE_DATABASE) {
			// Creating a query and setting a parameter after.
			Query q = session.getNamedQuery("thisUserLanguageStatistics");
			q.setParameter("t_id", userID);
			List<UserLanguageStatisticsItem> dataset = q.list();
			// Without this the query will always return the same things
			session.clear();
			// As the dataset is empty, creating new item by searching for user
			if (dataset.size() == 0) {
				User user = findUserById(userID);
				return new UserStatisticsItem(user.getId(), user.getName(), 0);
			}
			// UserStatisticsItem is the container for this query's results
			return new UserStatisticsItem(dataset);
		} else {
			List<UserLanguageStatisticsItem> dataset = new ArrayList<UserLanguageStatisticsItem>();
			dataset.add(new UserLanguageStatisticsItem(2, "test", 2, "Java"));
			dataset.add(new UserLanguageStatisticsItem(2, "test", 4, "Python"));
			dataset.add(new UserLanguageStatisticsItem(2, "test", 2, "SQL"));
			return new UserStatisticsItem(dataset);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public User findUserByName(String username) {
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
	public User findUserBySID(String SID) {
		int userID = ServerController.authenticationServer.getUserWithSID(SID);
		if (userID == -1) {
			return null;
		}
		User user = findUserById(userID);
		return user;
	}

}