package datastore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.CodeItem;

public class MemoryStore implements CodeDataProvider {

	private final Map<Integer, CodeItem> items;
	private int codeCounter; // useless

	public MemoryStore() {
		items = new HashMap<>();
		items.put(1, new CodeItem("hello", "public static void Hello(String s);", "java", "Public"));
		codeCounter = 2;
	}

	@Override
	public void addCode(CodeItem item) {
		items.put(codeCounter, item);
		codeCounter++; //
		System.out.println("Added item. List now contains:");
		System.out.println(items);

	}

	@Override
	public CodeItem findItemById(int id) {
		return items.get(id);
	}

	@Override
	public List<CodeItem> findAllItems() {
		return new ArrayList<>(items.values());
	}
}
