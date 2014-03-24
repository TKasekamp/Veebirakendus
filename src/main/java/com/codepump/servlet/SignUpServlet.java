package com.codepump.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.codepump.controller.ServerController;
import com.codepump.data.User;
import com.codepump.deserializer.UserDeserializer;
import com.codepump.service.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

@WebServlet(value = "/signup")
public class SignUpServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Gson gson;
	private static UserService userServ;

	@Override
	public void init() throws ServletException {
		super.init();
	    GsonBuilder gsonBuilder = new GsonBuilder();
	    gsonBuilder.registerTypeAdapter(User.class,
	            new UserDeserializer());
	    gson = gsonBuilder.create();
		userServ = ServerController.userServer;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			User user = gson.fromJson(req.getReader(), User.class);
			int result = userServ.addUser(user);
			resp.setHeader("Content-Type", "application/json");
			if (result == 0) {
				System.out.println("Adding user");
				System.out.println(user);		
				resp.getWriter().write(
						"{\"response\":\"User created\"}");
			}
			else {
				System.out.println("user already exists");
				resp.getWriter().write(
						"{\"response\":\"A user with this name already exists\"}");
			}
		} catch (JsonParseException ex) {
			System.err.println(ex);
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
		}
	}

}
