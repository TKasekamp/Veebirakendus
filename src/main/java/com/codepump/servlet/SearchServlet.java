package com.codepump.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Singleton;

/**
 * This servlet may be unneccesary
 * @author TKasekamp
 *
 */
@Singleton
public class SearchServlet extends HttpServlet {

	private static final long serialVersionUID = -3230289153808445104L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String searchString = req.getParameter("q") != null ? req.getParameter("q").trim() : "";
		String sortField = req.getParameter("sortField") != null ? req.getParameter("sortField").trim() : "relevance";
		int limit = req.getParameter("limit") != null ? Integer.parseInt(req.getParameter("limit")) : 10;
		int firstResult = req.getParameter("firstResult") != null ? Integer.parseInt(req.getParameter("firstResult")) : 0;
		String s = "q=" + searchString + "&sortField=" + sortField + "&limit=" + limit+ "&firstResult=" + firstResult;
		// Redirecting
		if (req.getParameter("nojs").equalsIgnoreCase("true")) {
			resp.sendRedirect("/search.html?" + s + "&nojs=true");
			return;
		} else {
			resp.sendRedirect("/search.html?" + s);
		}

	}

}
