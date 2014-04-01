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
 * <b>NB!</b> User password is hashed here.
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
		final String password = jsonObject.get("password").getAsString();
		String username = null;
		try {
			username = jsonObject.get("username").getAsString();
		} catch (NullPointerException e) {
		}
		User user = new User();
		user.setName(username);
		user.setPassword(password);
		user.setEmail(email);
		user.hashPassword();
		return user;
	}
}