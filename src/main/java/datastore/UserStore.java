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
	private final Map<Integer, String> SIDlist;

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
	 * Finds a name from the list and checks password. 
	 * 0 - there is no such user
	 * 1 - user and password correct, but already logged in
	 * 2 - wrong password
	 * 3 - user succesfully logged in
	 */
	@Override
	public LoginResponse checkPassword(User user) {
		int result = 0;
		int userID = -1;
		String sid = null;
		
		System.out.println(user);
		// Find user
		for (User value : users.values()) {
			System.out.println(value);
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
		// Check if user has logged in
		if ((result == 1) &&(!SIDlist.containsKey(userID))) {
			// This bit here to make sure we generated a random ID
			// Maybe overkill
			while (true) {
				sid = generateSID();
				if (!SIDlist.containsKey(userID)) {
					break;
				}
			}

			SIDlist.put(userID, sid);
			result = 3;
			System.out.println(SIDlist);
		}
		return new LoginResponse(result, sid);
	}
	
	/**
	 * Generates random string
	 */
	public String generateSID(){
		String temp = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		char[] lst = temp.toCharArray();
		
		String SID = "";
		Random rand = new Random();
		for(int i = 0; i < 32; i++){
			int nr = rand.nextInt(lst.length);
			SID += lst[nr];
		}return SID;
	}

}
