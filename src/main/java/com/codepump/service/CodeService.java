package com.codepump.service;

import java.util.List;

import com.codepump.data.CodeItem;
import com.codepump.service.impl.CodeServiceImpl;
import com.codepump.tempobject.EditContainer;
import com.codepump.tempobject.MyStuffListItem;
import com.codepump.tempobject.RecentItem;
import com.google.inject.ImplementedBy;

/**
 * Handles all codeitem related requests from the database.
 * 
 * @author TKasekamp
 * 
 */
@ImplementedBy(value = CodeServiceImpl.class)
public interface CodeService {

	public CodeItem findItemById(int id);

	public List<CodeItem> findAllItems();

	/**
	 * Adds this Code to the database.
	 * 
	 * @param item
	 *            CodeItem
	 */
	public void addCode(CodeItem item);

	/**
	 * Edits the text of the item with this id in the database.<br>
	 * Checks with {@link AuthenicationService} to make sure the item belongs to
	 * the user who made it<br>
	 * Only logged in users can edit code<br>
	 * 
	 * @param item
	 *            EdiContainer
	 */
	public void editCode(EditContainer item);

	/**
	 * The most recent codes in the database.
	 * 
	 * @return List of RecentItems
	 */
	public List<RecentItem> getRecentItems();

	/**
	 * Uses AuthenticationService to find the id of this user. Then searches the
	 * DB for all code made by this user.
	 * 
	 * @return All CodeItems by this user.
	 */
	public List<MyStuffListItem> getAllUserItems(String SID);

	public RecentItem getLastRecentItem();

	/**
	 * Deletes this code from the DB.
	 * 
	 * @param id
	 *            of code to be deleted.
	 */
	public void deleteCode(int id);

	/**
	 * For dependency injection. Used by Google Guice.
	 * 
	 * @author TKasekamp
	 * 
	 */
	public interface CodeServiceFactory {
		public CodeServiceImpl create();
	}

}
