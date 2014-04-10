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

//@WebServlet(value = "/data2")
@Singleton
public class DataServlet2 extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Gson gsonPost;
	private CodeService codeServ;

	@Override
	public void init() throws ServletException {
		super.init();
		// Deserializer
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(CodeItem.class,
				new CodeItemDeserializer());
		gsonPost = gsonBuilder.create();
	}

	@Inject
	public DataServlet2(CodeService codeServ) {
		this.codeServ = codeServ;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
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

		try {
			CodeItem item = gsonPost.fromJson(req.getReader(), CodeItem.class);
			codeServ.addCode(item, SID);
			System.out.println("Added item");
			System.out.println(item);
			resp.setHeader("Content-Type", "application/json");
			resp.getWriter().write(item.JsonID());
			try {
				RecentSocketController.find(req.getServletContext())
						.loadMostRecent();
			} catch (NullPointerException e) {
				System.out
						.println("Tartu, we have a problem. Actually no twats are looking at our websockets.");
			}
		} catch (JsonParseException ex) {
			System.err.println(ex);
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
		}
	}
}