package com.codepump.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.math.BigInteger;
import java.security.*;
import java.util.Date;

/**
 * Basic user class.
 * 
 * @author TKasekamp
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "WEBAPP_USER")
public class User implements java.io.Serializable {

	private int id;
	private String username;
	private String email;
	private String password;
	private int adminStatus;
	private Date createDate;
	private Date lastLoginDate;

	public User(String name, String email, String password) {
		super();
		this.username = name;
		this.email = email;
		this.password = password;
	}

	public User(int id, String name, String email, String password) {
		super();
		this.id = id;
		this.username = name;
		this.email = email;
		this.password = password;
	}

	public User() {
	}

	@Id
	@Column(name = "USER_ID", unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "USER_NAME", nullable = false, length = 50)
	public String getName() {
		return username;
	}

	public void setName(String name) {
		this.username = name;
	}

	@Column(name = "USER_EMAIL", unique = true, nullable = false, length = 50)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "USER_PASSWORD", nullable = false, length = 32)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void hashPassword() {
		this.password = getHash(password);
	}

	/**
	 * Hashes the input string with MD5. Reversed email is used as salt.
	 * 
	 * @param text
	 *            password
	 * @return MD5 hash
	 * @author juhanr
	 */
	private String getHash(String text) {
		try {
			String salt = new StringBuffer(this.email).reverse().toString();
			text += salt;
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.reset();
			m.update(text.getBytes());
			byte[] digest = m.digest();
			BigInteger bigInt = new BigInteger(1, digest);
			String hashedText = bigInt.toString(16);
			while (hashedText.length() < 32) {
				hashedText = "0" + hashedText;
			}
			return hashedText;
		} catch (Exception e) {
			e.getStackTrace();
			return text;
		}
	}

	@Column(name = "ADMIN_STATUS", nullable = false)
	public int getAdminStatus() {
		return adminStatus;
	}

	public void setAdminStatus(int adminStatus) {
		this.adminStatus = adminStatus;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_DATE", nullable = false)
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_LOGIN_DATE", nullable = false)
	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", email=" + email
				+ ", password=" + password + ", adminStatus=" + adminStatus
				+ ", createDate=" + createDate + ", lastLoginDate="
				+ lastLoginDate + "]";
	}

}