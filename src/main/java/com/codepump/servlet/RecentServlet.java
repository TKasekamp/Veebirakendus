package com.codepump.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.codepump.controller.ServerController;
import com.codepump.serializer.RecentItemSerializer;
import com.codepump.service.CodeService;
import com.codepump.tempobject.RecentItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

//@WebServlet(value = "/recent")
@Deprecated
public class RecentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Gson gson;
	private static CodeService codeServ;

	@Override
	public void init() throws ServletException {
		super.init();
		gson = new GsonBuilder().registerTypeAdapter(RecentItem.class,
				new RecentItemSerializer()).create();
		codeServ = ServerController.codeServer;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setHeader("Content-Type", "application/json");

		String idString = req.getParameter("id");
		if (idString != null) {
			if (idString.equals("")) {
				replyWithRecentItems(resp);
			} 

		} else {
			replyWithRecentItems(resp);
		}
	}

	private void replyWithRecentItems(HttpServletResponse resp)
			throws IOException {
		List<RecentItem> allContent = codeServ.getRecentItems();
		resp.getWriter().write(gson.toJson(allContent));
	}

}