package com.codepump.service;

import com.codepump.data.User;
import com.codepump.response.AuthenticationResponse;

/**
 * Handles logging in and logging out.
 * @author TKasekamp
 *
 */
public interface AuthenicationService {
	/**
	 * Checks the database for this user and compares the hashes.
	 * @param user User trying to log in.
	 * @return AuthenticationResponse
	 */
	public AuthenticationResponse checkPassword(User user);

	public String generateSID();

	/**
	 * Removes the user with this id from logged in user list.
	 * @param r holds userID
	 * @return response
	 */
	public int logOut(AuthenticationResponse r);

	/**
	 * Finds the ID of the user with this SID.
	 * @param SID cookie value
	 * @return User ID
	 */
	public int getUserWithSID(String SID);

}
