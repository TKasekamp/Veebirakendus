package com.codepump.serializer;

import java.lang.reflect.Type;

import com.codepump.tempobject.UserLanguageStatisticsItem;
import com.codepump.tempobject.UserStatisticsItem;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Serializes {@link UserStatisticsItem}. Uses
 * {@link UserLanguageStatisticsSerializer} to serialize
 * {@link UserLanguageStatisticsItem}
 * 
 * @author TKasekamp
 * 
 */
public class UserStatisticsSerializer implements
		JsonSerializer<UserStatisticsItem> {

	@Override
	public JsonElement serialize(UserStatisticsItem arg0, Type arg1,
			JsonSerializationContext context) {
		JsonObject obj = new JsonObject();
		obj.addProperty("userID", arg0.getUserID());
		obj.addProperty("userName", arg0.getUsername());
		obj.addProperty("createdItems", arg0.getCreatedItems());
		final JsonElement jsonLangauage = context.serialize(arg0
				.getLanguageStatistics());
		obj.add("languageStatistics", jsonLangauage);
		return obj;
	}

}
