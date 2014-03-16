package datastore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import data.LoginResponse;
import data.User;

public class UserStore implements UserDataProvider {
	private final Map<Integer, User> users;
	private int userCounter;
	private final Map<String, Integer> SIDlist; // SID, corresponding user ID

	public UserStore() {
		users = new HashMap<>();
		users.put(1, new User(1, "User", "fuckoff@gmail.com", "12345"));
		users.put(2, new User(2, "test", "the1whokn0cks@gmail.com", "qwerty"));
		userCounter = 3;
		SIDlist = new HashMap<>();
	}

	@Override
	public void addUser(User item) {
		item.setId(userCounter); // temp hack
		users.put(userCounter, item);
		userCounter++;
		System.out.println("Added user. List now contains:");
		System.out.println(users);

	}

	@Override
	public User findUserById(int id) {
		return users.get(id);
	}

	@Override
	public List<User> findAllUsers() {
		return new ArrayList<>(users.values());
	}

	/**
	 * Finds a name from the list and checks password. <br>
	 * 0 - there is no such user <br>
	 * 1 - user and password are correct, but already logged in. NOT USED<br>
	 * 2 - wrong password <br>
	 * 3 - user succesfully logged in
	 */
	@Override
	public LoginResponse checkPassword(User user) {
		int result = 0;
		int userID = -1;
		String sid = null;

		// Find user
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
		return new LoginResponse(result, sid);
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
	public int logOut(LoginResponse r) {
		System.out.println("Logged out userID:" + SIDlist.remove(r.getSID()));
		return 0;
	}

}
