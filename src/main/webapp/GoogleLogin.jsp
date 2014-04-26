<%@page import="com.codepump.google.auth.GoogleAuth"%>
<%@page import="javax.servlet.http.Cookie"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Google Login</title>
</head>
<body>
	<div>
		<%
			final GoogleAuth gAuth = new GoogleAuth();
			String originalUrl = request.getQueryString().replace("from=","");;
			
			if (request.getParameter("code") == null
					|| request.getParameter("state") == null) {

				session.setAttribute("state", gAuth.getStateToken());
				response.sendRedirect(gAuth.buildLoginUrl());

			} else if (request.getParameter("code") != null && request.getParameter("state") != null
					&& request.getParameter("state").equals(session.getAttribute("state"))) {

				session.removeAttribute("state");
				gAuth.login(request.getParameter("code"));
				String SID = gAuth.getSID();
				Cookie cookie = new Cookie("SID", SID);
				cookie.setMaxAge(2 * 60 * 60); // 2 h
				cookie.setPath("/");
				response.addCookie(cookie);
				try {
					String databaseUrl = System.getenv("DATABASE_URL");
	        		if (databaseUrl != null) {
	             		response.sendRedirect("http://codepump2.herokuapp.com"+originalUrl);
	        		} else {
	             		response.sendRedirect("http://localhost:8080"+originalUrl);
	        		}
				} catch (Exception ex) {
					System.err.println(ex);
				}
			}
		%>
	</div>
</body>
</html>