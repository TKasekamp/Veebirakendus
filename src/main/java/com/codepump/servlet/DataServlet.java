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
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Handles code creation. Updates websocket after code creation.
 * 
 * @author TKasekamp
 * 
 */

// @WebServlet(value = "/data")
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
		CodeItem item = new CodeItem(req.getParameter("title"),
				req.getParameter("text"), req.getParameter("language"),
				req.getParameter("privacy"));
		
		if (item.getName().toLowerCase().startsWith("rick")) {
			resp.sendRedirect("https://www.youtube.com/watch?v=dQw4w9WgXcQ");
			return;
		} else {
			Boolean codeAdded = codeServ.addCode(item, SID);
			if (codeAdded) {
				// Redirecting
				if (req.getParameter("nojs").equalsIgnoreCase("true")) {
					resp.sendRedirect("/source.html?id=" + item.getId()
							+ "&nojs=true");
					return;
				} else {
					resp.sendRedirect("/source.html?id=" + item.getId());
				}
			} else {
				// TODO some kind of error reporting
				resp.sendRedirect("/index.html?result=error");
				return;
			}
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
