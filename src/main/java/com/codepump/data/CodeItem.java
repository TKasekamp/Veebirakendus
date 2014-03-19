package com.codepump.data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Represents one item of code in the DB.<br>
 * <code>UserID</code> will be set to -1 if no user is specified.
 * Object User of this class will hold the user_ID.
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
	private Date createdDate; // when this code was made
	private Date expireDate;
	private User user; 

	public CodeItem() {
	}

	public CodeItem(Integer id, String name, String text, String language,
			String privacy) {
		super();
		this.id = id;
		this.name = name;
		this.text = text;
		this.language = language;
		this.privacy = privacy;
		this.createdDate = new Date();
		this.expireDate = new Date();
	}

	public CodeItem(Integer id, String name, String text, String language,
			String privacy, Date createdDate, Date expireDate, User user) {
		super();
		this.id = id;
		this.name = name;
		this.text = text;
		this.language = language;
		this.privacy = privacy;
		this.createdDate = createdDate;
		this.expireDate = expireDate;
		this.user = user;
	}

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

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = true)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "CodeItem [id=" + id + ", name=" + name + ", text=" + text
				+ ", language=" + language + ", privacy=" + privacy
				+ ", createdDate=" + createdDate + ", expireDate=" + expireDate
				+ ", user=" + user + "]";
	}

}
