package deserializer;

import java.lang.reflect.Type;



import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import data.User;

/**
 * User Json Deserializer. <br>
 * Constructs a User with username and hashed password.<br>
 * 
 * @author TKasekamp
 *
 */
public class UserLoginDeserializer implements JsonDeserializer<User> {
  @Override
  public User deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
      throws JsonParseException {
    final JsonObject jsonObject = json.getAsJsonObject();

    final String username = jsonObject.get("username").getAsString();
    final String password = jsonObject.get("password").getAsString();
    String email = null;
	try {
		email = jsonObject.get("email").getAsString();
	} catch (NullPointerException e) {
	}
    User user = new User();
    user.setName(username);
    user.setPassword(password);
    user.setEmail(email);
    return user;
  }
}