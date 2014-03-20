package com.codepump.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.codepump.controller.ServerController;
import com.codepump.service.UserService;
import com.codepump.tempobject.UserStatisticsItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@WebServlet(value = "/statistics")
public class UserStatisticsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private Gson gsonGetAll;

	public static UserService userServ;

	@Override
	public void init() throws ServletException {
		super.init();

		// Configure GSON
		gsonGetAll = new GsonBuilder().create();

		// Services
		userServ = ServerController.userServer;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setHeader("Content-Type", "application/json");

		String SID = req.getParameter("SID");
		if (SID != null) {
			replyWithUserStatistics(resp, SID);

		}
	}

	private void replyWithUserStatistics(HttpServletResponse resp, String SID)
			throws IOException {
		UserStatisticsItem allContent = userServ.findUserStatistics(SID);
		resp.getWriter().write(gsonGetAll.toJson(allContent));

	}

}
