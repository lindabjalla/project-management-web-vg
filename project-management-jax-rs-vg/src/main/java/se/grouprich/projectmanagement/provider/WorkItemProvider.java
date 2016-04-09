package se.grouprich.projectmanagement.provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import se.grouprich.projectmanagement.model.WorkItem;
import se.grouprich.projectmanagement.provider.adapter.WorkItemAdapter;

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
public class WorkItemProvider implements MessageBodyWriter<WorkItem>, MessageBodyReader<WorkItem>
{
	private static final Gson gson = new GsonBuilder().registerTypeAdapter(WorkItem.class, new WorkItemAdapter()).create();

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		return type.isAssignableFrom(WorkItem.class);
	}

	@Override
	public long getSize(WorkItem workItem, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		return 0;
	}

	@Override
	public void writeTo(WorkItem workItem, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException
	{
		try (JsonWriter writer = new JsonWriter(new OutputStreamWriter(entityStream, "UTF-8")))
		{
			gson.toJson(workItem, WorkItem.class, writer);
		}
	}

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		return type.isAssignableFrom(WorkItem.class);
	}

	@Override
	public WorkItem readFrom(Class<WorkItem> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException
	{
		return gson.fromJson(new InputStreamReader(entityStream, "UTF-8"), WorkItem.class);
	}
}
