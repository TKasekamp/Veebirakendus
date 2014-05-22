package com.codepump.service.impl;

import java.util.Date;
import java.util.List;

import com.codepump.data.CodeItem;
import com.codepump.data.User;
import com.codepump.service.AuthenicationService;
import com.codepump.service.CodeService;
import com.codepump.service.DatabaseService;
import com.codepump.data.container.EditContainer;
import com.codepump.data.temporary.MyStuffListItem;
import com.codepump.data.temporary.RecentItem;
import com.codepump.data.container.ResultContainer;
import com.google.inject.Inject;

public class CodeServiceImpl implements CodeService {
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
		this.dbServ = dbServ;
		this.authServ = authServ;
	}

	@Override
	public CodeItem findItemById(int id) {
		return dbServ.findCodeItemById(id);
	}

	@Override
	public ResultContainer<CodeItem> getAllCodeItems(int firstResult,
			int maxResults) {
		return dbServ.getAllCodeItems(firstResult, maxResults);

	}

	@Override
	public void editCode(EditContainer item) {
		if (authServ.authoriseEdit(item)) {
			item.setText(escapeChars(item.getText()));
			// this is not efficient, but creating a direct update query
			// kind of crashed the server
			CodeItem code = findItemById(item.getId());
			code.setText(item.getText());
			dbServ.updateCodeItem(code);

		}

	}

	@Override
	public List<RecentItem> getRecentItems() {
		return dbServ.getRecentItems(0, 4);
	}

	@Override
	public ResultContainer<MyStuffListItem> getAllUserItems(String SID,
			int firstResult, int maxResults) {
		int userId = authServ.getUserIdWithSID(SID);

		// userID will be set to -1 if no such SID can be found. This is the
		// public user and as such MyStuff should not work
		if (userId == -1) {
			return null;
		}
		return dbServ.getAllUserItems(userId, firstResult, maxResults);

	}

	@Override
	public void deleteCode(int id) {
		dbServ.deleteCodeItem(id);

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
	public Boolean addCode(CodeItem item, String SID) {
		if (isValid(item)) {
			item.setCreateDate(new Date());
			item.setExpireDate(new Date());
			item.setText(escapeChars(item.getText()));
			User u = new User();
			u.setId(authServ.getUserIdWithSID(SID));
			item.setUser(u);
			dbServ.saveCodeItem(item);
			return true;
		} else
			return false;
	}

	@Override
	public Boolean isValid(CodeItem item) {
		String name = item.getName();
		String text = item.getText();
		name = name.replaceAll("\\s", "");
		text = text.replaceAll("\\s", "");
		if (name.length() < 3 || text.length() < 3)
			return false;
		else
			return true;
	}
}
