package com.codepump.service;

import java.util.Map;

import com.codepump.data.User;
import com.codepump.response.AuthenticationResponse;
import com.codepump.tempobject.EditContainer;

/**
 * Handles logging in and logging out.
 * 
 * @author TKasekamp
 * 
 */
public interface AuthenicationService {

	/**
	 * Checks if there is a SID attached to this edit. Then finds the user with
	 * this SID. Then looks at the database to find the owner of the item to be
	 * changed.
	 * 
	 * @param item
	 *            EditContainer
	 * @return true if the user is the owner of this code<br>
	 *         false if there is no SID or it doesn't belong to the owner of
	 *         this code.
	 */
	public boolean authoriseEdit(EditContainer item);

	/**
	 * Checks the database for this user and compares the hashes.
	 * 
	 * @param user
	 *            User trying to log in.
	 * @return AuthenticationResponse
	 */
	public AuthenticationResponse checkPassword(User user);

	/**
	 * Direct login. <b>ONLY TO BE USED DURING SIGNUP!</b> <br>
	 * Searches the DB with email to get id of this user<br>
	 * Then adds the ID to logged in users. <br>
	 * 
	 * @param email
	 *            Email of user
	 * @return SID Session ID for cookie
	 */
	public String directLogin(String email);

	/**
	 * Generates random string
	 */
	public String generateSID();

	/**
	 * Finds the ID of the user with this SID. If no such user is found, returns
	 * -1 that marks Public user.
	 * 
	 * @param SID
	 *            cookie value
	 * @return User ID or -1 for Public user
	 */
	public int getUserIdWithSID(String SID);

	/**
	 * Google login handler. Tries to log in user wiht checkPassword. If not
	 * found in DB creates a new one.
	 * 
	 * @param user
	 *            Google user
	 * @return SID Session ID for cookie
	 */
	public String googleLogin(User user);

	/**
	 * Removes this SID from logged in user list.
	 * 
	 * @param SID
	 *            cookie value
	 */
	public void logOut(String SID);

	public Map<String, Integer> getSidList();
}
