package se.grouprich.projectmanagement.exception.mapper;

import se.grouprich.projectmanagement.exception.RepositoryException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.Status;

@Provider
public final class RepositoryExceptionMapper implements ExceptionMapper<RepositoryException>
{
	@Override
	public Response toResponse(RepositoryException exception)
	{
		return Response.status(Status.NOT_FOUND).entity(exception.getMessage()).build();
	}
}