package com.codepump.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Singleton;

@Singleton
public class SearchServlet extends HttpServlet {

	private static final long serialVersionUID = -3230289153808445104L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		try {
			String query = req.getParameter("query");

			// default parameters
			int limit = 10;
			int offset = 0;
			try {
				limit = Integer.parseInt(req.getParameter("limit"));
				offset = Integer.parseInt(req.getParameter("offset"));
			} catch (Exception e) {
			}
			String s = "query=" + query + "&limit=" + limit + "&offset="
					+ offset;
			System.out.println(s);
			// Redirecting
			if (req.getParameter("nojs").equalsIgnoreCase("true")) {
				resp.sendRedirect("/search.html?" + s + "&nojs=true");
				return;
			} else {
				resp.sendRedirect("/search.html?" + s);
			}
		} catch (Exception e) {
		}
	}

}
