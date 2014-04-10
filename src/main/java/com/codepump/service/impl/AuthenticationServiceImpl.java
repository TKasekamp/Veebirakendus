package com.codepump.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.codepump.controller.ServerController;
import com.codepump.data.CodeItem;
import com.codepump.data.User;
import com.codepump.response.AuthenticationResponse;
import com.codepump.service.AuthenicationService;
import com.codepump.service.DatabaseService;
import com.codepump.tempobject.EditContainer;
import com.google.inject.Inject;

public class AuthenticationServiceImpl implements AuthenicationService {
	private static Map<String, Integer> SIDlist = new HashMap<>(); // SID, corresponding user ID
	private final boolean USE_DATABASE = ServerController.USE_DATABASE;
	private static Map<Integer, User> users = UserServiceImpl.users;
	private DatabaseService dbServ; // NULL if USE_DATABASE = true
	
	/**
	 * @param dbServ Injected by Guice
	 */
	@Inject
	public AuthenticationServiceImpl(DatabaseService dbServ) {
		if(USE_DATABASE)
			this.dbServ = dbServ;
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
			User dbUser = dbServ.findUserByEmail(user.getEmail());
			try {
				System.out.println("Got user from DB");
				System.out.println(dbUser);
				if (dbUser.getPassword().equals(user.getPassword())) {
					result = 1;
					userID = dbUser.getId();
				} else {
					result = 2;
				}
			} catch (Exception e) {
				// This means there is no such user in DB
			}

		} else {
			System.out.println("Trying to log in: " + user.toString());
			for (User value : users.values()) {

				if (value.getEmail().equals(user.getEmail())) {
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

	@Override
	public boolean authoriseEdit(EditContainer item) {
		int userID = getUserWithSID(item.getSID());
		if (userID == -1) {
			return false;
		}
		CodeItem code = dbServ.findCodeItemById(item.getId());
		if (userID == code.getUser().getId()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean authoriseEdit(String SID, int codeID) {
		int userID = getUserWithSID(SID);
		if (userID == -1) {
			return false;
		}
		CodeItem code = dbServ.findCodeItemById(codeID);
		if (userID == code.getUser().getId()) {
			return true;
		}
		return false;
	}

	@Override
	public String directLogin(String email) {
		User user = dbServ.findUserByEmail(email);
		String sid = null;
		while (true) {
			sid = generateSID();
			if (!SIDlist.containsKey(sid)) {
				break;
			}
		}

		SIDlist.put(sid, user.getId());
		return sid;
	}

	@Override
	public String googleLogin(User user) {
		AuthenticationResponse r = checkPassword(user);
		// If no user create new one
		if (r.getResponse() == 0) {
			dbServ.saveUser(user);
			r = checkPassword(user);
		}
		return r.getSID();
	}

}
