package com.codepump.service.nodatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.codepump.data.User;
import com.codepump.service.AuthenicationService;
import com.codepump.service.UserService;
import com.codepump.tempobject.UserLanguageStatisticsItem;
import com.codepump.tempobject.UserStatisticsItem;
import com.google.inject.Inject;

public class UserServiceNoDB implements UserService {
	public static Map<Integer, User> users;
	private int userCounter;
	private AuthenicationService authServ;

	/**
	 * @param authServ
	 *            Injected by Guice
	 */
	@Inject
	public UserServiceNoDB(AuthenicationService authServ) {
		users = new HashMap<>();
		users.put(1, new User(1, "User", "fuckoff@gmail.com", "12345"));
		users.put(2, new User(2, "test", "the1whokn0cks@gmail.com", "qwerty"));
		userCounter = 3;
		for (User user : users.values()) {
			user.hashPassword();
		}
		System.out.println("Users are: " + users.toString());

		this.authServ = authServ;
	}

	@Override
	public User findUserById(int id) {
		return users.get(id);
	}

	@Override
	public User findUserByName(String username) {
		// Not used anywhere
		return null;
	}

	@Override
	public String addUser(User user) {
		String SID = null;
		user.setId(userCounter);
		user.hashPassword();
		users.put(userCounter, user);
		userCounter++;
		System.out.println("Added user. List now contains:");
		System.out.println(users);
		SID = authServ.directLogin(user.getEmail());
		return SID;
	}

	@Override
	public UserStatisticsItem generateUserStatistics(String SID) {
		int userID = authServ.getUserIdWithSID(SID);
		if (userID == -1) {
			return null;
		}
		List<UserLanguageStatisticsItem> dataset = new ArrayList<UserLanguageStatisticsItem>();
		dataset.add(new UserLanguageStatisticsItem(2, "test", 2, "Java"));
		dataset.add(new UserLanguageStatisticsItem(2, "test", 4, "Python"));
		dataset.add(new UserLanguageStatisticsItem(2, "test", 2, "SQL"));
		return new UserStatisticsItem(dataset);
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
		return null;
	}

}
