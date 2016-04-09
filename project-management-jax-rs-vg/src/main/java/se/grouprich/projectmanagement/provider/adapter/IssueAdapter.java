package se.grouprich.projectmanagement.provider.adapter;

import com.google.gson.*;
import se.grouprich.projectmanagement.model.Issue;
import se.grouprich.projectmanagement.model.WorkItem;

import java.lang.reflect.Type;

public final class IssueAdapter implements JsonSerializer<Issue>, JsonDeserializer<Issue>
{
	@Override
	public JsonElement serialize(Issue issue, Type type, JsonSerializationContext context)
	{
		final JsonObject json = new JsonObject();
		json.addProperty("id", issue.getId());
		json.addProperty("controlId", issue.getControlId());
		json.addProperty("description", issue.getDescription());

		final WorkItem workItem = issue.getWorkItem();
		if (workItem != null)
		{
			final JsonElement jsonWorkItem = context.serialize(workItem);
			json.add("workItem", jsonWorkItem);
		}

		return json;
	}

	@Override
	public Issue deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException
	{
		final JsonObject issueJson = json.getAsJsonObject();
		final String description = issueJson.get("description").getAsString();

		return new Issue(null, null, description, null);
	}
}
