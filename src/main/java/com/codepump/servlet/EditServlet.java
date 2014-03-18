package com.codepump.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.codepump.data.CodeItem;
import com.codepump.service.CodeService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

@WebServlet(value = "/edit")
public class EditServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Gson gson;
	public CodeService datastore;

	@Override
	public void init() throws ServletException {
		super.init();
		gson = new GsonBuilder().create();
		this.datastore = DataServlet.codeServ;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			// TODO authentication
			CodeItem item = gson.fromJson(req.getReader(), CodeItem.class);
			System.out.println(item);
			datastore.editCode(item);
			System.out.println("Edit success");

		} catch (JsonParseException ex) {
			System.err.println(ex);
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
		}
	}

}
