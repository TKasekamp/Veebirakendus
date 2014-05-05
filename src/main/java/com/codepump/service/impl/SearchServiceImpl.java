package com.codepump.service.impl;

import java.util.List;

import com.codepump.data.CodeItem;
import com.codepump.data.User;
import com.codepump.service.DatabaseService;
import com.codepump.service.SearchService;
import com.google.inject.Inject;

public class SearchServiceImpl implements SearchService {
	private DatabaseService dbServ;

	@Inject
	public SearchServiceImpl(DatabaseService dbServ) {
		this.dbServ = dbServ;
	}

	@Override
	public List<CodeItem> searchCode(String query, int limit, int offset) {
		return dbServ.searchDatabasePublic(query, limit, offset);
	}

	@Override
	public List<CodeItem> searchCode(String query, int limit, int offset,
			User user) {
		if (user.getAdminStatus() == 0) {
			return dbServ
					.searchDatabaseUser(query, limit, offset, user.getId());
		} else {
			return dbServ.searchDatabaseAdmin(query, limit, offset);
		}
	}

}
