package com.codepump.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.codepump.controller.ServerController;
import com.codepump.data.User;
import com.codepump.service.AuthenicationService;
import com.codepump.service.DatabaseService;
import com.codepump.service.UserService;
import com.codepump.tempobject.UserLanguageStatisticsItem;
import com.codepump.tempobject.UserStatisticsItem;
import com.google.inject.Inject;

public class UserServiceImpl implements UserService {
	public static Map<Integer, User> users;
	private int userCounter;

	// Database connection stuff
	private final boolean USE_DATABASE = ServerController.USE_DATABASE;
	private DatabaseService dbServ;
	private AuthenicationService authServ;

	/**
	 * Used when running in server memory.
	 */
	public UserServiceImpl() {
		users = new HashMap<>();
		users.put(1, new User(1, "User", "fuckoff@gmail.com", "12345"));
		users.put(2, new User(2, "test", "the1whokn0cks@gmail.com", "qwerty"));
		userCounter = 3;
		for (User user : users.values()) {
			user.hashPassword();
		}
		System.out.println("Users are: " + users.toString());
		authServ = new AuthenticationServiceImpl();
	}

	/**
	 * Used when DB activated.
	 * @param dbServ Injected by Guice
	 */
	@Inject
	public UserServiceImpl(final DatabaseService dbServ, final AuthenicationService authServ) {
		this.dbServ = dbServ;
		this.authServ = authServ;
	}

	@Override
	public int addUser(User item) {
		int result = 0;
		if (USE_DATABASE) {
			if (findUserByEmail(item.getEmail()) == null) {
				dbServ.saveUser(item);
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

	@Override
	public User findUserById(int id) {
		if (USE_DATABASE) {
			return dbServ.findUserById(id);
		} else {
			return users.get(id);
		}
	}

	@Override
	public UserStatisticsItem findUserStatistics(String SID) {
		int userID = authServ.getUserWithSID(SID);
		// userID will be set to -1 if no such SID can be found. This is the
		// public user and as such Statistics should not work
		if (userID == -1) {
			return null;
		}
		if (USE_DATABASE) {
			// Creating a query and setting a parameter after.
			List<UserLanguageStatisticsItem> dataset = dbServ
					.findUserLanguageStatistics(userID);
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

	@Override
	public User findUserByName(String username) {
		return dbServ.findUserByName(username);
	}

	@Override
	public User findUserBySID(String SID) {
		int userID = authServ.getUserWithSID(SID);
		if (userID == -1) {
			return null;
		}
		User user = findUserById(userID);
		return user;
	}

	@Override
	public User findUserByEmail(String email) {
		return dbServ.findUserByEmail(email);
	}

}