package com.codepump.service;

import com.codepump.data.User;
import com.codepump.data.container.UserStatisticsContainer;

/**
 * Handles the adding of users to the database.
 * 
 * @author TKasekamp
 * 
 */
public interface UserService {
	/**
	 * Searches for this user in the DB.
	 * 
	 * @param id
	 * @return User<br>
	 *         null if not found
	 */
	public User findUserById(int id);

	/**
	 * Checks the database for a user with this name.
	 * 
	 * @param username
	 *            Name of user
	 * @return this user if found<br>
	 *         null if not found
	 */
	public User findUserByName(String username);

	/**
	 * Adds an user to the database. Hashes password. Checks if this user
	 * already exists with findUserByEmail. Gives this user a SID if signup
	 * succesful.
	 * 
	 * @param item
	 *            new User
	 * @return String SID cookie if succesful<br>
	 *         null if user exists
	 */
	public String addUser(User item);

	/**
	 * Returns statistics about this's users activities.
	 * 
	 * @param SID
	 *            Cookie value
	 * @return {@link UserStatisticsContainer} with language statistics if this user
	 *         has made any code.<br>
	 *         {@link UserStatisticsContainer} with with no language statistics if no
	 *         code found.
	 */
	public UserStatisticsContainer generateUserStatistics(String SID);

	/**
	 * First checks if there is such an user logged in. Uses the ID to then get
	 * this user from DB.
	 * 
	 * @param SID
	 *            session ID
	 * @return {@link User} if found <br>
	 *         null if no such user logged in
	 */
	public User findUserBySID(String SID);

	/**
	 * Checks the database for a user with this email.
	 * 
	 * @param email
	 *            Email of user
	 * @return {@link User} if found<br>
	 *         null if not found
	 */
	public User findUserByEmail(String email);
}
