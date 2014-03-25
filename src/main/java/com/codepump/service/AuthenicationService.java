package com.codepump.service;

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
	 * Checks the database for this user and compares the hashes.
	 * 
	 * @param user
	 *            User trying to log in.
	 * @return AuthenticationResponse
	 */
	public AuthenticationResponse checkPassword(User user);

	public String generateSID();

	/**
	 * Removes the user with this id from logged in user list.
	 * 
	 * @param r
	 *            holds userID
	 * @return response
	 */
	public int logOut(AuthenticationResponse r);

	/**
	 * Finds the ID of the user with this SID. If no such user is found, returns
	 * -1 that marks Public user.
	 * 
	 * @param SID
	 *            cookie value
	 * @return User ID or -1 for Public user
	 */
	public int getUserWithSID(String SID);

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

}
