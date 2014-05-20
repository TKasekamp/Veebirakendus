package com.codepump.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
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

/**
 * Handles code deletion and editing.
 * 
 * @author TKasekamp
 * 
 */
// @WebServlet(value = "/edit/*")
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
		String SID = getCookies(req);
		String uri = req.getRequestURI();
		if (uri.equals("/edit/delete")) {
			handleCodeDelete(req, resp);
			// TODO websocket announcment missing. must find better way to link
			// it up
		} else if (uri.equals("/edit/ajax")) {
			handleCodeEdit(req, resp, SID);
		}
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String SID = getCookies(req);
		handleCodeEdit(req, resp, SID);

	}

	private void handleCodeDelete(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		String s = req.getParameter("codeID");

		codeServ.deleteCode(Integer.parseInt(s));
		// Redirecting
		if (req.getParameter("nojs").equalsIgnoreCase("true")) {
			resp.sendRedirect("/mystuff.html?nojs=true");
			return;
		} else {
			resp.sendRedirect("/mystuff.html");
		}

	}

	private void handleCodeEdit(HttpServletRequest req,
			HttpServletResponse resp, String SID) throws ServletException,
			IOException {
		try {
			EditContainer item = gson.fromJson(req.getReader(),
					EditContainer.class);
			item.setSID(SID);
			codeServ.editCode(item);

		} catch (JsonParseException ex) {
			System.err.println(ex);
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
		}
	}

	private String getCookies(HttpServletRequest req) {
		String SID = null;
		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("SID")) {
					SID = cookie.getValue();
					break;
				}
			}
		}
		return SID;
	}

}
