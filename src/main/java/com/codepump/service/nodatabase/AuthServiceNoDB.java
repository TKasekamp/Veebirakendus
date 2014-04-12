package com.codepump.service.nodatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.codepump.data.User;
import com.codepump.response.AuthenticationResponse;
import com.codepump.service.AuthenicationService;
import com.codepump.tempobject.EditContainer;

/**
 * This is the AuthService when no database is attached. Google login and
 * editing code is not implemented because I can't be bothered. Basic functions
 * still work.
 * 
 * @author TKasekamp
 * 
 */
public class AuthServiceNoDB implements AuthenicationService {
	private static Map<String, Integer> SIDlist = new HashMap<>();
	private static Map<Integer, User> users;

	@Override
	public AuthenticationResponse checkPassword(User user) {
		users = UserServiceNoDB.users;
		int result = 0;
		int userID = -1;
		String sid = null;
		user.hashPassword();
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
		if (result == 1) {
			sid = generateSID();
			SIDlist.put(sid, userID);
			result = 3;
		}
		return new AuthenticationResponse(result, sid);
	}

	@Override
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
		SIDlist.remove(SID);
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
		System.err.println("Editing code unavailable in server memory mode.");
		return false;
	}

	@Override
	public String directLogin(String email) {
		return null;
	}

	@Override
	public String googleLogin(User user) {
		System.err.println("Google login unavailable in server memory mode.");
		return null;
	}

	@Override
	public Map<String, Integer> getSidList() {
		return SIDlist;
	}

}
