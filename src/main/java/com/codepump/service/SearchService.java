package com.codepump.service;

import java.util.List;

import com.codepump.data.User;
import com.codepump.tempobject.SearchItem;

public interface SearchService {
	/**
	 * Default search method. Returns all Public texts mathing this query.
	 * 
	 * @param query
	 *            Stuff to search for
	 * @param limit
	 *            How many items to display
	 * @param offset
	 *            On which page you are I think
	 * @return List of {@link SearchItem} as the results of this query
	 */
	public List<SearchItem> searchCode(String query, int limit, int offset);

	/**
	 * Searching when user is logged in. If user is admin returns texts from
	 * everyone. Otherwise returns texts which are Public or made by this User.
	 * 
	 * @param query
	 *            Stuff to search for
	 * @param limit
	 *            How many items to display
	 * @param offset
	 *            On which page you are I think
	 * @param User
	 *            Logged in user with ID and adminStatus
	 * @return List of {@link SearchItem} as the results of this query
	 */
	public List<SearchItem> searchCode(String query, int limit, int offset,
			User user);
}
