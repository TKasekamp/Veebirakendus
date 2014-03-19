package com.codepump.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.codepump.controller.ServerController;
import com.codepump.data.CodeItem;

import com.codepump.serializer.CodeItemSerializerAll;

import com.codepump.service.CodeService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@WebServlet(value = "/mystuff")
public class MyStuffServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private Gson gsonGetAll; // For replyWithAll

	public static CodeService codeServ;

	@Override
	public void init() throws ServletException {
		super.init();

		// Configure GSON
		gsonGetAll = new GsonBuilder().registerTypeAdapter(CodeItem.class,
				new CodeItemSerializerAll()).create();

		// Services
		codeServ = ServerController.codeServer;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setHeader("Content-Type", "application/json");

		String SID = req.getParameter("SID");
		if (SID != null) {
			replyWithAllUserItems(resp, SID);

		}
	}

	private void replyWithAllUserItems(HttpServletResponse resp, String SID)
			throws IOException {
		List<CodeItem> allContent = codeServ.getAllUserItems(SID);
		resp.getWriter().write(gsonGetAll.toJson(allContent));

	}

}
