package se.grouprich.projectmanagement.provider.adapter;

import com.google.gson.*;
import se.grouprich.projectmanagement.model.Team;
import se.grouprich.projectmanagement.status.TeamStatus;

import java.lang.reflect.Type;
import java.util.Set;

public final class TeamAdapter implements JsonSerializer<Team>, JsonDeserializer<Team>
{
	@Override
	public JsonElement serialize(Team team, Type type, JsonSerializationContext context)
	{
		final JsonObject json = new JsonObject();
		json.addProperty("id", team.getId());
		json.addProperty("controlId", team.getControlId());
		json.addProperty("name", team.getName());
		json.addProperty("status", String.valueOf(team.getStatus()));

		final Set<Long> userIds = team.getUserIds();
		final JsonArray jsonUserIds = new JsonArray();

		if(userIds != null)
		{
			userIds.forEach(jsonUserIds::add);
		}
		json.add("userIds", jsonUserIds);

		return json;
	}

	@Override
	public Team deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException
	{
		final JsonObject teamJson = json.getAsJsonObject();
		final String name = teamJson.get("name").getAsString();
		final TeamStatus status = TeamStatus.valueOf(teamJson.get("status").getAsString());

		return new Team(null, null, name, status, null);
	}
}
