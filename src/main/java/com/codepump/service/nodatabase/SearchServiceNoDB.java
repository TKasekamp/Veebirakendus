package com.codepump.service.nodatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.codepump.data.CodeItem;
import com.codepump.data.User;
import com.codepump.service.SearchService;
import com.codepump.data.container.ResultContainer;

public class SearchServiceNoDB implements SearchService {

	public SearchServiceNoDB() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResultContainer<CodeItem> searchDatabase(String query, int limit,
			int offset, String sortField) {
		List<CodeItem> results = new ArrayList<>();
		for (int i = 0; i < 30; i++) {
			results.add(new CodeItem(i, "Test code " + Integer.toString(i),
					"This is sample text", "Sander-language", "Public",
					new Date(), new Date(), new User(i, "Sander", "bla", "")));
		}
		return new ResultContainer<CodeItem>(results.size(), results);
	}

}
