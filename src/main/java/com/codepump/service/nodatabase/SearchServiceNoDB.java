package com.codepump.service.nodatabase;

import java.util.ArrayList;
import java.util.List;

import com.codepump.data.CodeItem;
import com.codepump.data.User;
import com.codepump.service.SearchService;

public class SearchServiceNoDB implements SearchService {

	public SearchServiceNoDB() {
	}

	@Override
	public List<CodeItem> searchCode(String query, int limit, int offset) {
		List<CodeItem> l = new ArrayList<>();
		CodeItem s = new CodeItem(1, "This is it",
				"<b>This</b> is an example result.", "Java", "Public");
		l.add(s);
		return l;
	}

	@Override
	public List<CodeItem> searchCode(String query, int limit, int offset,
			User user) {
		List<CodeItem> l = new ArrayList<>();
		CodeItem s = new CodeItem(1, "This is it",
				"<b>This</b> is an example result.", "Java", "Public");
		l.add(s);
		return l;
	}

}
