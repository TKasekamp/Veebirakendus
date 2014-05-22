package com.codepump.service;

import com.codepump.data.CodeItem;
import com.codepump.data.temporary.MyStuffListItem;
import com.codepump.data.container.ResultContainer;

public interface SearchService {

	/**
	 * Searches the database for with specified query. Returns all texts.
	 * General purpose query, uses fuzzy search.
	 * 
	 * @param <T>
	 *            {@link CodeItem} or {@link MyStuffListItem}
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
	 * @return {@link ResultContainer} holding the results and expected total
	 *         size of this query
	 */
	public <T> ResultContainer<T> searchDatabase(String query, int limit,
			int firstResult, String sortField);

}
