package com.codepump.service;

import java.util.List;

import org.hibernate.exception.ConstraintViolationException;

import com.codepump.data.CodeItem;
import com.codepump.data.User;
import com.codepump.service.impl.DatabaseServiceImpl;
import com.codepump.tempobject.MyStuffListItem;
import com.codepump.tempobject.RecentItem;
import com.codepump.tempobject.SearchItem;
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
	 * @param limit
	 *            number of items to retrieve
	 * @param offset
	 *            from where to start counting
	 * @return CodeItem List
	 */
	public List<CodeItem> getAllCodeItems(int limit, int offset);

	/**
	 * Searches the DB for all CodeItems made by this user. Formats them as
	 * MyStuffListItem. Query specified in {@link MyStuffListItem}.
	 * 
	 * @param userId
	 * @param limit
	 *            number of items to retrieve
	 * @param offset
	 *            from where to start counting
	 * @return All CodeItems by this user.
	 */
	public List<MyStuffListItem> getAllUserItems(int userId, int limit,
			int offset);

	/**
	 * Returns the most recent items in the DB. Query specified in
	 * {@link RecentItem}.
	 * 
	 * @param limit
	 *            number of items to retrieve
	 * @param offset
	 *            from where to start counting
	 * @return List of RecentItems
	 */
	public List<RecentItem> getRecentItems(int limit, int offset);

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
	 * Searches the database for with specified query. Returns only Public
	 * texts.
	 * 
	 * @param query
	 *            Stuff to search for
	 * @param limit
	 *            How many items to display
	 * @param offset
	 *            On which page you are I think
	 * @return List of {@link SearchItem} as the results of this query
	 */
	public List<SearchItem> searchDatabasePublic(String query, int limit,
			int offset);

	/**
	 * Searches the database for with specified query. Returns only Public texts
	 * or the ones which the user has created
	 * 
	 * @param query
	 *            Stuff to search for
	 * @param limit
	 *            How many items to display
	 * @param offset
	 *            On which page you are I think
	 * @param userId
	 *            Logged in user id
	 * @return List of {@link SearchItem} as the results of this query
	 */
	public List<SearchItem> searchDatabaseUser(String query, int limit,
			int offset, int userId);

	/**
	 * Searches the database for with specified query. Returns all texts made by
	 * all users.
	 * 
	 * @param query
	 *            Stuff to search for
	 * @param limit
	 *            How many items to display
	 * @param offset
	 *            On which page you are I think
	 * @return List of {@link SearchItem} as the results of this query
	 */
	public List<SearchItem> searchDatabaseAdmin(String query, int limit,
			int offset);

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
	 * @param user {@link User} to be updated
	 */
	public void updateUser(User user);
}
