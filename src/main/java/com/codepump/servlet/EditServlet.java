package com.codepump.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.codepump.service.CodeService;
import com.codepump.tempobject.EditContainer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.inject.Inject;
import com.google.inject.Singleton;

//@WebServlet(value = "/edit")
@Singleton
public class EditServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Gson gson;
	private CodeService codeServ;

	@Override
	public void init() throws ServletException {
		super.init();
		gson = new GsonBuilder().create();
	}

	@Inject
	public EditServlet(CodeService codeServ) {
		this.codeServ = codeServ;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			EditContainer item = gson.fromJson(req.getReader(),
					EditContainer.class);
			System.out.println(item);
			codeServ.editCode(item);

		} catch (JsonParseException ex) {
			System.err.println(ex);
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
		}
	}

}
