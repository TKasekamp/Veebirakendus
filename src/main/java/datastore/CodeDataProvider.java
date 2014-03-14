package datastore;

import java.util.List;

import data.CodeItem;

public interface CodeDataProvider {

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
	 * Edits the text of the item with this id in the database.
	 * 
	 * @param item
	 *            CodeItem
	 */
	public void editCode(CodeItem item);

}
