package se.grouprich.projectmanagement.provider.adapter;

import com.google.gson.*;
import se.grouprich.projectmanagement.model.User;
import se.grouprich.projectmanagement.model.WorkItem;
import se.grouprich.projectmanagement.status.WorkItemStatus;

import java.lang.reflect.Type;

public class WorkItemAdapter implements JsonSerializer<WorkItem>, JsonDeserializer<WorkItem>
{
	@Override
	public JsonElement serialize(WorkItem workItem, Type type, JsonSerializationContext context)
	{
		final JsonObject json = new JsonObject();

		json.addProperty("id", workItem.getId());
		json.addProperty("controlId", workItem.getControlId());
		json.addProperty("title", workItem.getTitle());
		json.addProperty("description", workItem.getDescription());
		json.addProperty("status", String.valueOf(workItem.getStatus()));

		User user = workItem.getUser();
		if (user != null)
		{
			JsonElement jsonUser = context.serialize(user);
			json.add("user", jsonUser);
		}

		return json;
	}

	@Override
	public WorkItem deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException
	{
		JsonObject workItemJson = json.getAsJsonObject();
		String title = workItemJson.get("title").getAsString();
		String description = workItemJson.get("description").getAsString();
		WorkItemStatus status = WorkItemStatus.valueOf(workItemJson.get("status").getAsString());

		return new WorkItem(null, null, title, description, status, null);
	}
}
