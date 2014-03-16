package data;

/**
 * Used in response to a login attempt. <br>
 * 0 - there is no such user <br>
 * 1 - user and password are correct, but already logged in. NOT USED<br>
 * 2 - wrong password <br>
 * 3 - user succesfully logged in
 * 
 * @author TKasekamp
 * 
 */
public class LoginResponse {
	private int response;
	private String SID;

	public LoginResponse(int response, String sID) {
		super();
		this.response = response;
		SID = sID;
	}

	public LoginResponse() {
		// TODO Auto-generated constructor stub
	}

	public int getResponse() {
		return response;
	}

	public void setResponse(int response) {
		this.response = response;
	}

	public String getSID() {
		return SID;
	}

	public void setSID(String sID) {
		SID = sID;
	}

	@Override
	public String toString() {
		return "LoginResponse [response=" + response + ", SID=" + SID + "]";
	}

}
