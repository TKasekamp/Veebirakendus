package com.codepump.deserializer;

import java.lang.reflect.Type;

import com.codepump.controller.ServerController;
import com.codepump.data.CodeItem;
import com.codepump.data.User;
import com.codepump.service.AuthenicationService;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

/**
 * CodeItem Json Deserializer. <br>
 * Constructs a CodeItem <br>
 * Used to match a SID to an user ID. If no user is found the ID will be set to
 * -1.
 * 
 * @author TKasekamp
 * 
 */
@Deprecated
public class CodeItemDeserializer implements JsonDeserializer<CodeItem> {
	private static AuthenicationService authServ = ServerController.authenticationServer;

	@Override
	public CodeItem deserialize(final JsonElement json, final Type typeOfT,
			final JsonDeserializationContext context) throws JsonParseException {
		final JsonObject jsonObject = json.getAsJsonObject();

		final String name = jsonObject.get("name").getAsString();
		final String text = jsonObject.get("text").getAsString();
		final String language = jsonObject.get("language").getAsString();
		final String privacy = jsonObject.get("privacy").getAsString();
		final String SID = jsonObject.get("SID").getAsString();

		int id = -1;
		if (SID.length() == 32) {
			id = authServ.getUserWithSID(SID);
		}

		final CodeItem code = new CodeItem();
		code.setName(name);
		code.setText(text);
		code.setLanguage(language);
		code.setPrivacy(privacy);
		User u = new User();
		u.setId(id);
		code.setUser(u);
		return code;
	}
}