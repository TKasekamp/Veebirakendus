package serializer;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import data.CodeItem;

/**
 * Returns a Json object with the name, text and language of a CodeItem object.
 * Used to populate a specific source.html page.
 * 
 * @author TKasekamp
 * 
 */
public class CodeItemSerializerNormal implements JsonSerializer<CodeItem> {
	public JsonElement serialize(CodeItem src, Type typeOfSrc,
			JsonSerializationContext context) {
		JsonObject obj = new JsonObject();
		obj.addProperty("name", src.getName());
		obj.addProperty("text", src.getText());
		obj.addProperty("language", src.getLanguage());
		return obj;
	}

}