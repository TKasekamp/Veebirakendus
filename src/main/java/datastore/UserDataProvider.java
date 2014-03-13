package datastore;

import java.util.List;

import data.User;

public interface UserDataProvider {

	public User findUserById(int id);

	public List<User> findAllUsers();

	public void addUser(User item);

}
