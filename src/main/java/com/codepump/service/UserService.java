package com.codepump.service;

import com.codepump.data.User;
import com.codepump.service.impl.UserServiceImpl;
import com.codepump.tempobject.UserStatisticsItem;
import com.google.inject.ImplementedBy;

/**
 * Handles the adding of users to the database.
 * 
 * @author TKasekamp
 * 
 */
@ImplementedBy(value = UserServiceImpl.class)
public interface UserService {
	/**
	 * Searches for this user in the DB.
	 * 
	 * @param id
	 * @return User
	 */
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
	 * Adds an user to the database. Checks if this user already exists with
	 * findUserByEmail.
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

	/**
	 * First checks if there is such an user logged in. Uses the ID to then get
	 * this user from DB.
	 * 
	 * @param SID
	 *            session ID
	 * @return null if no such user logged in <br>
	 *         User if succesful
	 */
	public User findUserBySID(String SID);

	/**
	 * Checks the database for a user with this email.
	 * 
	 * @param email
	 *            Email of user
	 * @return this user if found<br>
	 *         null if not found
	 */
	public User findUserByEmail(String email);

	/**
	 * For dependency injection. Used by Google Guice.
	 * 
	 * @author TKasekamp
	 * 
	 */
	public interface UserServiceFactory {
		public UserServiceImpl create();
	}
}
