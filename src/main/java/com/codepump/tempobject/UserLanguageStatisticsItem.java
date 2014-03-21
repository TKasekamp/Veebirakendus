package com.codepump.tempobject;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;

/**
 * Class to transport values from both tables to the user. <br>
 * Used to create statistics about the user.<br>
 * Returns the number of code made in a certain language by this user. <br>
 * 
 * @author TKasekamp
 * 
 */
@Entity
@NamedNativeQuery(name = "thisUserLanguageStatistics", query = "SELECT w.user_id, w.user_name, c.code_language as language, "
		+ "count(*) as count FROM  webapp_user as w JOIN codeitem as c ON w.user_id = c.user_id WHERE w.user_id = :t_id GROUP BY w.user_id, w.user_name, "
		+ "c.code_language", resultClass = UserLanguageStatisticsItem.class)
public class UserLanguageStatisticsItem implements Serializable {
	/**
	 * Has to be here
	 */
	private static final long serialVersionUID = 6466809240738515855L;
	private int userID;
	private String username;
	private int createdItems;
	private String language;

	public UserLanguageStatisticsItem() {
	}

	public UserLanguageStatisticsItem(int userID, String username,
			int createdItems, String language) {
		super();
		this.userID = userID;
		this.username = username;
		this.createdItems = createdItems;
		this.language = language;
	}

	@Id
	@Column(name = "USER_ID")
	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	@Column(name = "USER_NAME")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "COUNT")
	public int getCreatedItems() {
		return createdItems;
	}

	public void setCreatedItems(int createdItems) {
		this.createdItems = createdItems;
	}

	@Column(name = "LANGUAGE")
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Override
	public String toString() {
		return "UserStatisticsItem [userID=" + userID + ", username="
				+ username + ", createdItems=" + createdItems
				+ ", favouriteLanguage=" + language + "]";
	}

}
