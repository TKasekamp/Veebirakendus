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

//@WebServlet(value = "/login")
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
		try {
			// Checking user
			User user = gson.fromJson(req.getReader(), User.class);
			AuthenticationResponse r = authServ.checkPassword(user);
			if (r.getResponse() == 3) {
				Cookie c = new Cookie("SID", authServ.googleLogin(user));
				resp.addCookie(c);
			}
			System.out.println("User login result: " + r.toString());
			resp.setHeader("Content-Type", "application/json");
			resp.getWriter().write(gson.toJson(r));
			// resp.getWriter()
			// .write("{\"response\":\"" + r.getResponse() + "\"}");

		} catch (JsonParseException ex) {
			System.err.println(ex);
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
		}
	}

}
