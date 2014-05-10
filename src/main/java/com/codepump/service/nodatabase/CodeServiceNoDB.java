package com.codepump.service.nodatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.codepump.data.CodeItem;
import com.codepump.data.User;
import com.codepump.service.AuthenicationService;
import com.codepump.service.CodeService;
import com.codepump.tempobject.EditContainer;
import com.codepump.tempobject.MyStuffListItem;
import com.codepump.tempobject.RecentItem;
import com.google.inject.Inject;

/**
 * CodeService when there is no DB.
 * 
 * @author TKasekamp
 * 
 */
public class CodeServiceNoDB implements CodeService {
	private static Map<Integer, CodeItem> items;
	private int codeCounter;
	private AuthenicationService authServ;

	/**
	 * 
	 * @param authServ
	 *            Injected by Guice
	 */
	@Inject
	public CodeServiceNoDB(AuthenicationService authServ) {
		items = new HashMap<>();
		items.put(1, new CodeItem(1, "hello",
				"public static void Hello(String s);", "java", "Public",
				new Date(), new Date(), new User(100, "Test", "dumbass",
						"parool")));
		items.put(2, new CodeItem(2, "bla", "print(\"bla\")", "python",
				"Public", new Date(), new Date(), new User(100, "Test",
						"dumbass", "parool")));
		items.put(3, new CodeItem(3, "haha", "Hello::Hello", "c", "Private",
				new Date(), new Date(), new User(100, "Test", "dumbass",
						"parool")));
		codeCounter = 4;

		this.authServ = authServ;
	}

	@Override
	public CodeItem findItemById(int id) {
		return items.get(id);
	}

	@Override
	public List<CodeItem> getAllCodeItems() {
		ArrayList<CodeItem> dataset = new ArrayList<CodeItem>();
		for (CodeItem value : items.values()) {
			if (value.getPrivacy().equals("Public")) {
				dataset.add(value);
			}
		}
		return dataset;
	}

	@Override
	public Boolean addCode(CodeItem item, String SID) {
		if(isValid(item)) {
		item.setCreateDate(new Date());
		item.setExpireDate(new Date());
		item.setText(escapeChars(item.getText()));

		User u = new User();
		u.setId(authServ.getUserIdWithSID(SID));
		item.setUser(u);
		item.setId(codeCounter);
		items.put(codeCounter, item);
		codeCounter++;
		return true;
		} else 
			return false;

	}

	@Override
	public void editCode(EditContainer item) {
		item.setText(escapeChars(item.getText()));
		items.get(item.getId()).setText(item.getText());

	}

	@Override
	public List<RecentItem> getRecentItems() {
		// TODO for someone to fix. RecentItem should get values from users
		ArrayList<RecentItem> dataset = new ArrayList<RecentItem>();
		for (CodeItem value : items.values()) {
			if (value.getPrivacy().equals("Public")) {
				RecentItem r = new RecentItem(value.getId(), value.getName(),
						value.getLanguage(), value.getCreateDate(), 100,
						"TEST USER NOT FOR REAL AS I CAN'T BE BOTHERED");
				dataset.add(r);
			}
		}
		return dataset;
	}

	@Override
	public List<MyStuffListItem> getAllUserItems(String SID) {
		int userId = authServ.getUserIdWithSID(SID);
		if (userId == -1) {
			return null;
		}
		ArrayList<MyStuffListItem> dataset = new ArrayList<MyStuffListItem>();
		for (CodeItem value : items.values()) {
			if (value.getUser().getId() == userId) {
				dataset.add(new MyStuffListItem(value.getId(), value.getName(),
						value.getLanguage(), value.getCreateDate()));
			}
		}
		return dataset;
	}

	@Override
	public void deleteCode(int id) {
		items.remove(id);

	}

	private String escapeChars(String text) {
		char[] temp = text.toCharArray();
		String out = "";
		for (int i = 0; i < temp.length; i++) {
			if (temp[i] == '<') {
				out += "&lt;";
				continue;
			}
			out += temp[i];
		}
		return out;
	}
	
	@Override
	public Boolean isValid(CodeItem item) {
		String name = item.getName();
		String text = item.getText();
		name.replaceAll("\\s", "");
		text.replaceAll("\\s", "");
		if (name.length() < 3 || text.length() < 3)
			return false;
		else
			return true;
	}
}
