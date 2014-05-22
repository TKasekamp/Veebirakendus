package com.codepump.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.codepump.service.AuthenicationService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Handles logging out. Ajax logout uses the doPost method to get rid of the
 * cookie. NOJS uses doGet method.
 * 
 * @author TKasekamp
 * 
 */
// @WebServlet(value = "/logout")
@Singleton
public class LogOutServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private AuthenicationService authServ;

	@Override
	public void init() throws ServletException {
		super.init();
	}

	@Inject
	public LogOutServlet(AuthenicationService authServ) {
		this.authServ = authServ;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// For NOJS
		String SID = getCookies(req);
		try {
			authServ.logOut(SID);
		} catch (NullPointerException e) {
			// No such user in logged in, but this is no problem
		}
		killCookie(resp);

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
		String SID = getCookies(req);

		authServ.logOut(SID);
		// Empty cookie
		killCookie(resp);
		resp.setHeader("Content-Type", "application/json");
		// What the response is is not really important as long as there
		// is a response
		resp.getWriter().write("{\"response\":\"a\"}");
		return;
	}

	private String getCookies(HttpServletRequest req) {
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
		return SID;
	}

	private void killCookie(HttpServletResponse resp) {
		Cookie c = new Cookie("SID", "");
		c.setMaxAge(0); // No more cookie
		resp.addCookie(c);
	}

}