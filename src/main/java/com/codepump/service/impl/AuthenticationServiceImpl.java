package com.codepump.service.impl;

import java.util.Date;
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
import com.codepump.tempobject.MyStuffListItem;
import com.codepump.tempobject.ResultContainer;
import com.google.inject.Inject;

public class AuthenticationServiceImpl implements AuthenicationService {
	private static Map<String, Integer> SIDlist;
	private DatabaseService dbServ; // NULL if USE_DATABASE = true

	/**
	 * @param dbServ
	 *            Injected by Guice
	 */
	@Inject
	public AuthenticationServiceImpl(DatabaseService dbServ) {
		this.dbServ = dbServ;
		if (SIDlist == null)
			SIDlist = new HashMap<>();
	}

	/**
	 * The reason why this class exists. Logs user in if password is correct.
	 * Updates lastlogin date upon succesful login. <br>
	 * 0 - there is no such user <br>
	 * 1 - user and password are correct, but already logged in. NOT USED<br>
	 * 2 - wrong password <br>
	 * 3 - user succesfully logged in
	 */
	@Override
	public AuthenticationResponse checkPassword(User user) {
		int result = 0;
		String sid = null;
		user.hashPassword();

		User dbUser = dbServ.findUserByEmail(user.getEmail());
		try {
			System.out.println("Got user from DB");
			System.out.println(dbUser);
			if (dbUser.getPassword().equals(user.getPassword())) {
				result = 1;
			} else {
				result = 2;
			}
		} catch (Exception e) {
			// This means there is no such user in DB
		}

		// Check if the user has logged in
		if (result == 1) {
			sid = login(dbUser);
			result = 3;
			dbUser.setLastLoginDate(new Date());
			dbServ.updateUser(dbUser);
		}
		return new AuthenticationResponse(result, sid);
	}

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
	public void logOut(String SID) {
		int id = SIDlist.remove(SID);
		if (ServerController.AUTOMATED_TESTING) {
			testCleanUp(id);
		}
	}

	@Override
	public int getUserIdWithSID(String SID) {
		int id = -1;
		try {
			id = SIDlist.get(SID);
		} catch (Exception e) {
		}
		return id;
	}

	@Override
	public boolean authoriseEdit(EditContainer item) {
		int userID = getUserIdWithSID(item.getSID());
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
	public String directLogin(String email) {
		User user = dbServ.findUserByEmail(email);
		return login(user);
	}

	@Override
	public String googleLogin(User user) {
		AuthenticationResponse r = checkPassword(user);
		// If no user create new one
		if (r.getResponse() == 0) {
			user.setAdminStatus(0);
			user.setCreateDate(new Date());
			user.setLastLoginDate(new Date());
			dbServ.saveUser(user);
		}
		String SID = this.directLogin(user.getEmail());
		return SID;
	}

	@Override
	public Map<String, Integer> getSidList() {
		return SIDlist;
	}

	/**
	 * Cleans up after Selenium tests. Removes test user and all his created
	 * items.
	 * 
	 * @param userId
	 */
	private void testCleanUp(int userId) {
		User user = dbServ.findUserById(userId);
		if (user.getEmail().equals("test1@email.com")) {
			ResultContainer<MyStuffListItem> l = dbServ.getAllUserItems(userId, 1000, 0);
			for (MyStuffListItem myStuffListItem : l.getCodeList()) {
				dbServ.deleteCodeItem(myStuffListItem.getCodeId());
			}
			dbServ.deleteUser(userId);
		}
	}

	/**
	 * Generates SID and adds user to logged in user list.
	 * 
	 * @param user
	 *            to be logged in
	 * @return SID, cookie value
	 */
	private String login(User user) {
		String sid = generateSID();
		SIDlist.put(sid, user.getId());
		return sid;
	}

}
