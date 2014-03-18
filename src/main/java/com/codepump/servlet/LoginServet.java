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
import com.codepump.response.AuthenticationResponse;
import com.codepump.service.AuthenicationService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

@WebServlet(value = "/login")
public class LoginServet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Gson gson; 
	private static AuthenicationService authServ;

	@Override
	public void init() throws ServletException {
		super.init();
	    GsonBuilder gsonBuilder = new GsonBuilder();
	    gsonBuilder.registerTypeAdapter(User.class,
	            new UserDeserializer());
	    gson = gsonBuilder.create();
		authServ = ServerController.authenticationServer;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {

			// Checking user
			User user = gson.fromJson(req.getReader(), User.class);
			AuthenticationResponse r = authServ.checkPassword(user);
			System.out.println("User login result: "+ r.toString());
			resp.setHeader("Content-Type", "application/json");
			resp.getWriter().write(gson.toJson(r));
			
		} catch (JsonParseException ex) {
			System.err.println(ex);
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
		}
	}
	


}
