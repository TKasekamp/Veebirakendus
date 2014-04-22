package com.codepump.tempobject;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.hibernate.annotations.NamedNativeQuery;

import org.hibernate.annotations.NamedNativeQueries;

/**
 * Used to return search result from database. To use searchQuery, "query" must
 * be set to the search term. "Limit" is how many results to return and "offset"
 * is used to show page 2 of search results. In that case it probably has to be
 * set to limit*(page-1). Maybe.
 * 
 * @author TKasekamp
 * 
 */
@Entity
@NamedNativeQueries({
		@NamedNativeQuery(name = "publicSearchQuery", query = "WITH q AS (SELECT plainto_tsquery(:query) AS query), ranked AS ("
				+ " SELECT c.code_id, c.code_name, c.code_language, c.code_text, c.create_date, w.user_name, c.user_id, ts_rank('{0.1, 0.2, 0.4, 1.0}',tsv, query) AS rank "
				+ "FROM q, codeitem as c join webapp_user as w on c.user_id = w.user_id "
				+ "WHERE c.privacy = 'Public' and q.query @@ tsv  "
				+ "ORDER BY rank DESC "
				+ "LIMIT :limit OFFSET :offset )"
				+ "SELECT code_id, code_name, code_language,create_date, user_name, user_id, ts_headline(code_text, q.query, 'MaxWords=75,MinWords=25,ShortWord=3,MaxFragments=3,FragmentDelimiter=\"||||\"') "
				+ "FROM ranked, q " + "ORDER BY ranked DESC", resultClass = SearchItem.class),
		@NamedNativeQuery(name = "userSearchQuery", query = "WITH q AS (SELECT plainto_tsquery(:query) AS query), ranked AS ("
				+ " SELECT c.code_id, c.code_name, c.code_language, c.code_text, c.create_date, w.user_name, c.user_id, ts_rank('{0.1, 0.2, 0.4, 1.0}',tsv, query) AS rank "
				+ "FROM q, codeitem as c join webapp_user as w on c.user_id = w.user_id "
				+ "WHERE (c.privacy = 'Public' or c.user_id = :user_id) and q.query @@ tsv  "
				+ "ORDER BY rank DESC "
				+ "LIMIT :limit OFFSET :offset )"
				+ "SELECT code_id, code_name, code_language,create_date, user_name, user_id, ts_headline(code_text, q.query, 'MaxWords=75,MinWords=25,ShortWord=3,MaxFragments=3,FragmentDelimiter=\"||||\"') "
				+ "FROM ranked, q "
				+ "ORDER BY ranked DESC", resultClass = SearchItem.class),
		@NamedNativeQuery(name = "adminQuery", query = "WITH q AS (SELECT plainto_tsquery(:query) AS query), ranked AS ("
				+ " SELECT c.code_id, c.code_name, c.code_language, c.code_text, c.create_date, w.user_name, c.user_id, ts_rank('{0.1, 0.2, 0.4, 1.0}',tsv, query) AS rank "
				+ "FROM q, codeitem as c join webapp_user as w on c.user_id = w.user_id "
				+ "WHERE q.query @@ tsv  "
				+ "ORDER BY rank DESC "
				+ "LIMIT :limit OFFSET :offset )"
				+ "SELECT code_id, code_name, code_language,create_date, user_name, user_id, ts_headline(code_text, q.query, 'MaxWords=75,MinWords=25,ShortWord=3,MaxFragments=3,FragmentDelimiter=\"||||\"') "
				+ "FROM ranked, q " + "ORDER BY ranked DESC", resultClass = SearchItem.class) })
public class SearchItem implements Serializable {

	private static final long serialVersionUID = 4621868209069560350L;
	private int codeId;
	private String codeName;
	private String codeLanguage;
	private String ts_headline;
	private Date createDate;
	private int userId;
	private String userName;

	public SearchItem() {
	}

	public SearchItem(int codeId, String codeName, String codeLanguage,
			String ts_headline) {
		super();
		this.codeId = codeId;
		this.codeName = codeName;
		this.codeLanguage = codeLanguage;
		this.ts_headline = ts_headline;
		this.createDate = new Date();
		this.userId = -1;
		this.userName = "Public";
	}

	@Id
	@Column(name = "CODE_ID")
	public int getCodeId() {
		return codeId;
	}

	public void setCodeId(int codeId) {
		this.codeId = codeId;
	}

	@Column(name = "CODE_NAME")
	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	@Column(name = "CODE_LANGUAGE")
	public String getCodeLanguage() {
		return codeLanguage;
	}

	public void setCodeLanguage(String codeLanguage) {
		this.codeLanguage = codeLanguage;
	}

	@Column(name = "TS_HEADLINE")
	public String getTs_headline() {
		return ts_headline;
	}

	public void setTs_headline(String ts_headline) {
		this.ts_headline = ts_headline;
	}

	@Column(name = "create_date")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "user_id")
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Column(name = "user_name")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "SearchItem [codeId=" + codeId + ", codeName=" + codeName
				+ ", codeLanguage=" + codeLanguage + ", ts_headline="
				+ ts_headline + ", createDate=" + createDate + ", userId="
				+ userId + ", userName=" + userName + "]";
	}

}
