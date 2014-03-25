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
import com.codepump.deserializer.CodeItemDeserializer;
import com.codepump.serializer.CodeItemSerializerAll;
import com.codepump.serializer.CodeItemSerializerNormal;
import com.codepump.service.CodeService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

@WebServlet(value = "/data")
public class DataServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Gson gsonGetNormal;
	private Gson gsonGetAll; // For replyWithAll
	private Gson gsonPost;
	public static CodeService codeServ;

	@Override
	public void init() throws ServletException {
		super.init();
		// Configure GSON
		gsonGetNormal = new GsonBuilder().registerTypeAdapter(CodeItem.class,
				new CodeItemSerializerNormal()).create();
		gsonGetAll = new GsonBuilder().registerTypeAdapter(CodeItem.class,
				new CodeItemSerializerAll()).create();
		// Deserializer
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(CodeItem.class,
				new CodeItemDeserializer());
		gsonPost = gsonBuilder.create();

		// Services
		codeServ = ServerController.codeServer;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setHeader("Content-Type", "application/json");

		String idString = req.getParameter("id");
		if (idString != null) {
			if (idString.equals("")) {
				replyWithAllItems(resp);
			} else {
				replyWithSingleItem(resp, idString);
			}

		} else {
			replyWithAllItems(resp);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			CodeItem item = gsonPost.fromJson(req.getReader(), CodeItem.class);
			codeServ.addCode(item);
			System.out.println("Added item");
			System.out.println(item);
			resp.setHeader("Content-Type", "application/json");
			// resp.getWriter().write(gson.toJson(item));
			resp.getWriter().write(item.JsonID());
//			RecentSocketController.find(req.getServletContext()).broadcast("hello");
		} catch (JsonParseException ex) {
			System.err.println(ex);
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
		}
	}

	private void replyWithAllItems(HttpServletResponse resp) throws IOException {
		List<CodeItem> allContent = codeServ.findAllItems();
		resp.getWriter().write(gsonGetAll.toJson(allContent));
		System.out.println("wanted all public stuff");
	}

	private void replyWithSingleItem(HttpServletResponse resp, String idString)
			throws IOException {
		int id = Integer.parseInt(idString);
		CodeItem item = codeServ.findItemById(id);
		resp.getWriter().write(gsonGetNormal.toJson(item));
	}

}
