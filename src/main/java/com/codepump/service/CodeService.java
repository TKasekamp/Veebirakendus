package com.codepump.service;

import java.util.List;

import com.codepump.data.CodeItem;
import com.codepump.tempobject.EditContainer;
import com.codepump.tempobject.MyStuffListItem;
import com.codepump.tempobject.RecentItem;

/**
 * Handles all codeitem related requests from the database.
 * 
 * @author TKasekamp
 * 
 */
public interface CodeService {

	public CodeItem findItemById(int id);

	public List<CodeItem> getAllCodeItems();

	/**
	 * Adds this code to the DB. Uses SID to find user.
	 * 
	 * @param item
	 *            CodeItem
	 * @param SID
	 *            Cookie value
	 * @return
	 */
	public Boolean addCode(CodeItem item, String SID);

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
	 * The most recent codes in the database.<br>
	 * 
	 * @return List of RecentItems. Contains 4 items.
	 */
	public List<RecentItem> getRecentItems();

	/**
	 * Uses AuthenticationService to find the id of this user. Then searches the
	 * DB for all code made by this user.
	 * 
	 * @return All CodeItems by this user.<br>
	 *         null if no code was found.
	 */
	public List<MyStuffListItem> getAllUserItems(String SID);

	/**
	 * Deletes this code from the DB.
	 * 
	 * @param id
	 *            of code to be deleted.
	 */
	public void deleteCode(int id);

	public Boolean isValid(CodeItem item);

}
