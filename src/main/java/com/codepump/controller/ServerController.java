package com.codepump.controller;

import com.codepump.service.AuthenicationService;
import com.codepump.service.CodeService;
import com.codepump.service.UserService;
import com.codepump.service.impl.AuthenticationServiceImpl;
import com.codepump.service.impl.CodeServiceImpl;
import com.codepump.service.impl.UserServiceImpl;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * This is the 'main' class of the whole app. It holds the implementation of the
 * services and the boolean value that turns on the database connection. <br>
 * Other classes should refer to these services from here.<br>
 * 
 * @author TKasekamp
 * 
 */
public class ServerController {
	public static CodeService codeServer;
	public static UserService userServer;
	public static AuthenicationService authenticationServer;
	public static final boolean USE_DATABASE = true; // TRUE WHEN DEPLOYING

	static {

		if (USE_DATABASE) {
			Module module1 = new FactoryModuleBuilder()
					.build(CodeService.CodeServiceFactory.class);
			Injector injector1 = Guice.createInjector(module1);
			codeServer = injector1.getInstance(CodeServiceImpl.class);

			Module module2 = new FactoryModuleBuilder()
					.build(UserService.UserServiceFactory.class);
			Injector injector2 = Guice.createInjector(module2);
			userServer = injector2.getInstance(UserServiceImpl.class);

			Module module3 = new FactoryModuleBuilder()
					.build(AuthenicationService.AuthServiceFactory.class);
			Injector injector3 = Guice.createInjector(module3);
			authenticationServer = injector3
					.getInstance(AuthenticationServiceImpl.class);

		} else {
			codeServer = new CodeServiceImpl();
			userServer = new UserServiceImpl();
			authenticationServer = new AuthenticationServiceImpl();
		}
	}

}
