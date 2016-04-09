package se.grouprich.projectmanagement.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class EmptyParameterException extends WebApplicationException
{
	private static final long serialVersionUID = 1L;

	public EmptyParameterException(String paramName)
	{
		super(Response.status(Response.Status.BAD_REQUEST).entity(paramName + " can not be empty").build());
	}
}
