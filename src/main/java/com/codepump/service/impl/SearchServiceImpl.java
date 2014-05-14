package com.codepump.service.impl;

import java.util.List;

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.errors.EmptyQueryException;
import org.hibernate.search.query.dsl.QueryBuilder;

import com.codepump.data.CodeItem;
import com.codepump.service.SearchService;
import com.codepump.tempobject.ResultContainer;
import com.codepump.util.HibernateUtil;

@SuppressWarnings("unchecked")
public class SearchServiceImpl implements SearchService {
	private static SessionFactory sessionFactory;

	public SearchServiceImpl() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}

	@Override
	public ResultContainer<CodeItem> searchDatabaseFuzzy(String searchString, int size,
			int firstResult, String sortField) {

		if(illegalArgumentCheck(size, firstResult)) {
			return new ResultContainer<CodeItem>(0, null);
		}
		FullTextQuery hibernateQuery;
		// TODO rethink error handling
		try {
			if (searchString.length() > 2 && searchString.startsWith("\"")
					&& searchString.endsWith("\"")) {
				hibernateQuery = strictQuery(searchString, size, firstResult);
			} else {
				hibernateQuery = fuzzyQuery(searchString, size, firstResult);
			}
		} catch (EmptyQueryException e) {
			return new ResultContainer<CodeItem>(0, null);
		}

		// Apply optional sort criteria, if not the default sort-by-relevance
		if (sortField.equals("name")) {
			Sort sort = new Sort(
					new SortField("sorting_name", SortField.STRING));
			hibernateQuery.setSort(sort);
		} else if (sortField.equals("name-reverse")) {
			Sort sort = new Sort(new SortField("sorting_name",
					SortField.STRING, true));
			hibernateQuery.setSort(sort);
		}

		int resultSize = ((FullTextQuery) hibernateQuery).getResultSize();

		List<CodeItem> result = hibernateQuery.list();
		return new ResultContainer<CodeItem>(resultSize, result);
	}

	private FullTextQuery fuzzyQuery(String query, int size, int firstResult) {
		Session session = sessionFactory.getCurrentSession();
		FullTextSession fullTextSession = Search.getFullTextSession(session);

		QueryBuilder qb = fullTextSession.getSearchFactory()
				.buildQueryBuilder().forEntity(CodeItem.class).get();
		org.apache.lucene.search.Query luceneQuery = qb.keyword().fuzzy()
				.withThreshold(0.7f).onFields("name", "text").matching(query)
				.createQuery();

		FullTextQuery hibernateQuery = fullTextSession.createFullTextQuery(
				luceneQuery, CodeItem.class);
		hibernateQuery.setFirstResult(firstResult);
		hibernateQuery.setMaxResults(size);
		return hibernateQuery;
	}

	private FullTextQuery strictQuery(String searchString, int size,
			int firstResult) {

		// If the user's search string is surrounded by double-quotes, then use
		// a phrase search
		String unquotedSearchString = searchString.substring(1,
				searchString.length() - 1);

		Session session = sessionFactory.getCurrentSession();
		FullTextSession fullTextSession = Search.getFullTextSession(session);

		QueryBuilder qb = fullTextSession.getSearchFactory()
				.buildQueryBuilder().forEntity(CodeItem.class).get();
		org.apache.lucene.search.Query luceneQuery = qb.phrase()
				.onField("name").andField("text")
				.sentence(unquotedSearchString).createQuery();

		FullTextQuery hibernateQuery = fullTextSession.createFullTextQuery(
				luceneQuery, CodeItem.class);
		hibernateQuery.setFirstResult(firstResult);
		hibernateQuery.setMaxResults(size);
		return hibernateQuery;
	}

	/**
	 * Checking if the pagination parameters are illegal.
	 * 
	 * @param size
	 * @param firstResult
	 * @return true if params are illegal
	 */
	private boolean illegalArgumentCheck(int size, int firstResult) {
		if (size <= 0) {
			return true;
		}
		if (firstResult < 0) {
			return true;
		}
		return false;
	}

}
