package com.codepump.service;

import java.util.List;

import org.hibernate.exception.ConstraintViolationException;

import com.codepump.data.CodeItem;
import com.codepump.data.User;
import com.codepump.service.impl.DatabaseServiceImpl;
import com.codepump.tempobject.MyStuffListItem;
import com.codepump.tempobject.RecentItem;
import com.codepump.tempobject.ResultContainer;
import com.codepump.tempobject.UserLanguageStatisticsItem;
import com.codepump.tempobject.UserStatisticsItem;
import com.google.inject.ImplementedBy;

/**
 * All database commands go through here. These are the building blocks on which
 * this app is based. The methods don't handle any excetions.<br>
 * Only to be used in other services.<br>
 * Should be split into separate methods if this class grows too big.
 * 
 * @author TKasekamp
 * 
 */
@ImplementedBy(value = DatabaseServiceImpl.class)
public interface DatabaseService {

	/**
	 * Deletes this code from the DB.
	 * 
	 * @param codeId
	 *            of code to be deleted.
	 */
	public void deleteCodeItem(int codeId);

	/**
	 * Searches the DB for code with this id.
	 * 
	 * @param codeId
	 *            of CodeItem
	 * @return CodeItem <br>
	 *         null if not found
	 */
	public CodeItem findCodeItemById(int codeId);

	/**
	 * All CodeItems from DB. Privacy is Public, sorted in descending order by
	 * created date.
	 * 
	 * @param maxResults
	 *            number of items to retrieve
	 * @param firstResult
	 *            from where to start counting
	 * @return CodeItem {@link ResultContainer}
	 */
	public ResultContainer<CodeItem> getAllCodeItems(int firstResult, int maxResults);

	/**
	 * Searches the DB for all CodeItems made by this user. Formats them as
	 * MyStuffListItem. Query specified in {@link MyStuffListItem}.
	 * 
	 * @param userId
	 * @param firstResult
	 *            from where to start counting
	 * @param maxResults
	 *            number of items to retrieve
	 * @return {@link ResultContatainer} with expected results and List of {@link MyStuffListItem}
	 */
	public ResultContainer<MyStuffListItem> getAllUserItems(int userId, int firstResult, int maxResults);

	/**
	 * Returns the most recent items in the DB. Query specified in
	 * {@link RecentItem}.
	 * 

	 * @param firstResult
	 *            from where to start counting
	 * @param maxResults
	 *            number of items to retrieve
	 * @return List of RecentItems
	 */
	public List<RecentItem> getRecentItems(int firstResult, int maxResults);

	/**
	 * Updates this CodeItem in the DB.
	 * 
	 * @param code
	 *            CodeItem to be updated.
	 */
	public void updateCodeItem(CodeItem code);

	/**
	 * Adds this CodeItem to the DB. Code must include User.<br>
	 * Clears the session after commit.
	 * 
	 * @param code
	 *            CodeItem to be added.
	 */
	public void saveCodeItem(CodeItem code);

	/**
	 * Checks the database for a user with this id.
	 * 
	 * @param userId
	 *            ID of user
	 * @return this user if found<br>
	 *         null if not found
	 */
	public User findUserById(int userId);

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
	 * Checks the database for a user with this name.
	 * 
	 * @param username
	 *            Name of user
	 * @return this user if found<br>
	 *         null if not found
	 */
	public User findUserByName(String username);

	/**
	 * Returns statistics about this's users languages. Proper
	 * {@link UserStatisticsItem} must be constructed using this list. Query
	 * explained in {@link UserLanguageStatisticsItem}.
	 * 
	 * @param userId
	 *            ID of user.
	 */
	public List<UserLanguageStatisticsItem> findUserLanguageStatistics(
			int userId);

	/**
	 * Adds new user to DB. <br>
	 * Email check MUST be done before using this method. Otherwise stuff might
	 * break.
	 * 
	 * @param user
	 *            User
	 */
	public void saveUser(User user);

	/**
	 * Deletes the user with this id from the database. To be safe all code made
	 * by this user has to be deleted before deleteting user.
	 * 
	 * @param userId
	 *            User id to be deleted
	 * @throws ConstraintViolationException
	 *             If this user has created any code. Here because of foreign
	 *             keys in the database.
	 */
	public void deleteUser(int userId) throws ConstraintViolationException;

	/**
	 * Updates this user in the database.
	 * 
	 * @param user
	 *            {@link User} to be updated
	 */
	public void updateUser(User user);

}
