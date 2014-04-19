package com.codepump.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.codepump.data.User;
import com.codepump.deserializer.UserDeserializer;
import com.codepump.response.AuthenticationResponse;
import com.codepump.service.AuthenicationService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Handles all login. Google login goes to /login/google, AJAX login to
 * /login/ajax and NOJS login to /login/nojs.
 * 
 * @author TKasekamp
 * 
 */
// @WebServlet(value = "/login/*")
@Singleton
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Gson gson;
	private AuthenicationService authServ;

	@Override
	public void init() throws ServletException {
		super.init();
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(User.class, new UserDeserializer());
		gson = gsonBuilder.create();
	}

	@Inject
	public LoginServlet(AuthenicationService authServ) {
		this.authServ = authServ;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String uri = req.getRequestURI();
		if (uri.equals("/login/ajax")) {
			ajaxLogin(req, resp);
		} else if (uri.equals("/login/nojs")) {
			noJsLogin(req, resp);
		} else if (uri.equals("/login/google")) {
			googleLogin(req, resp);
		}

	}

	private void ajaxLogin(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		try {
			// Checking user
			User user = gson.fromJson(req.getReader(), User.class);
			AuthenticationResponse r = authServ.checkPassword(user);
			if (r.getResponse() == 3) {
				createCookie(resp, r.getSID());
			}
			// TODO these responses must be better represented
			resp.setHeader("Content-Type", "application/json");
			resp.getWriter()
					.write("{\"response\":\"" + r.getResponse() + "\"}");

		} catch (JsonParseException ex) {
			System.err.println(ex);
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
		}
	}

	private void noJsLogin(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		User user = new User("", req.getParameter("email"),
				req.getParameter("password"));

		AuthenticationResponse r = authServ.checkPassword(user);
		if (r.getResponse() == 0) {
			resp.sendRedirect("/login.html?nojs=true&result=nouser");
			return;
		} else if (r.getResponse() == 2) {
			resp.sendRedirect("/login.html?nojs=true&result=wrongpass");
			return;
		}

		createCookie(resp, r.getSID());

		if (req.getParameter("nojs") != null) {
			resp.sendRedirect("/index.html?nojs=true");
		} else {
			resp.sendRedirect("/index.html");
		}
	}

	private void googleLogin(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		try {
			User user = gson.fromJson(req.getReader(), User.class);
			AuthenticationResponse r = authServ.checkPassword(user);
			if (r.getResponse()==0 && user.getPassword()==""){
				resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "no user");
			} else {
				createCookie(resp, authServ.googleLogin(user));
			}
		} catch (JsonParseException ex) {
			System.err.println(ex);
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
		}
	}

	private void createCookie(HttpServletResponse resp, String SID) {
		// I AM THE COOKIEMONSTER
		Cookie cookie = new Cookie("SID", SID);
		cookie.setMaxAge(2 * 60 * 60); // 2 h
		cookie.setPath("/"); // This is important
		resp.addCookie(cookie);
	}

}
