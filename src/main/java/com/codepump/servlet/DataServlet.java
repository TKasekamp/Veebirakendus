package com.codepump.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.codepump.data.CodeItem;
import com.codepump.service.CodeService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

//@WebServlet(value = "/data")
@Singleton
public class DataServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private CodeService codeServ;

	@Override
	public void init() throws ServletException {
		super.init();
	}

	@Inject
	public DataServlet(CodeService codeServ) {
		this.codeServ = codeServ;
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
		System.out.println(req.getHeaderNames());
		if (req.getParameter("codeID") != null) {
			handleCodeDelete(req, resp, SID);

		} else {
			handleCodePost(req, resp, SID);
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

	private void handleCodePost(HttpServletRequest req,
			HttpServletResponse resp, String SID) throws ServletException,
			IOException {
		CodeItem item = new CodeItem(req.getParameter("title"),
				req.getParameter("text"), req.getParameter("language"),
				req.getParameter("privacy"));

		codeServ.addCode(item, SID);
		System.out.println("Added item");
		System.out.println(item);

		// Redirecting
		if (req.getParameter("nojs").equalsIgnoreCase("true")) {
			resp.sendRedirect("/source.html?id=" + item.getId() + "&nojs=true");
			return;
		} else {
			resp.sendRedirect("/source.html?id=" + item.getId());
		}

	}

	private void handleCodeDelete(HttpServletRequest req,
			HttpServletResponse resp, String SID) throws ServletException,
			IOException {
		String s = req.getParameter("codeID");
		System.out.println(s);
		codeServ.deleteCode(Integer.parseInt(s));
		// Redirecting
		if (req.getParameter("nojs").equalsIgnoreCase("true")) {
			resp.sendRedirect("/mystuff.html?nojs=true");
			return;
		} else {
			resp.sendRedirect("/mystuff.html");
		}

	}

}
