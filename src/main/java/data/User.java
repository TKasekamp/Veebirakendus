package data;

/**
 * Basic user class. Password has to be hashed at some point
 * 
 * @author TKasekamp
 * 
 */
public class User {

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
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", Name=" + username + ", Email=" + email
				+ ", Password=" + password + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return username;
	}

	public void setName(String name) {
		this.username = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
