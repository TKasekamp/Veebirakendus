package com.codepump.response;

/**
 * Used in response to a login attempt. <br>
 * 0 - there is no such user <br>
 * 1 - user and password are correct, but already logged in. NOT USED<br>
 * 2 - wrong password <br>
 * 3 - user succesfully logged in<br>
 * 
 * Returns Integer and String SID
 * 
 * @author TKasekamp
 * 
 */
public class AuthenticationResponse {
	private int response;
	private String SID;

	public AuthenticationResponse(int response, String sID) {
		super();
		this.response = response;
		SID = sID;
	}

	public AuthenticationResponse() {
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
		return "AuthenticationResponse [response=" + response + ", SID=" + SID + "]";
	}

}
