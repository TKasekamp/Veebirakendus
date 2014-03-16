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

import data.User;
import datastore.UserDataProvider;

@WebServlet(value = "/signup")
public class SignUpController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Gson gson;
	private UserDataProvider datastore;

	@Override
	public void init() throws ServletException {
		super.init();
		gson = new GsonBuilder().create();
		datastore = PumpController.userstore;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			User user = gson.fromJson(req.getReader(), User.class);
			datastore.addUser(user);
			System.out.println("Adding user");
			System.out.println(user);
		} catch (JsonParseException ex) {
			System.err.println(ex);
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
		}
	}

}
