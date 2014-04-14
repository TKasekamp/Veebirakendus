package com.codepump.tempobject;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;

@Entity
@NamedNativeQuery(name = "searchQuery", query = "WITH q AS (SELECT plainto_tsquery(:query) AS query), ranked AS ("
		+ " SELECT code_id, code_name, code_language, code_text, created_date, ts_rank('{0.1, 0.2, 0.4, 1.0}',tsv, query) AS rank "
		+ "FROM codeitem, q "
		+ "WHERE q.query @@ tsv "
		+ "ORDER BY rank DESC "
		+ "LIMIT :limit OFFSET :offset )"
		+ "SELECT code_id, code_name, code_language, created_date, ts_headline(code_text, q.query, 'MaxWords=75,MinWords=25,ShortWord=3,MaxFragments=3,FragmentDelimiter=\"||||\"') "
		+ "FROM ranked, q " + "ORDER BY ranked DESC", resultClass = SearchItem.class)
public class SearchItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4621868209069560350L;
	private int codeId;
	private String codeName;
	private String codeLanguage;
	private String ts_headline;

	 private Date createDate;
	// private int userId;
	// private String userName;

	public SearchItem() {
		// TODO Auto-generated constructor stub
	}

	public SearchItem(int codeId, String codeName, String codeLanguage,
			String ts_headline) {
		super();
		this.codeId = codeId;
		this.codeName = codeName;
		this.codeLanguage = codeLanguage;
		this.ts_headline = ts_headline;
		this.createDate = new Date();
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

	 @Column(name = "created_date")
	 public Date getCreateDate() {
	 return createDate;
	 }

	 public void setCreateDate(Date createDate) {
	 this.createDate = createDate;
	 }
	//
	//
	// public int getUserId() {
	// return userId;
	// }
	//
	//
	// public void setUserId(int userId) {
	// this.userId = userId;
	// }
	//
	//
	// public String getUserName() {
	// return userName;
	// }
	//
	//
	// public void setUserName(String userName) {
	// this.userName = userName;
	// }
	//
	//

	@Override
	public String toString() {
		return "SearchItem [codeId=" + codeId + ", codeName=" + codeName
				+ ", codeLanguage=" + codeLanguage + ", ts_headline="
				+ ts_headline + ", createDate=" + createDate + "]";
	}

}
