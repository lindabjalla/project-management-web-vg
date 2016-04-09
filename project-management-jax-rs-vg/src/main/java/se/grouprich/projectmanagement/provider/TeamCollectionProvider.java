package se.grouprich.projectmanagement.provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import se.grouprich.projectmanagement.model.Team;
import se.grouprich.projectmanagement.provider.adapter.TeamAdapter;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public final class TeamCollectionProvider implements MessageBodyWriter<Collection<Team>>
{
	private static final Gson gson = new GsonBuilder().registerTypeAdapter(Team.class, new TeamAdapter()).create();

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		boolean isWritable;
		if (Collection.class.isAssignableFrom(type) && genericType instanceof ParameterizedType)
		{
			ParameterizedType parameterizedType = (ParameterizedType) genericType;
			Type[] actualTypeArgs = (parameterizedType.getActualTypeArguments());
			isWritable = (actualTypeArgs.length == 1 && actualTypeArgs[0].equals(Team.class));
		}
		else
		{
			isWritable = false;
		}

		return isWritable;
	}

	@Override
	public long getSize(Collection<Team> teams, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		return 0;
	}

	@Override
	public void writeTo(Collection<Team> teams, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException
	{
		try (JsonWriter writer = new JsonWriter(new OutputStreamWriter(entityStream, "UTF-8")))
		{
			gson.toJson(teams, Collection.class, writer);
		}
	}
}
