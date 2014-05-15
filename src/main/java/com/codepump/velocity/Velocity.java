package com.codepump.velocity;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.codepump.controller.CacheController;
import com.codepump.data.CodeItem;
import com.codepump.data.User;
import com.codepump.service.CodeService;
import com.codepump.service.SearchService;
import com.codepump.service.UserService;
import com.codepump.tempobject.MyStuffListItem;
import com.codepump.tempobject.ResultContainer;
import com.codepump.tempobject.UserStatisticsItem;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

// process all requested html files with velocity templating engine
// https://velocity.apache.org/engine/releases/velocity-1.7
// https://velocity.apache.org/engine/releases/velocity-1.7/user-guide.html
@SuppressWarnings("serial")
// @WebServlet(value = "*.html")
@Singleton
public class Velocity extends HttpServlet {

	private VelocityEngine engine;
	private UserService userServ;
	private CodeService codeServ;
	private SearchService searchServ;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		engine = createTemplateEngine(config.getServletContext());
//		CacheController.updateCacheManifest();
	}

	@Inject
	public Velocity(UserService userServ, CodeService codeServ,
			SearchService searchServ) {
		this.userServ = userServ;
		this.codeServ = codeServ;
		this.searchServ = searchServ;
	}

	private VelocityEngine createTemplateEngine(ServletContext context) {
		// velocity must know where /src/main/webapp is deployed
		// details in the developer guide (Configuring resource loaders)
		String templatePath = context.getRealPath("/");

		VelocityEngine engine = new VelocityEngine();
		engine.addProperty("file.resource.loader.path", templatePath);
		engine.addProperty("output.encoding", "UTF-8");
		engine.addProperty("input.encoding", "UTF-8");
		engine.init();
		return engine;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html; charset=UTF-8");

		// add additional data to the context here
		// it can then be used inside the templates
		getTemplate(req).merge(getContext(req), resp.getWriter());

	}

	private Template getTemplate(HttpServletRequest req) {
		return engine.getTemplate(req.getRequestURI());
	}

	/**
	 * Implementation of routing rules. Returns context that includes the items
	 * necessary for this request.
	 * 
	 * @param req
	 *            HttpServletRequest to get URI
	 */
	private VelocityContext getContext(HttpServletRequest req) {
		// Creating context
		VelocityContext context = new VelocityContext();
		String uri = req.getRequestURI();

		// User timezone
		String timeZone = getCookies(req, "codepump_timezone");
		if (timeZone == null) {
			timeZone = "Europe/Helsinki"; // Cause it's home
		}

		// Checking if there is a user logged in
		String SID = getCookies(req, "SID");
		boolean haveUser = false;
		User user = userServ.findUserBySID(SID);
		if (user != null) {
			haveUser = true;
			context.put("user", user);
		}

		// NOJS
		boolean nojs = false;
		if (req.getParameter("nojs") != null) {
			nojs = true;
		}

		boolean localDatabase = false;
		if (haveUser) {
			try {
				if (userServ.findUserById(5).getName().equals("a")) {
					localDatabase = true;
				}
			} catch (NullPointerException e) {
			}
		}

		// Routing
		if (uri.equals("/browse.html")) {
			handleBrowse(req, context);
		} else if (uri.equals("/login.html")) {
			handleLogin(req, context);
		} else if (uri.equals("/mystuff.html")) {
			handleMyStuff(req, context, haveUser, SID);
		} else if (uri.equals("/search.html")) {
			handleSearch(req, context, user);
		} else if (uri.equals("/signup.html")) {
			handleSignup(req, context);
		} else if (uri.equals("/source.html")) {
			handleSource(req, context, user);
		} else if (uri.equals("/index.html")) {
			handleIndex(req, context);
		}

		context.put("timeZone", timeZone);
		context.put("localDB", localDatabase);
		context.put("nojs", nojs);
		context.put("haveUser", haveUser);
		return context;
	}

	private String getCookies(HttpServletRequest req, String cookieName) {
		String SID = null;
		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(cookieName)) {
					SID = cookie.getValue();
					break;
				}
			}
		}
		return SID;
	}

	private void handleBrowse(HttpServletRequest req, VelocityContext context) {
		// default parameters
		int limit = req.getParameter("limit") != null ? Integer.parseInt(req.getParameter("limit")) : 20;
		int firstResult = req.getParameter("firstResult") != null ? Integer.parseInt(req.getParameter("firstResult")) : 0;
		
		ResultContainer<CodeItem> codeList = codeServ.getAllCodeItems(firstResult, limit);
		
		context.put("codeList", codeList.getCodeList());
		context.put("recentList", codeServ.getRecentItems());
		
		context.put("firstResult", firstResult);
		context.put("nextFirstResult", firstResult +limit);		
		context.put("previousFirstResult", firstResult -limit);		
		context.put("last", firstResult+limit < codeList.getResultSize() ? firstResult+limit : codeList.getResultSize());
		context.put("resultSize", codeList.getResultSize());
		context.put("limit", limit);
		System.out.println(codeList.getResultSize());

	}

	private void handleSource(HttpServletRequest req, VelocityContext context,
			User user) {
		String idString = req.getParameter("id");
		boolean canChange = false;
		try {
			int id = Integer.parseInt(idString);
			CodeItem item = codeServ.findItemById(id);
			// Setting to lower case here
			context.put("language", item.getLanguage().toLowerCase());
			context.put("code", item);
			// For edit button
			if ((user != null) && (item.getUser().getId() == user.getId())) {
				canChange = true;
			}

		} catch (Exception e) {
			// If here then no such code was found in DB.
			// No languages will be loaded
			// Creating empty code to display
			CodeItem item = new CodeItem();
			item.setName("Nothing here");
			item.setText("");
			context.put("code", item);
		}
		context.put("canChange", canChange);
	}

	private void handleMyStuff(HttpServletRequest req, VelocityContext context,
			boolean haveUser, String SID) {
		if (haveUser) {
			// default parameters
			int limit = req.getParameter("limit") != null ? Integer.parseInt(req.getParameter("limit")) : 20;
			int firstResult = req.getParameter("firstResult") != null ? Integer.parseInt(req.getParameter("firstResult")) : 0;
			
			UserStatisticsItem stat = userServ.generateUserStatistics(SID);
			ResultContainer<MyStuffListItem> allContent = codeServ.getAllUserItems(SID, firstResult, limit);
			System.out.println(allContent.getResultSize());
			context.put("stat", stat);
			context.put("myStuffList", allContent.getCodeList());
			
			context.put("firstResult", firstResult);
			context.put("nextFirstResult", firstResult +limit);		
			context.put("previousFirstResult", firstResult -limit);		
			context.put("last", firstResult+limit < allContent.getResultSize() ? firstResult+limit : allContent.getResultSize());
			context.put("resultSize", allContent.getResultSize());
			context.put("limit", limit);

		}

		context.put("recentList", codeServ.getRecentItems());
	}

	private void handleLogin(HttpServletRequest req, VelocityContext context) {
		int result = -1;
		try {
			String r = req.getParameter("result");

			if (r.equals("nouser")) {
				result = 0;
			} else if (r.equals("wrongpass")) {
				result = 2;
			}
		} catch (Exception e) {
		}
		context.put("result", result);
	}

	private void handleSignup(HttpServletRequest req, VelocityContext context) {
		int result = 0;
		try {
			String r = req.getParameter("result");

			if (r.equals("pass")) {
				result = 1;
			} else if (r.equals("userexists")) {
				result = 2;
			} else if (r.equals("badname")) {
				result = 3;
			} else if (r.equals("bademail")) {
				result = 4;
			} else if (r.equals("badpassword")) {
				result = 5;
			}
		} catch (Exception e) {
			
		}
		context.put("result", result);
	}

	private void handleSearch(HttpServletRequest req, VelocityContext context,
			User user) {
		ResultContainer<CodeItem> dataset;
		
		// default parameters
		String searchString = req.getParameter("q") != null ? req.getParameter("q").trim() : "";
		String sortField = req.getParameter("sortField") != null ? req.getParameter("sortField").trim() : "relevance";
		int limit = req.getParameter("limit") != null ? Integer.parseInt(req.getParameter("limit")) : 0;
		int firstResult = req.getParameter("firstResult") != null ? Integer.parseInt(req.getParameter("firstResult")) : 0;

		dataset = searchServ.searchDatabaseFuzzy(searchString, limit, firstResult, sortField);
		
		context.put("results", dataset.getCodeList());
		context.put("recentList", codeServ.getRecentItems());	
		
		//Search stuff
		context.put("firstResult", firstResult);
		context.put("nextFirstResult", firstResult +limit);		
		context.put("previousFirstResult", firstResult -limit);		
		context.put("last", firstResult+limit < dataset.getResultSize() ? firstResult+limit : dataset.getResultSize());
		context.put("searchResultSize", dataset.getResultSize());
		context.put("q", searchString);
		context.put("sortField", sortField);
		context.put("limit", limit);
	}
	
	private void handleIndex(HttpServletRequest req, VelocityContext context) {
		String result = "";
		try {
			String r = req.getParameter("result");
			if (r.equals("error"))
				result = "error";
		} catch (Exception e) {
			
		}
		context.put("result", result);
	}

}
