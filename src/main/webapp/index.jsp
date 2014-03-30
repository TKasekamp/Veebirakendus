<%@page import="com.codepump.google.auth.GoogleAuthHelper"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Google OAuth 2.0 v1 Demo</title>

</head>
<body>
	<div class="oauthDemo">
		<%
			final GoogleAuthHelper helper = new GoogleAuthHelper();
		%>
	</div>
	<br />
	<div class="readme">
		This works.
	</div>
</body>
</html>