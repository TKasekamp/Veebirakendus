package datastore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.hibernate.Session;

import util.HibernateUtil;
import data.LoginResponse;
import data.User;

public class UserStore implements UserDataProvider {
	private Map<Integer, User> users;
	private int userCounter;
	private Map<String, Integer> SIDlist; // SID, corresponding user ID

	// Database connection stuff
	private final boolean USE_DATABASE = MemoryStore.USE_DATABASE;
	private Session session;

	public UserStore() {
		if (USE_DATABASE) {
			session = HibernateUtil.currentSession();

		} else {
			users = new HashMap<>();
			users.put(1, new User(1, "User", "fuckoff@gmail.com", "12345"));
			users.put(2, new User(2, "test", "the1whokn0cks@gmail.com",
					"qwerty"));
			userCounter = 3;

		}
		SIDlist = new HashMap<>();
	}

	@Override
	public void addUser(User item) {
		if (USE_DATABASE) {
			session.getTransaction().begin();
			session.save(item);
			session.getTransaction().commit();
		} else {
			item.setId(userCounter); // temp hack
			users.put(userCounter, item);
			userCounter++;
			System.out.println("Added user. List now contains:");
			System.out.println(users);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public User findUserById(int id) {
		if (USE_DATABASE) {
			// TODO Guard from SQL injection
			List<User> dataset = session.createQuery(
					"from User where USER_ID='" + Integer.toString(id) + "'")
					.list();
			return dataset.get(0);
		} else {
			return users.get(id);
		}
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
		if (USE_DATABASE) {
			// TODO Guard from SQL injection
			@SuppressWarnings("unchecked")
			List<User> dataset = session.createQuery(
					"from User where USER_NAME='" + user.getName() + "'")
					.list();
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
