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

	public void addUser(User item);

	/**
	 * Returns statistics about this's users activities.
	 * 
	 * @param SID
	 *            Cookie value
	 */
	public UserStatisticsItem findUserStatistics(String SID);
}
