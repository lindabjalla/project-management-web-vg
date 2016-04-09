package se.grouprich.projectmanagement.provider.adapter;

import com.google.gson.*;
import se.grouprich.projectmanagement.model.Team;
import se.grouprich.projectmanagement.model.User;
import se.grouprich.projectmanagement.status.UserStatus;

import java.lang.reflect.Type;

public class UserAdapter implements JsonSerializer<User>, JsonDeserializer<User>
{
	@Override
	public JsonElement serialize(User user, Type type, JsonSerializationContext context)
	{
		final JsonObject json = new JsonObject();

		json.addProperty("id", user.getId());
		json.addProperty("controlId", user.getControlId());
		json.addProperty("username", user.getUsername());
		json.addProperty("password", user.getPassword());
		json.addProperty("firstName", user.getFirstName());
		json.addProperty("lastName", user.getLastName());
		json.addProperty("status", String.valueOf(user.getStatus()));

		final Team team = user.getTeam();
		if (team != null)
		{
			final JsonElement jsonTeam = context.serialize(team);
			json.add("team", jsonTeam);
		}

		return json;
	}

	@Override
	public User deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException
	{
		final JsonObject userJson = json.getAsJsonObject();
		final String username = userJson.get("username").getAsString();
		final String password = userJson.get("password").getAsString();
		final String firstName = userJson.get("firstName").getAsString();
		final String lastName = userJson.get("lastName").getAsString();
		final UserStatus status = UserStatus.valueOf(userJson.get("status").getAsString());

		return new User(null, null, username, password, firstName, lastName, status, null);
	}
}
