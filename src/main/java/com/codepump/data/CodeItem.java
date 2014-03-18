package com.codepump.data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Represents one item of code in the DB.<br>
 * <code>UserID</code> will be set to -1 if no user is specified.
 * 
 * @author TKasekamp
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "CODEITEM")
public class CodeItem implements java.io.Serializable {
	private Integer id;
	private String name;
	private String text;
	private String language;
	private String privacy;
	private Integer UserId; // who it belongs to
	private Date createdDate; // when this code was made
	private Date expireDate;

	public CodeItem() {
	}

//	public CodeItem(Integer id, String name, String text) {
//		super();
//		this.name = name;
//		this.text = text;
//		this.id = id;
//	}

//	public CodeItem(String name, String text) {
//		super();
//		this.name = name;
//		this.text = text;
//	}

	public CodeItem(Integer id, String name, String text, String language,
			String privacy) {
		super();
		this.id = id;
		this.name = name;
		this.text = text;
		this.language = language;
		this.privacy = privacy;
		this.UserId = -1;
		this.createdDate = new Date();
		this.expireDate = new Date();
	}

//	public CodeItem(String name, String text, String language, String privacy) {
//		super();
//		this.name = name;
//		this.text = text;
//		this.language = language.toLowerCase();
//		this.privacy = privacy;
//	}

	/**
	 * Generates a string with id. It can be done with a costum com.codepump.serializer, but
	 * this is easier.
	 * 
	 * @return id in Json format
	 */
	public String JsonID() {
		return "{\"id\":\"" + Integer.toString(id) + "\"}";
	}

	@Id
	@Column(name = "CODE_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "CODE_NAME", nullable = false, length = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "CODE_TEXT", nullable = false)
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Column(name = "CODE_LANGUAGE", nullable = false, length = 20)
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Column(name = "PRIVACY", nullable = false, length = 10)
	public String getPrivacy() {
		return privacy;
	}

	public void setPrivacy(String privacy) {
		this.privacy = privacy;
	}

	@Column(name = "USER_ID")
	public Integer getUserId() {
		return UserId;
	}

	public void setUserId(Integer userId) {
		UserId = userId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", nullable = false, length = 7)
	public Date getSaveDate() {
		return createdDate;
	}

	public void setSaveDate(Date saveDate) {
		this.createdDate = saveDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXPIRE_DATE", nullable = false, length = 7)
	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	@Override
	public String toString() {
		return "CodeItem [id=" + id + ", name=" + name + ", text=" + text
				+ ", language=" + language + ", privacy=" + privacy
				+ ", UserId=" + UserId + ", createdDate=" + createdDate
				+ ", expireDate=" + expireDate + "]";
	}

}
