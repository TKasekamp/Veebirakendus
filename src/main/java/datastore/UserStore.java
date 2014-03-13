package datastore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.User;

public class UserStore implements UserDataProvider {
	private final Map<Integer, User> users;
	private int userCounter;

	public UserStore() {
		users = new HashMap<>();
		users.put(1, new User(1, "User", "fuckoff@gmail.com", "12345"));
		userCounter = 2;
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

}
