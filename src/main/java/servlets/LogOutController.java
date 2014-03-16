package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import data.LoginResponse;
import datastore.UserDataProvider;

@WebServlet(value = "/logout")
public class LogOutController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Gson gson; 
	private static UserDataProvider datastore;

	@Override
	public void init() throws ServletException {
		super.init();
		gson = new GsonBuilder().create();
		datastore = PumpController.userstore;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			
			// Using LoginResponse here is terrible, has to be fixed
			LoginResponse r = gson.fromJson(req.getReader(), LoginResponse.class);
			int response = datastore.logOut(r);
			resp.setHeader("Content-Type", "application/json");
			resp.getWriter().write("{\"r\":\"" + Integer.toString(response) + "\"}");
			
			
		} catch (JsonParseException ex) {
			System.err.println(ex);
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
		}
	}
	


}