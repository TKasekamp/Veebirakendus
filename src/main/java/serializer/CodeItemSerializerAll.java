package serializer;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import data.CodeItem;

/**
 * Returns a Json object with the id and name of a CodeItem object. Used to
 * create a list of all items in source.html
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