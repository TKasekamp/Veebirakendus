package datastore;

import data.LoginResponse;
import data.User;

public interface UserDataProvider {
	public User findUserById(int id);

	public void addUser(User item);

	public LoginResponse checkPassword(User user);

	public String generateSID();

	public int logOut(LoginResponse r);

	public int getUserWithSID(String SID);

}
