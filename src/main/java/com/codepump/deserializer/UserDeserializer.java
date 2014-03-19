package com.codepump.deserializer;

import java.lang.reflect.Type;

import com.codepump.data.User;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

/**
 * User Json Deserializer. <br>
 * Constructs a User object with username, password and email. If no email is
 * provided sets it to <code>null</code>. Used in user registration and user
 * login.
 * 
 * @author TKasekamp
 * 
 */
public class UserDeserializer implements JsonDeserializer<User> {
	@Override
	public User deserialize(final JsonElement json, final Type typeOfT,
			final JsonDeserializationContext context) throws JsonParseException {
		final JsonObject jsonObject = json.getAsJsonObject();

		final String username = jsonObject.get("username").getAsString();
		final String password = jsonObject.get("password").getAsString();
		String email = null;
		try {
			email = jsonObject.get("email").getAsString();
		} catch (NullPointerException e) {
		}
		User user = new User();
		user.setName(username);
		user.setPassword(password);
		user.setEmail(email);
		return user;
	}
}