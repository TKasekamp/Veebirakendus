package com.codepump.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.codepump.controller.ServerController;
import com.codepump.response.AuthenticationResponse;
import com.codepump.service.AuthenicationService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

@WebServlet(value = "/logout")
public class LogOutServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Gson gson;
	private static AuthenicationService authServ;

	@Override
	public void init() throws ServletException {
		super.init();
		gson = new GsonBuilder().create();
		authServ= ServerController.authenticationServer;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {

			AuthenticationResponse r = gson.fromJson(req.getReader(),
					AuthenticationResponse.class);
			int response = authServ.logOut(r);
			resp.setHeader("Content-Type", "application/json");
			// What the response is is not really important as long as there is
			// a response
			resp.getWriter().write(
					"{\"response\":\"" + Integer.toString(response) + "\"}");

		} catch (JsonParseException ex) {
			System.err.println(ex);
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
		}
	}

}