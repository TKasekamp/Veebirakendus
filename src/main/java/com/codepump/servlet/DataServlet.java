package com.codepump.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.codepump.data.CodeItem;
import com.codepump.deserializer.CodeItemDeserializer;
import com.codepump.service.CodeService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Handles code creation. Code made with JS go to /data/ajax, NOJS goes to
 * /data/nojs. Updates websocket after code creation.
 * 
 * @author TKasekamp
 * 
 */

// @WebServlet(value = "/data/*")
@Singleton
public class DataServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private CodeService codeServ;
	private Gson gsonPost;

	@Override
	public void init() throws ServletException {
		super.init();
	}

	@Inject
	public DataServlet(CodeService codeServ) {
		this.codeServ = codeServ;
		// Deserializer
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(CodeItem.class,
				new CodeItemDeserializer());
		gsonPost = gsonBuilder.create();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String SID = getCookies(req);
		String uri = req.getRequestURI();
		if (uri.equals("/data/ajax")) {
			handleCodePostAjax(req, resp, SID);
		} else if (uri.equals("/data/nojs")) {
			handleCodePostNoJs(req, resp, SID);
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

	private void handleCodePostAjax(HttpServletRequest req,
			HttpServletResponse resp, String SID) throws ServletException,
			IOException {
		try {
			CodeItem item = gsonPost.fromJson(req.getReader(), CodeItem.class);
			codeServ.addCode(item, SID);

			resp.setHeader("Content-Type", "application/json");
			resp.getWriter().write(item.JsonID());

		} catch (JsonParseException ex) {
			System.err.println(ex);
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
		}
	}

	private void handleCodePostNoJs(HttpServletRequest req,
			HttpServletResponse resp, String SID) throws ServletException,
			IOException {
		CodeItem item = new CodeItem(req.getParameter("title"),
				req.getParameter("text"), req.getParameter("language"),
				req.getParameter("privacy"));

		codeServ.addCode(item, SID);
		// TODO some kind of error reporting

		// Redirecting
		if (req.getParameter("nojs").equalsIgnoreCase("true")) {
			resp.sendRedirect("/source.html?id=" + item.getId() + "&nojs=true");
			return;
		} else {
			resp.sendRedirect("/source.html?id=" + item.getId());
		}

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
}
