package com.codepump.service;

import com.codepump.data.User;

/**
 * Handles the adding of users to the database.
 * 
 * @author TKasekamp
 * 
 */
public interface UserService {
	public User findUserById(int id);

	public void addUser(User item);

}
