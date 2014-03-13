package servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import data.User;
import datastore.UserStore;
import datastore.UserDataProvider;

@WebServlet(value = "/signup")
public class SignUpController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Gson gson;
//	private Gson gson2; 
	private UserDataProvider datastore;

	@Override
	public void init() throws ServletException {
		super.init();
		gson = new GsonBuilder().create();
//		gson2 = new GsonBuilder().
//			    registerTypeAdapter(CodeItem.class, new CodeItemSerializerAll()).create();
		datastore = new UserStore();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setHeader("Content-Type", "application/json");

		String idString = req.getParameter("id");
		if (idString != null) {
			replyWithSingleUser(resp, idString);
			System.out.println(idString);
		} else {
			replyWithAllUsers(resp);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			System.out.println("hello");
//			System.out.println(req.getReader().toString());
			User user = gson.fromJson(req.getReader(), User.class);
			datastore.addUser(user); // bid should be validated carefully
			System.out.println("Adding user");
			System.out.println(user);
//			resp.setHeader("Content-Type", "application/json");
//			resp.getWriter().write(gson.toJson(user));
		} catch (JsonParseException ex) {
			System.err.println(ex);
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
		}
	}

	private void replyWithAllUsers(HttpServletResponse resp) throws IOException {
		List<User> allContent = datastore.findAllUsers();
		// Costum serializer http://stackoverflow.com/questions/8572568/how-to-serialize-such-a-custom-type-to-json-with-google-gson
		resp.getWriter().write(gson.toJson(allContent));
		System.out.println("all users");
		System.out.println(allContent);
	}

	private void replyWithSingleUser(HttpServletResponse resp, String idString)
			throws IOException {
		int id = Integer.parseInt(idString); // Try catch needed
		User item = datastore.findUserById(id);
		if (item == null) {
			resp.getWriter().write("no such user");			
		}
		else {
			resp.getWriter().write(gson.toJson(item));
		}
	}

}
