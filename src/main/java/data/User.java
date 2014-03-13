package data;

/**
 * Basic user class. Password has to be hashed at some point
 * 
 * @author TKasekamp
 * 
 */
public class User {

	private int id;
	private String Name;
	private String Email;
	private String Password;

	public User(String name, String email, String password) {
		super();
		Name = name;
		Email = email;
		Password = password;
	}

	public User(int id, String name, String email, String password) {
		super();
		this.id = id;
		Name = name;
		Email = email;
		Password = password;
	}

	public User() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", Name=" + Name + ", Email=" + Email
				+ ", Password=" + Password + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

}
