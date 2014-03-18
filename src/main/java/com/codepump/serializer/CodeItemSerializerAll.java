package com.codepump.serializer;

import java.lang.reflect.Type;

import com.codepump.data.CodeItem;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Returns a Json object with the id and name of a CodeItem object. Used to
 * populate the header list.
 * 
 * @author TKasekamp
 * 
 */
public class CodeItemSerializerAll implements JsonSerializer<CodeItem> {
	public JsonElement serialize(CodeItem src, Type typeOfSrc,
			JsonSerializationContext context) {
		JsonObject obj = new JsonObject();
		obj.addProperty("id", src.getId());
		obj.addProperty("name", src.getName());
		return obj;
	}

}