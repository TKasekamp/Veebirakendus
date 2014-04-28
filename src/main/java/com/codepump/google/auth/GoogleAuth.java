package com.codepump.google.auth;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.codepump.data.User;
import com.codepump.deserializer.UserDeserializer;
import com.codepump.response.AuthenticationResponse;
import com.codepump.service.AuthenicationService;
import com.codepump.service.DatabaseService;
import com.codepump.service.impl.AuthenticationServiceImpl;
import com.codepump.service.impl.DatabaseServiceImpl;

public final class GoogleAuth {
	private Gson gson;
	private DatabaseService dbServ = new DatabaseServiceImpl();
	private AuthenicationService authServ = new AuthenticationServiceImpl(dbServ);
	private String SID;

	private static final String CLIENT_ID = "570680235799-mq2l1luj94oqc20ahl8uh0a9evi3d8be.apps.googleusercontent.com";
	private static final String CLIENT_SECRET = "lMwtpgOnXm_XprLMijtA0pCy";
	private static String CALLBACK_URI;

	// start google authentication constants
	private static final Collection<String> SCOPE = Arrays.asList("https://www.googleapis.com/auth/userinfo.profile;https://www.googleapis.com/auth/userinfo.email".split(";"));
	private static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo";
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	// end google authentication constants

	private String stateToken;

	private final GoogleAuthorizationCodeFlow flow;

	public GoogleAuth() {
		flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT,
				JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, SCOPE).build();
		generateStateToken();
		
		try {
			String databaseUrl = System.getenv("DATABASE_URL");
    		if (databaseUrl != null) {
         		CALLBACK_URI = "http://codepump2.herokuapp.com/GoogleLogin.jsp";
    		} else {
    			CALLBACK_URI = "http://localhost:8080/GoogleLogin.jsp";
    		}
		} catch (Exception ex) {
			System.err.println(ex);
		}
	}
	
	public void buildGson() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(User.class, new UserDeserializer());
		gson = gsonBuilder.create();
	}

	public String buildLoginUrl() {
		final GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl();
		return url.setRedirectUri(CALLBACK_URI).setState(stateToken).build();
	}

	private void generateStateToken(){
		SecureRandom sr1 = new SecureRandom();
		stateToken = "google;"+sr1.nextInt();

	}

	public String getStateToken(){
		return stateToken;
	}
	
	public String getSID(){
		return this.SID;
	}

	/**
	 * Expects an Authentication Code, and makes an authenticated request for the user's profile information
	 * @return JSON formatted user profile information
	 * @param authCode authentication code provided by google
	 */
	public String getUserInfoJson(final String authCode) throws IOException {

		final GoogleTokenResponse response = flow.newTokenRequest(authCode).setRedirectUri(CALLBACK_URI).execute();
		final Credential credential = flow.createAndStoreCredential(response, null);
		final HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(credential);
		// Make an authenticated request
		final GenericUrl url = new GenericUrl(USER_INFO_URL);
		final HttpRequest request = requestFactory.buildGetRequest(url);
		request.getHeaders().setContentType("application/json");
		final String jsonIdentity = request.execute().parseAsString();

		return jsonIdentity;

	}
	
	public void login(final String authCode) throws IOException {
		String jsonIdentity = getUserInfoJson(authCode);
		try {
			buildGson();
			User user = gson.fromJson(jsonIdentity, User.class);
			AuthenticationResponse r = authServ.checkPassword(user);
			if (r.getResponse() == 0)
				user.generateRandomPassword();
			String sid = authServ.googleLogin(user);
			this.SID = sid;

		} catch (JsonParseException ex) {
			System.err.println(ex);
		}

	}
	
	public void createSIDCookie(HttpServletResponse resp) {
		Cookie cookie = new Cookie("SID", this.getSID());
		cookie.setMaxAge(2 * 60 * 60); // 2 h
		cookie.setPath("/");
		resp.addCookie(cookie);
	}

}