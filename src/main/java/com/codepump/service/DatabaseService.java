package com.codepump.service;

import java.util.List;

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
	 * @return CodeItem List
	 */
	public List<CodeItem> getAllCodeItems();

	/**
	 * Searches the DB for all CodeItems made by this user. Formats them as
	 * MyStuffListItem. Query specified in {@link MyStuffListItem}.
	 * 
	 * @param userId
	 * @return All CodeItems by this user.
	 */
	public List<MyStuffListItem> getAllUserItems(int userId);

	/**
	 * Returns the most recent items in the DB. Query specified in
	 * {@link RecentItem}.
	 * 
	 * @return List of RecentItems
	 */
	public List<RecentItem> getRecentItems();

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
	 * Searches the database for query.
	 * 
	 * @param query
	 *            Stuff to search for
	 * @param limit
	 *            How many items to display
	 * @param offset
	 *            On which page you are I think
	 * @return List of {@link SearchItem} as the results of this query
	 */
	public List<SearchItem> searchDatabase(String query, int limit, int offset);
}
