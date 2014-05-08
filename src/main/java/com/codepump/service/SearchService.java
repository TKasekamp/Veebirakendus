package com.codepump.service;

import com.codepump.tempobject.SearchContainer;

public interface SearchService {

	/**
	 * Searches the database for with specified query. Returns all texts.
	 * General purpose query, uses fuzzy search.
	 * 
	 * @param query
	 *            Stuff to search for
	 * @param limit
	 *            How many items to display
	 * @param firstResult
	 *            First result
	 * @param sortField
	 *            Sort the results with by this field. Accepted values are
	 *            "relevancy", "name", "name-reverse".
	 * @return {@link SearchContainer} holding the results and expected total
	 *         size of this query
	 */
	public SearchContainer searchDatabaseFuzzy(String query, int limit,
			int firstResult, String sortField);

}
