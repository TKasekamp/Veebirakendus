package com.codepump.service.impl;

import java.util.Date;
import java.util.List;

import com.codepump.data.User;
import com.codepump.service.AuthenicationService;
import com.codepump.service.DatabaseService;
import com.codepump.service.UserService;
import com.codepump.tempobject.UserLanguageStatisticsItem;
import com.codepump.tempobject.UserStatisticsItem;
import com.google.inject.Inject;

public class UserServiceImpl implements UserService {
	private DatabaseService dbServ; // NULL if USE_DATABASE = true
	private AuthenicationService authServ;

	/**
	 * @param dbServ
	 *            Injected by Guice
	 * @param authServ
	 *            Injected by Guice
	 */
	@Inject
	public UserServiceImpl(DatabaseService dbServ, AuthenicationService authServ) {
		this.dbServ = dbServ;
		this.authServ = authServ;
	}

	@Override
	public String addUser(User user) {
		user.hashPassword();
		String SID = null;
		if (findUserByEmail(user.getEmail()) == null) {
			user.setAdminStatus(0);
			user.setCreateDate(new Date());
			user.setLastLoginDate(new Date());
			dbServ.saveUser(user);
			SID = authServ.directLogin(user.getEmail());
		}
		return SID;

	}

	@Override
	public User findUserById(int id) {
		return dbServ.findUserById(id);

	}

	@Override
	public UserStatisticsItem generateUserStatistics(String SID) {
		int userID = authServ.getUserIdWithSID(SID);
		// userID will be set to -1 if no such SID can be found. This is the
		// public user and as such Statistics should not work
		if (userID == -1) {
			return null;
		}

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

	}

	@Override
	public User findUserByName(String username) {
		return dbServ.findUserByName(username);
	}

	@Override
	public User findUserBySID(String SID) {
		int userID = authServ.getUserIdWithSID(SID);
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