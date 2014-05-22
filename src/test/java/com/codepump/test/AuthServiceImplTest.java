package com.codepump.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.codepump.data.CodeItem;
import com.codepump.data.User;
import com.codepump.response.AuthenticationResponse;
import com.codepump.service.DatabaseService;
import com.codepump.service.impl.AuthenticationServiceImpl;
import com.codepump.service.impl.DatabaseServiceImpl;
import com.codepump.data.container.EditContainer;

import static org.mockito.Mockito.*;

public class AuthServiceImplTest {
	private DatabaseService dataServ;
	private User user;

	@Before
	public void setUp() {
		dataServ = mock(DatabaseServiceImpl.class);
		// This is user qwe in database with hashed password
		user = new User(1, "qwe", "qwe", "a4b1e5982418ef7e4ec8005794bc2b41");

	}

	/**
	 * Testing when correct user tries to log in and is already in database.
	 * Creating this test has made me realise that this checkPassword is WAY too
	 * big.
	 */
	@Test
	public void testCheckPassword() {
		when(dataServ.findUserByEmail("qwe")).thenReturn(user);
		AuthenticationServiceImpl authServ = new AuthenticationServiceImpl(
				dataServ);
		User newUser = new User("", "qwe", "qwe");
		AuthenticationResponse r = authServ.checkPassword(newUser);
		// Correct response
		assertEquals(3, r.getResponse());
		// Check if have SID included
		String sid = r.getSID();
		assertEquals(32, sid.length());

		// Check if included in SIDlist
		int id = authServ.getSidList().get(sid);
		assertEquals(1, id);
	}

	@Test
	public void testCheckPasswordNoUser() {
		when(dataServ.findUserByEmail("qwe")).thenReturn(null);
		AuthenticationServiceImpl authServ = new AuthenticationServiceImpl(
				dataServ);
		User newUser = new User("", "qwe", "qwe");
		AuthenticationResponse r = authServ.checkPassword(newUser);
		assertEquals(0, r.getResponse());
		assertEquals(null, r.getSID());
	}

	@Test
	public void testCheckPasswordWrongPassword() {
		when(dataServ.findUserByEmail("qwe")).thenReturn(user);
		AuthenticationServiceImpl authServ = new AuthenticationServiceImpl(
				dataServ);
		User newUser = new User("", "qwe", "qwe1");
		AuthenticationResponse r = authServ.checkPassword(newUser);
		assertEquals(2, r.getResponse());
		assertEquals(null, r.getSID());
	}

	@Test
	public void testLogOut() {
		AuthenticationServiceImpl authServ = new AuthenticationServiceImpl(
				dataServ);
		authServ.getSidList().put("hello", 1);
		authServ.logOut("hello");
		assertEquals(null, authServ.getSidList().get("hello"));
	}

	@Test
	public void testGetUserIdWithSID() {
		AuthenticationServiceImpl authServ = new AuthenticationServiceImpl(
				dataServ);
		authServ.getSidList().put("hello", 1);
		assertEquals(1, authServ.getUserIdWithSID("hello"));
	}

	@Test
	public void testAuthoriseEditHasRight() {
		when(dataServ.findCodeItemById(100)).thenReturn(
				new CodeItem(100, "name", "text", "", "", null, null, user));
		AuthenticationServiceImpl authServ = new AuthenticationServiceImpl(
				dataServ);
		authServ.getSidList().put("hello", 1);
		EditContainer con = new EditContainer();
		con.setId(100);
		con.setSID("hello");
		assertTrue(authServ.authoriseEdit(con));
	}

	@Test
	public void testAuthoriseEditPublic() {
		when(dataServ.findCodeItemById(100)).thenReturn(
				new CodeItem(100, "name", "text", "", "", null, null, user));
		AuthenticationServiceImpl authServ = new AuthenticationServiceImpl(
				dataServ);

		EditContainer con = new EditContainer();
		con.setId(100);

		// User with incorrect SID
		EditContainer con2 = new EditContainer();
		con2.setId(100);
		con2.setSID("some random stuff");
		assertFalse(authServ.authoriseEdit(con));
		assertFalse(authServ.authoriseEdit(con2));
	}

	@Test
	public void testDirectLogin() {
		when(dataServ.findUserByEmail("qwe")).thenReturn(user);
		AuthenticationServiceImpl authServ = new AuthenticationServiceImpl(
				dataServ);
		String sid = authServ.directLogin("qwe");
		int id = authServ.getSidList().get(sid);
		assertEquals(1, id);
	}

	@Test
	public void testGoogleLoginUserExists() {
		when(dataServ.findUserByEmail("qwe")).thenReturn(user);
		AuthenticationServiceImpl authServ = new AuthenticationServiceImpl(
				dataServ);
		User newUser = new User("", "qwe", "qwe");
		String sid = authServ.googleLogin(newUser);
		int id = authServ.getSidList().get(sid);
		assertEquals(1, id);
	}

	/*
	 * testGoogleLoginNoUser cannot be implemented as it requires changing
	 * dataServ return value during the test. It first has to output null and
	 * then after saveUser a proper User.
	 */

}
