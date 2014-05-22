package com.codepump.injector;

import javax.servlet.annotation.WebListener;

import com.codepump.controller.ServerController;
import com.codepump.filter.HibernateSessionRequestFilter;
import com.codepump.service.AuthenicationService;
import com.codepump.service.CodeService;
import com.codepump.service.SearchService;
import com.codepump.service.UserService;
import com.codepump.service.nodatabase.AuthServiceNoDB;
import com.codepump.service.nodatabase.SearchServiceNoDB;
import com.codepump.service.impl.AuthenticationServiceImpl;
import com.codepump.service.impl.CodeServiceImpl;
import com.codepump.service.impl.SearchServiceImpl;
import com.codepump.service.nodatabase.CodeServiceNoDB;
import com.codepump.service.impl.UserServiceImpl;
import com.codepump.service.nodatabase.UserServiceNoDB;
import com.codepump.servlet.DataServlet;
import com.codepump.servlet.EditServlet;
import com.codepump.servlet.LogOutServlet;
import com.codepump.servlet.LoginServlet;
import com.codepump.socket.RecentSocketController;
import com.codepump.servlet.SearchServlet;
import com.codepump.servlet.SignUpServlet;
import com.codepump.velocity.Velocity;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

/**
 * Google Guice servlet injection happens here. This class is run before any
 * other servlet. PageExpiryFilter still mapped in web.xml. <br>
 * configureServlets is essentially web.xml in Java form. @Webservlet annotations
 * in servlets are unnecessary.<br>
 * 
 * USE_DATABASE is used to set interface implementations.
 * 
 * @author TKasekamp
 * 
 */
@WebListener
public class GuiceInjector extends GuiceServletContextListener {
	private final boolean USE_DATABASE = ServerController.USE_DATABASE;

	public GuiceInjector() {
	}

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new ServletModule() {
			@Override
			protected void configureServlets() {
				// This is the only method where USE_DATABASE is checked
				if (USE_DATABASE) {
					this.bind(AuthenicationService.class)
							.to(AuthenticationServiceImpl.class)
							.asEagerSingleton();
					this.bind(UserService.class).to(UserServiceImpl.class)
							.asEagerSingleton();
					this.bind(CodeService.class).to(CodeServiceImpl.class)
							.asEagerSingleton();
					this.bind(SearchService.class).to(SearchServiceImpl.class)
							.asEagerSingleton();
				} else {
					this.bind(AuthenicationService.class)
							.to(AuthServiceNoDB.class).asEagerSingleton();
					this.bind(UserService.class).to(UserServiceNoDB.class)
							.asEagerSingleton();
					this.bind(CodeService.class).to(CodeServiceNoDB.class)
							.asEagerSingleton();
					this.bind(SearchService.class).to(SearchServiceNoDB.class)
							.asEagerSingleton();
				}

				if (USE_DATABASE) {
					 filter("/*").through(HibernateSessionRequestFilter.class);
				}
				// This mapping is not pretty, but how to do it better?
				serve("*.html").with(Velocity.class);
				serve("/data").with(DataServlet.class);
				serve("/edit/*").with(EditServlet.class);
				serve("/login/*").with(LoginServlet.class);
				serve("/logout").with(LogOutServlet.class);
				serve("/feed").with(RecentSocketController.class);
				serve("/signup").with(SignUpServlet.class);
				serve("/search").with(SearchServlet.class);

			}
		});
	}

}
