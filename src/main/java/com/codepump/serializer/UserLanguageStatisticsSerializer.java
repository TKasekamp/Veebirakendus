package com.codepump.serializer;

import java.lang.reflect.Type;

import com.codepump.tempobject.UserLanguageStatisticsItem;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Returns a Json object with only languge and count of a
 * {@link UserLanguageStatisticsItem}.
 * 
 * @author TKasekamp
 * 
 */
public class UserLanguageStatisticsSerializer implements
		JsonSerializer<UserLanguageStatisticsItem> {

	@Override
	public JsonElement serialize(UserLanguageStatisticsItem arg0, Type arg1,
			JsonSerializationContext arg2) {
		JsonObject obj = new JsonObject();
		obj.addProperty("language", arg0.getLanguage());
		obj.addProperty("count", arg0.getCreatedItems());
		return obj;
	}

}
