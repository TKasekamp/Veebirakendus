package com.codepump.serializer;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.codepump.tempobject.RecentItem;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Returns a Json object with the id and name of a RecentItem object. Used to
 * populate the recent items list.
 * 
 * @author TKasekamp
 * 
 */
public class RecentItemSerializer implements JsonSerializer<RecentItem> {
	
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat(
			"HH:mm:ss dd.MM.yyyy");
	public JsonElement serialize(RecentItem src, Type typeOfSrc,
			JsonSerializationContext context) {
		JsonObject obj = new JsonObject();
		obj.addProperty("codeID", src.getCodeId());
		obj.addProperty("codeName", src.getCodeName());
		obj.addProperty("codeLangauge", src.getCodeLanguage());
		obj.addProperty("createDate", DATE_FORMAT.format(src.getCreateDate()));
		obj.addProperty("userID", src.getUserID());
		obj.addProperty("userName", src.getUserName());
		return obj;
	}

}