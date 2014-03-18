package deserializer;

import java.lang.reflect.Type;

import servlets.PumpController;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import data.CodeItem;
import datastore.UserDataProvider;

/**
 * CodeItem Json Deserializer. <br>
 * Constructs a CodeItem <br>
 * Frontend will also send a SID and that needs to matched with a userID.
 * 
 * @author TKasekamp
 *
 */
public class CodeItemDeserializer implements JsonDeserializer<CodeItem> {
	private UserDataProvider userstore = PumpController.userstore;
  @Override
  public CodeItem deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
      throws JsonParseException {
    final JsonObject jsonObject = json.getAsJsonObject();

    final String name = jsonObject.get("name").getAsString();
    final String text = jsonObject.get("text").getAsString();
    final String language = jsonObject.get("language").getAsString();
    final String privacy = jsonObject.get("privacy").getAsString();
    final String SID = jsonObject.get("SID").getAsString();
    
    int id = -1;
    if(SID.length() == 32) {
    	id = userstore.getUserWithSID(SID);
    }

    final CodeItem code = new CodeItem();
    code.setName(name);
    code.setText(text);
    code.setLanguage(language);
    code.setPrivacy(privacy);
    code.setUserId(id);
    return code;
  }
}