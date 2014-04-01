package com.codepump.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.codepump.controller.ServerController;
import com.codepump.data.CodeItem;
import com.codepump.deserializer.CodeItemDeserializer;
import com.codepump.service.CodeService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

@WebServlet(value = "/data")
public class DataServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Gson gsonPost;
	public static CodeService codeServ;

	@Override
	public void init() throws ServletException {
		super.init();
		// Deserializer
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(CodeItem.class,
				new CodeItemDeserializer());
		gsonPost = gsonBuilder.create();
		
		// Services
		codeServ = ServerController.codeServer;
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
//			System.out.println(codeServ.getLastRecentItem());
			try {
				RecentSocketController.find(req.getServletContext()).loadMostRecent();
			} catch (NullPointerException e) {
				System.out.println("Tartu, we have a problem. Actually no twats are looking at our websockets.");
			}
		} catch (JsonParseException ex) {
			System.err.println(ex);
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
		}
	}

}
