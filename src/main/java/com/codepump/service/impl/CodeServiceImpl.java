package com.codepump.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.codepump.controller.ServerController;
import com.codepump.data.CodeItem;
import com.codepump.data.User;
import com.codepump.service.AuthenicationService;
import com.codepump.service.CodeService;
import com.codepump.service.DatabaseService;
import com.codepump.tempobject.EditContainer;
import com.codepump.tempobject.MyStuffListItem;
import com.codepump.tempobject.RecentItem;
import com.google.inject.Inject;

public class CodeServiceImpl implements CodeService {
	private static Map<Integer, CodeItem> items;
	private int codeCounter; // useless
	private final boolean USE_DATABASE = ServerController.USE_DATABASE;
	private DatabaseService dbServ; // NULL if USE_DATABASE = true
	private AuthenicationService authServ;



	/**
	 * 
	 * @param dbServ
	 *            Injected by Guice
	 * @param authServ
	 *            Injected by Guice
	 */
	@Inject
	public CodeServiceImpl(DatabaseService dbServ, AuthenicationService authServ) {
		if (USE_DATABASE) {
			this.dbServ = dbServ;
		} else {
			items = new HashMap<>();
			items.put(1, new CodeItem(1, "hello",
					"public static void Hello(String s);", "java", "Public",
					new Date(), new Date(), new User(100, "Test", "dumbass",
							"parool")));
			items.put(2, new CodeItem(2, "bla", "print(\"bla\")", "python",
					"Public", new Date(), new Date(), new User(100, "Test",
							"dumbass", "parool")));
			items.put(3, new CodeItem(3, "haha", "Hello::Hello", "c",
					"Private", new Date(), new Date(), new User(100, "Test",
							"dumbass", "parool")));
			codeCounter = 4;
		}
		this.authServ = authServ;
	}

	@Override
	@Deprecated
	public void addCode(CodeItem item) {
		item.setSaveDate(new Date());
		item.setExpireDate(new Date());
		item.setText(escapeChars(item.getText()));
		if (USE_DATABASE) {
			dbServ.saveCodeItem(item);
		} else {
			item.setId(codeCounter); // temp hack
			items.put(codeCounter, item);
			codeCounter++;
		}

	}

	@Override
	public CodeItem findItemById(int id) {
		if (USE_DATABASE) {
			return dbServ.findCodeItemById(id);
		} else {
			return items.get(id);
		}
	}

	@Override
	public List<CodeItem> findAllItems() {
		if (USE_DATABASE) {
			return dbServ.getAllCodeItems();
		} else {
			ArrayList<CodeItem> dataset = new ArrayList<CodeItem>();
			for (CodeItem value : items.values()) {
				if (value.getPrivacy().equals("Public")) {
					dataset.add(value);
				}
			}
			return dataset;
		}

	}

	@Override
	public void editCode(EditContainer item) {
		if (authServ.authoriseEdit(item)) {
			item.setText(escapeChars(item.getText()));
			if (USE_DATABASE) {
				// this is not efficient, but creating a direct update query
				// kind of crashed the server
				CodeItem code = findItemById(item.getId());
				code.setText(item.getText());
				dbServ.updateCodeItem(code);
			} else {
				items.get(item.getId()).setText(item.getText());
			}
		}

	}

	@Override
	public List<RecentItem> getRecentItems() {
		if (USE_DATABASE) {
			return dbServ.getRecentItems();
		} else {
			// TODO for someone to fix. RecentItem should get values from users
			ArrayList<RecentItem> dataset = new ArrayList<RecentItem>();
			for (CodeItem value : items.values()) {
				if (value.getPrivacy().equals("Public")) {
					RecentItem r = new RecentItem(value.getId(),
							value.getName(), value.getLanguage(),
							value.getSaveDate(), 100,
							"TEST USER NOT FOR REAL AS I CAN'T BE BOTHERED");
					dataset.add(r);
				}
			}
			return dataset;
		}
	}

	@Override
	@Deprecated
	public RecentItem getLastRecentItem() {
		if (USE_DATABASE) {
			return null;
		} else {
			// TODO for someone to fix. RecentItem should get values from users
			return new RecentItem(100, "haha", "Java", new Date(), 100, "test");
		}
	}

	@Override
	public List<MyStuffListItem> getAllUserItems(String SID) {
		int userId = authServ.getUserWithSID(SID);

		// userID will be set to -1 if no such SID can be found. This is the
		// public user and as such MyStuff should not work
		if (userId == -1) {
			return null;
		}
		if (USE_DATABASE) {
			return dbServ.getAllUserItems(userId);
		} else {
			ArrayList<MyStuffListItem> dataset = new ArrayList<MyStuffListItem>();
			for (CodeItem value : items.values()) {
				if (value.getUser().getId() == userId) {
					dataset.add(new MyStuffListItem(value.getId(), value
							.getName(), value.getLanguage(), value
							.getSaveDate()));
				}
			}
			return dataset;
		}

	}

	@Override
	public void deleteCode(int id) {
		if (USE_DATABASE) {
			dbServ.deleteCodeItem(id);
		} else {
			items.remove(id);
		}
	}

	private String escapeChars(String text) {
		text.replaceAll("<", "&lt;");
		return text;
	}

	@Override
	public void addCode(CodeItem item, String SID) {
		item.setSaveDate(new Date());
		item.setExpireDate(new Date());
		item.setText(escapeChars(item.getText()));
		
		User u = new User();
		u.setId(authServ.getUserWithSID(SID));
		item.setUser(u);
		if (USE_DATABASE) {
			dbServ.saveCodeItem(item);
		} else {
			item.setId(codeCounter); // temp hack
			items.put(codeCounter, item);
			codeCounter++;
		}
		
	}
}
