package com.codepump.service;

import com.codepump.data.User;
import com.codepump.tempobject.UserStatisticsItem;

/**
 * Handles the adding of users to the database.
 * 
 * @author TKasekamp
 * 
 */
public interface UserService {
	public User findUserById(int id);

	/**
	 * Checks the database for a user with this name
	 * 
	 * @param username
	 *            Name of user
	 * @return this user if found<br>
	 *         null if not found
	 */
	public User findUserByName(String username);

	/**
	 * Adds an user to the database. Checks if this user already exists.
	 * 
	 * @param item
	 *            new User
	 * @return 0 if succesful<br>
	 *         1 if user exists
	 */
	public int addUser(User item);

	/**
	 * Returns statistics about this's users activities.
	 * 
	 * @param SID
	 *            Cookie value
	 */
	public UserStatisticsItem findUserStatistics(String SID);
}
