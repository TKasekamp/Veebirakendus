package com.codepump.service.impl;

import java.util.List;

import com.codepump.data.User;
import com.codepump.service.DatabaseService;
import com.codepump.service.SearchService;
import com.codepump.tempobject.SearchItem;
import com.google.inject.Inject;

public class SearchServiceImpl implements SearchService {
	private DatabaseService dbServ;

	@Inject
	public SearchServiceImpl(DatabaseService dbServ) {
		this.dbServ = dbServ;
	}

	@Override
	public List<SearchItem> searchCode(String query, int limit, int offset) {
		return dbServ.searchDatabase(query, limit, offset);
	}

	@Override
	public List<SearchItem> searchCode(String query) {
		return dbServ.searchDatabase(query, 10, 0);
	}

	@Override
	public List<SearchItem> searchCode(String query, User user) {
		return dbServ.searchDatabase(query, 10, 0);
	}

	@Override
	public List<SearchItem> searchCode(String query, int limit, int offset,
			User user) {
		return dbServ.searchDatabase(query, limit, offset);
	}

}
