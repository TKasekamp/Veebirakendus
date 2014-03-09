package datastore;

import java.util.List;

import data.CodeItem;

public interface CodeDataProvider {

	public CodeItem findItemById(int id);

	public List<CodeItem> findAllItems();

	public void addCode(CodeItem item);

}
