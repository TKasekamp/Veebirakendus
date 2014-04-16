package com.codepump.service.nodatabase;

import java.util.ArrayList;
import java.util.List;

import com.codepump.data.User;
import com.codepump.service.SearchService;
import com.codepump.tempobject.SearchItem;

public class SearchServiceNoDB implements SearchService {

	public SearchServiceNoDB() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<SearchItem> searchCode(String query, int limit, int offset) {
		List<SearchItem> l = new ArrayList<>();
		SearchItem s = new SearchItem(1, "This is it", "Java",
				"<b>This</b> is an example result.");
		l.add(s);
		return l;
	}

	@Override
	public List<SearchItem> searchCode(String query, int limit, int offset,
			User user) {
		List<SearchItem> l = new ArrayList<>();
		SearchItem s = new SearchItem(1, "This is it", "Java",
				"<b>This</b> is an example result.");
		l.add(s);
		return l;
	}

}
