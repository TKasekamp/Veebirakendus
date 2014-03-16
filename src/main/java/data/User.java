package data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Basic user class. 
 * 
 * @author TKasekamp
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "WEBAPP_USER")
public class User implements java.io.Serializable{

	private int id;
	private String username;
	private String email;
	private String password;

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

	@Override
	public String toString() {
		return "User [id=" + id + ", Name=" + username + ", Email=" + email
				+ ", Password=" + password + "]";
	}

	@Id
	@Column(name = "USER_ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	@Column(name = "USER_NAME", nullable = false, length = 100)
	public String getName() {
		return username;
	}

	public void setName(String name) {
		this.username = name;
	}
	@Column(name = "USER_EMAIL", length = 50)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	@Column(name = "USER_PASSWORD", nullable = false,length = 50)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
