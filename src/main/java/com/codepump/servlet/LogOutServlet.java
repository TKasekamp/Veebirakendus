package com.codepump.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.codepump.controller.ServerController;
import com.codepump.response.AuthenticationResponse;
import com.codepump.service.AuthenicationService;
import com.google.gson.JsonParseException;

@WebServlet(value = "/logout")
public class LogOutServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static AuthenicationService authServ;

	@Override
	public void init() throws ServletException {
		super.init();
		authServ = ServerController.authenticationServer;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// For NOJS
		String SID = null;
		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("SID")) {
					SID = cookie.getValue();
					break;
				}
			}
		}
		AuthenticationResponse r = new AuthenticationResponse(1, SID);
		authServ.logOut(r);
		Cookie c = new Cookie("SID", "");
		c.setMaxAge(0); // Telling it to die
		resp.addCookie(c);
		if (req.getParameter("nojs") != null) {
			resp.sendRedirect("/index.html?nojs=true");
		} else {
			resp.sendRedirect("/index.html");
		}
		return;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			String SID = null;
			Cookie[] cookies = req.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals("SID")) {
						SID = cookie.getValue();
						break;
					}
				}
			}
			AuthenticationResponse r = new AuthenticationResponse(1, SID);
			int response = authServ.logOut(r);
			// Empty cookie
			Cookie c = new Cookie("SID", "");
			c.setMaxAge(0); // No more cookie
			resp.addCookie(c); 
			resp.setHeader("Content-Type", "application/json");
			// What the response is is not really important as long as there
			// is
			// a response
			resp.getWriter().write(
					"{\"response\":\"" + Integer.toString(response) + "\"}");
			return;

		} catch (JsonParseException ex) {
			System.err.println(ex);
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
		}
	}

}