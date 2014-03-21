package com.codepump.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.hibernate.Session;

import com.codepump.controller.ServerController;
import com.codepump.data.User;
import com.codepump.response.AuthenticationResponse;
import com.codepump.service.AuthenicationService;
import com.codepump.util.HibernateUtil;

public class AuthenticationServiceImpl implements AuthenicationService {
	private Map<String, Integer> SIDlist; // SID, corresponding user ID
	private final boolean USE_DATABASE = ServerController.USE_DATABASE;
	private Session session;
	private static Map<Integer, User> users = UserServiceImpl.users;

	public AuthenticationServiceImpl() {
		if (USE_DATABASE) {
			session = HibernateUtil.currentSession();

		}
		SIDlist = new HashMap<>();
	}

	/**
	 * Finds a name from the list and checks password. <br>
	 * 0 - there is no such user <br>
	 * 1 - user and password are correct, but already logged in. NOT USED<br>
	 * 2 - wrong password <br>
	 * 3 - user succesfully logged in
	 */
	@Override
	public AuthenticationResponse checkPassword(User user) {
		int result = 0;
		int userID = -1;
		String sid = null;

		// Find user
		if (USE_DATABASE) {
			@SuppressWarnings("unchecked")
			List<User> dataset = session
					.createQuery("from User where USER_NAME = :userName")
					.setParameter("userName", user.getName()).list();
			try {
				System.out.println("Got user from DB");
				System.out.println(dataset.get(0));
				if (dataset.get(0).getPassword().equals(user.getPassword())) {
					result = 1;
					userID = dataset.get(0).getId();
				} else {
					result = 2;
				}
			} catch (IndexOutOfBoundsException e) {
				// This means there is no such user in DB
			}

		} else {
			System.out.println("Trying to log in: " + user.toString());
			for (User value : users.values()) {

				if (value.getName().equals(user.getName())) {
					if (value.getPassword().equals(user.getPassword())) {
						result = 1;
						userID = value.getId();

					} else {
						result = 2;
					}
					break;

				}
			}
		}
		// Check if the user has logged in
		if (result == 1) {
			// This bit here to make sure we generated a random ID
			// May be overkill
			while (true) {
				sid = generateSID();
				if (!SIDlist.containsKey(sid)) {
					break;
				}
			}

			SIDlist.put(sid, userID);
			result = 3;
		}
		return new AuthenticationResponse(result, sid);
	}

	/**
	 * Generates random string
	 */
	public String generateSID() {
		String temp = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		char[] lst = temp.toCharArray();

		String SID = "";
		Random rand = new Random();
		for (int i = 0; i < 32; i++) {
			int nr = rand.nextInt(lst.length);
			SID += lst[nr];
		}
		return SID;
	}

	@Override
	public int logOut(AuthenticationResponse r) {
		System.out.println("Logged out userID:" + SIDlist.remove(r.getSID()));
		return 0;
	}

	@Override
	public int getUserWithSID(String SID) {
		int id = -1;
		try {
			id = SIDlist.get(SID);
		} catch (Exception e) {
			System.out.println("no such user logged in");
		}
		return id;
	}

}
