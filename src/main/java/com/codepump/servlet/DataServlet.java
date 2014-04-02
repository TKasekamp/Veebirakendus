package com.codepump.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.codepump.controller.ServerController;
import com.codepump.data.CodeItem;
import com.codepump.data.User;
import com.codepump.service.CodeService;
import com.codepump.service.UserService;

@WebServlet(value = "/data")
public class DataServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public static CodeService codeServ;
	private static UserService userServ;

	@Override
	public void init() throws ServletException {
		super.init();
		// Services
		codeServ = ServerController.codeServer;
		userServ = ServerController.userServer;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// Finding logged in user
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

		User user = userServ.findUserBySID(SID);
		CodeItem item = new CodeItem(req.getParameter("title"),
				req.getParameter("text"), req.getParameter("language"),
				req.getParameter("privacy"));
		// If no user then must be public
		// Creating new public user
		System.out.println("Logged in user");
		System.out.println(user);
		if (user == null) {
			user = new User();
			user.setId(-1);
			item.setUser(user);
		} else {
			item.setUser(user);
		}

		codeServ.addCode(item);
		System.out.println("Added item");
		System.out.println(item);

		// Redirecting
		if (req.getParameter("nojs").equalsIgnoreCase("true")) {
			resp.sendRedirect("/source.html?id=" + item.getId() + "&nojs=true");
			return;
		} else {
			resp.sendRedirect("/source.html?id=" + item.getId());
		}

		// Websocket announcement
		try {
			RecentSocketController.find(req.getServletContext())
					.loadMostRecent();
		} catch (NullPointerException e) {
			System.out
					.println("Tartu, we have a problem. Actually no twats are looking at our websockets.");
		}

	}

}
