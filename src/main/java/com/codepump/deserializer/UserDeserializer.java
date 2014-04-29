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
 * Constructs a User object with username, password and email. If no username is
 * provided sets it to <code>null</code>. Used in user registration and user
 * login.<br>
 * 
 * 
 * @author TKasekamp
 * 
 */
public class UserDeserializer implements JsonDeserializer<User> {
	@Override
	public User deserialize(final JsonElement json, final Type typeOfT,
			final JsonDeserializationContext context) throws JsonParseException {
		final JsonObject jsonObject = json.getAsJsonObject();

		final String email = jsonObject.get("email").getAsString();
		String password = null;
		String username = null;
		
		if (jsonObject.has("password")) {
			password = jsonObject.get("password").getAsString();
		} else if (jsonObject.has("name")) {
			username = jsonObject.get("name").getAsString();
		} else {
			throw new IllegalArgumentException("Invalid arguments in UserDeserializer class.");
		}
		
		User user = new User(username, email, password);
		return user;
	}
}