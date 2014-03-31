package com.codepump.controller;

import com.codepump.service.AuthenicationService;
import com.codepump.service.CodeService;
import com.codepump.service.UserService;
import com.codepump.service.impl.AuthenticationServiceImpl;
import com.codepump.service.impl.CodeServiceImpl;
import com.codepump.service.impl.UserServiceImpl;


/**
 * This is the 'main' class of the whole app. It holds the implementation of the services and the boolean value that turns on the database connection. <br>
 * Other classes should refer to these services from here.<br>
 * @author TKasekamp
 *
 */
public class ServerController {
	public static CodeService codeServer;
	public static UserService userServer;
	public static AuthenicationService authenticationServer;
	public static final boolean USE_DATABASE = true; // TRUE WHEN DEPLOYING

	static {
		codeServer = new CodeServiceImpl();
		userServer = new UserServiceImpl();
		authenticationServer = new AuthenticationServiceImpl();
	}

}
