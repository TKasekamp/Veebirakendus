package com.codepump.service;

import java.util.List;

import com.codepump.data.User;
import com.codepump.tempobject.SearchItem;

public interface SearchService {
	public List<SearchItem> searchCode(String query, int limit, int offset);

	public List<SearchItem> searchCode(String query);

	public List<SearchItem> searchCode(String query, User user);

	public List<SearchItem> searchCode(String query, int limit, int offset,
			User user);
}
