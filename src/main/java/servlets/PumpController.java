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

import data.CodeItem;
import datastore.CodeDataProvider;
import datastore.MemoryStore;

@WebServlet(value = "/data")
public class PumpController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Gson gson;
	private CodeDataProvider datastore;

	@Override
	public void init() throws ServletException {
		super.init();
		gson = new GsonBuilder().setPrettyPrinting().create();
		datastore = new MemoryStore();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setHeader("Content-Type", "application/json");

		String idString = req.getParameter("id");
		if (idString != null) {
			replyWithSingleItem(resp, idString);
			System.out.println(idString);
		} else {
			replyWithAllItems(resp);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			System.out.println("hello");
//			if (req.getAttribute("id").equals("0")) {
				System.out.println("This is a new text");
				CodeItem item = gson.fromJson(req.getReader(), CodeItem.class);
				datastore.addCode(item); 
				System.out.println("Adding codeitem");
				System.out.println(item);

				resp.setHeader("Content-Type", "application/json");
				resp.getWriter().write(gson.toJson(item));
//			}
//			else if (req.getAttribute("id").equals("1")){
//				System.out.println("This is a new User");			
//			}

		} catch (JsonParseException ex) {
			System.err.println(ex);
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
		}
	}

	private void replyWithAllItems(HttpServletResponse resp) throws IOException {
		List<CodeItem> allContent = datastore.findAllItems();
		resp.getWriter().write(gson.toJson(allContent));
		System.out.println("all items");
		System.out.println(allContent);
	}

	private void replyWithSingleItem(HttpServletResponse resp, String idString)
			throws IOException {
		int id = Integer.parseInt(idString);
		CodeItem item = datastore.findItemById(id);
		resp.getWriter().write(gson.toJson(item));
	}

}
