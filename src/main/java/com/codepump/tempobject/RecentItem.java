package com.codepump.tempobject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;

/**
 * Class to transport values from both tables to the user. <br>
 * Creates a new object with values from both the codeitem and webapp_user
 * table. Creating this join clause almost destroyed my will to live.
 * 
 * @author TKasekamp
 * 
 */
@Entity
@NamedNativeQuery(name = "findRecentItemsInOrder", query = "select c.code_id, c.code_name,  c.code_language, "
		+ "c.created_date, w.user_name, w.user_id FROM CodeItem as c JOIN webapp_user as w on w.user_id = c.user_id where c.privacy = 'Public' ORDER BY c.created_date DESC LIMIT 4", resultClass = RecentItem.class)
public class RecentItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6503631209142487572L;
	private int codeID;
	private String codeName;
	private String codeLanguage;
	private Date createDate;
	private int userID;
	private String userName;

	private static final DateFormat FORMAT = new SimpleDateFormat(
			"HH:mm:ss dd.MM.yyyy");

	public RecentItem() {
	}

	public RecentItem(int codeId, String codeName, String codeLanguage,
			Date createDate, int user_id, String user_name) {
		super();
		this.codeID = codeId;
		this.codeName = codeName;
		this.codeLanguage = codeLanguage;
		this.createDate = createDate;
		this.userID = user_id;
		this.userName = user_name;
	}

	@Id
	@Column(name = "CODE_ID")
	public int getCodeId() {
		return codeID;
	}

	public void setCodeId(int codeId) {
		this.codeID = codeId;
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

	@Column(name = "CREATED_DATE")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "USER_ID")
	public int getUserID() {
		return userID;
	}

	public void setUserID(int user_id) {
		this.userID = user_id;
	}

	@Column(name = "USER_NAME")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String user_name) {
		this.userName = user_name;
	}

	@Override
	public String toString() {
		return "RecentItem [codeId=" + codeID + ", codeName=" + codeName
				+ ", codeLanguage=" + codeLanguage + ", createDate="
				+ createDate + ", userID=" + userID + ", userName=" + userName
				+ "]";
	}

	/**
	 * Formats the creation date to "HH:mm:ss dd.MM.yyyy"
	 * 
	 * @return String format of date
	 */
	public String prettyCreateDate() {
		return FORMAT.format(createDate);
	}

}
