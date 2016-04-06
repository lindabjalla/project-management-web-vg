package se.grouprich.projectmanagement.provider;

import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import se.grouprich.projectmanagement.model.Issue;
import se.grouprich.projectmanagement.model.WorkItem;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class IssueProvider implements MessageBodyWriter<Issue>, MessageBodyReader<Issue>
{
	private static final Gson gson = new GsonBuilder().registerTypeAdapter(Issue.class, new IssueAdapter()).create();

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		return type.isAssignableFrom(Issue.class);
	}

	@Override
	public long getSize(Issue issue, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		return 0;
	}

	@Override
	public void writeTo(Issue issue, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException
	{
		try (JsonWriter writer = new JsonWriter(new OutputStreamWriter(entityStream)))
		{
			gson.toJson(issue, Issue.class, writer);
		}
	}

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		return type.isAssignableFrom(Issue.class);
	}

	@Override
	public Issue readFrom(Class<Issue> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
			InputStream entityStream) throws IOException, WebApplicationException
	{
		return gson.fromJson(new InputStreamReader(entityStream), Issue.class);

	}

	private static final class IssueAdapter implements JsonSerializer<Issue>, JsonDeserializer<Issue>
	{
		@Override
		public JsonElement serialize(Issue issue, Type type, JsonSerializationContext context)
		{
			final JsonObject json = new JsonObject();
			json.addProperty("id", issue.getId());
			json.addProperty("controlId", issue.getControlId());
			json.addProperty("description", issue.getDescription());

			if (issue.getWorkItem() != null)
			{
				final JsonElement jsonWorkItem = context.serialize(issue.getWorkItem());
				json.add("workItem", jsonWorkItem);
			}

			return json;
		}

		@Override
		public Issue deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException
		{
			final JsonObject issueJson = json.getAsJsonObject();
			final Long id = issueJson.get("id").getAsLong();
			final String controlId = issueJson.get("controlId").getAsString();
			final String description = issueJson.get("description").getAsString();

			final JsonElement workItemJson = issueJson.getAsJsonObject("workItem");
			final WorkItem workItem = context.deserialize(workItemJson, WorkItem.class);

			return new Issue(id, controlId, description, workItem);
		}
	}
}
