package se.grouprich.projectmanagement.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status;

public class InvalidStatusException extends WebApplicationException
{
	private static final long serialVersionUID = 1L;

	public InvalidStatusException(String invalidStatusName)
	{
		super(Response.status(Status.BAD_REQUEST).entity("Status: " + invalidStatusName + " is invalid").build());
	}
}
