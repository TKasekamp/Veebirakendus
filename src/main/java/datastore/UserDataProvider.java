package datastore;

import java.util.List;

import data.LoginResponse;
import data.User;

public interface UserDataProvider {

	public User findUserById(int id);

	public List<User> findAllUsers();

	public void addUser(User item);
	
	public LoginResponse checkPassword(User user);
	
	public String generateSID();
	
	public int logOut(LoginResponse r);

}
