package servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import serializer.CodeItemSerializerAll;
import serializer.CodeItemSerializerNormal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import data.CodeItem;
import datastore.CodeDataProvider;
import datastore.MemoryStore;

//@WebServlet(value = "/source.html")
public class SourceController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Gson gson;
	private Gson gson2; // For replyWithAll
	private CodeDataProvider datastore;

	@Override
	public void init() throws ServletException {
		super.init();
		gson = new GsonBuilder().registerTypeAdapter(CodeItem.class, new CodeItemSerializerNormal()).create();
		gson2 = new GsonBuilder().
			    registerTypeAdapter(CodeItem.class, new CodeItemSerializerAll()).create();
		datastore = PumpController.datastore;
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
			CodeItem item = gson.fromJson(req.getReader(), CodeItem.class);
			datastore.addCode(item); // bid should be validated carefully
			System.out.println("Adding codeitem");
			System.out.println(item);
			resp.setHeader("Content-Type", "application/json");
			resp.getWriter().write(gson.toJson(item));
		} catch (JsonParseException ex) {
			System.err.println(ex);
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
		}
	}

	private void replyWithAllItems(HttpServletResponse resp) throws IOException {
		List<CodeItem> allContent = datastore.findAllItems();
		// Costum serializer http://stackoverflow.com/questions/8572568/how-to-serialize-such-a-custom-type-to-json-with-google-gson
		resp.getWriter().write(gson2.toJson(allContent));
		System.out.println("all items");
		System.out.println(allContent);
	}

	private void replyWithSingleItem(HttpServletResponse resp, String idString)
			throws IOException {
		int id = Integer.parseInt(idString); // Try catch needed
		CodeItem item = datastore.findItemById(id);
		if (item == null) {
			resp.getWriter().write("sorry, there isn't such a file yet");			
		}
		else {
			resp.getWriter().write(gson.toJson(item));
		}
	}

}
