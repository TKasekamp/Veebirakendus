package com.codepump.deserializer;

import java.lang.reflect.Type;

import com.codepump.data.CodeItem;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

/**
 * CodeItem Json Deserializer. <br>
 * Constructs a CodeItem. This class may be unnecessary.
 * 
 * @author TKasekamp
 * 
 */
public class CodeItemDeserializer implements JsonDeserializer<CodeItem> {
	@Override
	public CodeItem deserialize(final JsonElement json, final Type typeOfT,
			final JsonDeserializationContext context) throws JsonParseException {
		final JsonObject jsonObject = json.getAsJsonObject();

		final String name = jsonObject.get("name").getAsString();
		final String text = jsonObject.get("text").getAsString();
		final String language = jsonObject.get("language").getAsString();
		final String privacy = jsonObject.get("privacy").getAsString();
		final CodeItem code = new CodeItem();
		code.setName(name);
		code.setText(text);
		code.setLanguage(language);
		code.setPrivacy(privacy);
		return code;
	}
}