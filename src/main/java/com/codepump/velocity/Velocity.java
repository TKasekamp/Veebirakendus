package com.codepump.velocity;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.codepump.data.CodeItem;
import com.codepump.data.User;
import com.codepump.service.CodeService;
import com.codepump.service.UserService;
import com.codepump.tempobject.MyStuffListItem;
import com.codepump.tempobject.SearchItem;
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
import java.util.List;

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

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		engine = createTemplateEngine(config.getServletContext());
	}

	@Inject
	public Velocity(UserService userServ, CodeService codeServ) {
		this.userServ = userServ;
		this.codeServ = codeServ;
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

		// Checking if there is a user logged in
		String SID = getCookies(req);
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
			context.put("codeList", codeServ.getAllCodeItems());
			context.put("recentList", codeServ.getRecentItems());
		}

		else if (uri.equals("/source.html")) {
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

		else if (uri.equals("/mystuff.html")) {
			if (haveUser) {
				UserStatisticsItem stat = userServ.generateUserStatistics(SID);
				List<MyStuffListItem> allContent = codeServ
						.getAllUserItems(SID);
				context.put("stat", stat);
				context.put("myStuffList", allContent);

			}

			context.put("recentList", codeServ.getRecentItems());
		} else if (uri.equals("/signup.html")) {
			int result = 0;
			try {
				String r = req.getParameter("result");

				if (r.equals("pass")) {
					result = 1;
				} else if (r.equals("userexists")) {
					result = 2;
				}
			} catch (Exception e) {
			}
			context.put("result", result);
		} else if (uri.equals("/login.html")) {
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
		} else if (uri.equals("/search.html")) {

			try {
				String query = req.getParameter("query");
				// default parameters
				int limit = 10;
				int offset = 0;
				try {
					limit = Integer.parseInt(req.getParameter("limit"));
					offset = Integer.parseInt(req.getParameter("offset"));					
				}
				catch (Exception e) {
				}
				List<SearchItem> dataset = codeServ.searchCode(query, limit,
						offset);
				System.out.println(dataset);
				context.put("results", dataset);
			} catch (Exception e) {
			}

		}
		
		context.put("localDB", localDatabase);
		context.put("nojs", nojs);
		context.put("haveUser", haveUser);
		return context;
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
