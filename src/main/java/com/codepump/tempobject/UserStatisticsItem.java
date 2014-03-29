package com.codepump.tempobject;

import java.io.Serializable;
import java.util.List;


/**
 * Class to transport values from both tables to the user. <br>
 * Used to create statistics about the user.<br>
 * 
 * @author TKasekamp
 * 
 */
public class UserStatisticsItem implements Serializable {
	/**
	 * Has to be here
	 */
	private static final long serialVersionUID = -5921914751621125681L;
	private int userID;
	private String userName;
	private int createdItems;
	private List<UserLanguageStatisticsItem> languageStatistics;

	public UserStatisticsItem() {
	}

	public UserStatisticsItem(int userID, String username, int createdItems) {
		super();
		this.userID = userID;
		this.userName = username;
		this.createdItems = createdItems;
		languageStatistics = null;
	}

	/**
	 * Creates user statistics based on language statistics. Gets userID,
	 * userName from first item of dataset. CreatedItems are calculated by
	 * iterating over the dataset.
	 * 
	 * @param dataset
	 *            {@link UserLanguageStatisticsItem}
	 */
	public UserStatisticsItem(List<UserLanguageStatisticsItem> dataset) {
		this.userID = dataset.get(0).getUserID();
		this.userName = dataset.get(0).getUsername();
		this.languageStatistics = dataset;
		this.createdItems = 0;
		for (UserLanguageStatisticsItem item : dataset) {
			this.createdItems += item.getCreatedItems();
		}
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String username) {
		this.userName = username;
	}

	public int getCreatedItems() {
		return createdItems;
	}

	public void setCreatedItems(int createdItems) {
		this.createdItems = createdItems;
	}

	public List<UserLanguageStatisticsItem> getLanguageStatistics() {
		return languageStatistics;
	}

	public void setLanguageStatistics(
			List<UserLanguageStatisticsItem> languageStatistics) {
		this.languageStatistics = languageStatistics;
	}

	@Override
	public String toString() {
		return "UserStatisticsItem [userID=" + userID + ", userName="
				+ userName + ", createdItems=" + createdItems
				+ ", languageStatistics=" + languageStatistics + "]";
	}

}
