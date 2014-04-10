package com.codepump.injector;

import javax.servlet.annotation.WebListener;

import com.codepump.servlet.DataServlet;
import com.codepump.servlet.DataServlet2;
import com.codepump.servlet.EditServlet;
import com.codepump.servlet.GoogleLoginServlet;
import com.codepump.servlet.LogOutServlet;
import com.codepump.servlet.LoginNoJsServlet;
import com.codepump.servlet.LoginServlet;
import com.codepump.servlet.RecentSocketController;
import com.codepump.servlet.SignUpServlet;
import com.codepump.velocity.Velocity;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

/**
 * Google Guice servlet injection happens here. This class is run before any
 * other servlet. PageExpiryFilter still mapped in web.xml. <br>
 * configureServlets is esentially web.xml in Java form. @Webservlet annotations
 * in servlets are unnecessary.
 * 
 * @author TKasekamp
 * 
 */
@WebListener
public class GuiceInjector extends GuiceServletContextListener {

	public GuiceInjector() {
	}

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new ServletModule() {
			@Override
			protected void configureServlets() {
				serve("*.html").with(Velocity.class);
				serve("/data").with(DataServlet.class);
				serve("/data2").with(DataServlet2.class);
				serve("/edit").with(EditServlet.class);
				serve("/glogin").with(GoogleLoginServlet.class);
				serve("/loginnojs").with(LoginNoJsServlet.class);
				serve("/login").with(LoginServlet.class);
				serve("/logout").with(LogOutServlet.class);
				serve("/feed").with(RecentSocketController.class);
				serve("/signup").with(SignUpServlet.class);

			}
		});
	}

}
