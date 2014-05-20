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
			final GoogleAuth gAuth = GoogleAuth.getInstance();
			
			if (request.getParameter("code") == null
					|| request.getParameter("state") == null) {

				session.setAttribute("state", gAuth.getStateToken());
				response.sendRedirect(gAuth.buildLoginUrl());
				return;

			} else if (request.getParameter("code") != null && request.getParameter("state") != null
					&& request.getParameter("state").equals(session.getAttribute("state"))) {

				session.removeAttribute("state");
				gAuth.login(request.getParameter("code"));
				gAuth.createSIDCookie(response);
				
				try {
					String databaseUrl = System.getenv("DATABASE_URL");
	        		if (databaseUrl != null) {
	             		response.sendRedirect("http://codepump2.herokuapp.com/return.html");
	        		} else {
	             		response.sendRedirect("http://localhost:8080/return.html");
	        		}
				} catch (Exception ex) {
					System.err.println(ex);
				}
				return;
			}
		%>
	</div>
</body>
</html>