package com.codepump.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.codepump.data.User;
import com.codepump.service.AuthenicationService;
import com.codepump.service.UserService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

//@WebServlet(value = "/signup")
@Singleton
public class SignUpServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private UserService userServ;
	private AuthenicationService authServ;

	@Override
	public void init() throws ServletException {
		super.init();
	}

	@Inject
	public SignUpServlet(UserService userServ, AuthenicationService authServ) {
		this.authServ = authServ;
		this.userServ = userServ;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			User user = new User(req.getParameter("user"),
					req.getParameter("email"), req.getParameter("password1"));
			if (!user.getPassword().equals(req.getParameter("password2"))) {
				resp.sendRedirect("/signup.html?result=pass");
				return;
			}
			user.hashPassword();
			int result = userServ.addUser(user);
			if (result == 0) {
				System.out.println("Adding user");
				System.out.println(user);
				Cookie c = new Cookie("SID", authServ.directLogin(user
						.getEmail()));
				resp.addCookie(c);
				resp.sendRedirect("/index.html");

			} else {
				System.out.println("user already exists");
				resp.sendRedirect("/signup.html?result=userexists");
			}
		} catch (Exception ex) {
		}
	}

}
