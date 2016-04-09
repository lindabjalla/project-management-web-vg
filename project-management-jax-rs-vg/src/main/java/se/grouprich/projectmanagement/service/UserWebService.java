package se.grouprich.projectmanagement.service;

import com.google.common.collect.Lists;
import se.grouprich.projectmanagement.Loader;
import se.grouprich.projectmanagement.exception.InvalidValueException;
import se.grouprich.projectmanagement.exception.RepositoryException;
import se.grouprich.projectmanagement.model.TeamData;
import se.grouprich.projectmanagement.model.User;
import se.grouprich.projectmanagement.model.UserData;
import se.grouprich.projectmanagement.model.mapper.UserMapper;
import se.grouprich.projectmanagement.status.UserStatus;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.Collection;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public final class UserWebService
{
	private static final UserService userService = Loader.getBean(UserService.class);
	private static final TeamService teamService = Loader.getBean(TeamService.class);
	private static final UserMapper userMapper = new UserMapper();

	@Context
	private UriInfo uriInfo;

	@POST
	public Response createUser(final User user) throws InvalidValueException, RepositoryException
	{
		user.setStatus(UserStatus.ACTIVE);
		final UserData userData = userMapper.convertUserToUserData(user);
		final UserData createdUser = userService.createOrUpdate(userData);
		final URI location = uriInfo.getAbsolutePathBuilder().path(getClass(), "getUser").build(createdUser.getId());

		return Response.created(location).build();
	}

	@GET
	@Path("{id}")
	public Response getUser(@PathParam("id") final Long id) throws RepositoryException
	{
		final UserData userData = userService.findById(id);
		final User user = userMapper.convertUserDataToUser(userData);

		return Response.ok(user).build();
	}

	@PUT
	@Path("{id}")
	public Response updateUser(@PathParam("id") final Long id, User user) throws InvalidValueException, RepositoryException
	{
		final UserData userData = userService.findById(id);
		final UserData updatedUserData = userMapper.updateUserData(user, userData);
		userService.createOrUpdate(updatedUserData);

		return Response.noContent().build();
	}

	@DELETE
	@Path("{id}")
	public Response deleteUser(@PathParam("id") final Long id) throws RepositoryException, InvalidValueException
	{
		userService.deleteById(id);
		return Response.noContent().build();
	}

	@GET
	@Path("control-id/{controlId}")
	public Response getUserByControlId(@PathParam("controlId") final String controlId) throws RepositoryException
	{
		final UserData userData = userService.findByControlId(controlId);
		final User user = userMapper.convertUserDataToUser(userData);

		return Response.ok(user).build();
	}

	@GET
	@Path("search")
	public Response searchUsersByFirstNameOrLastNameOrUsername(@QueryParam("first-name") final String firstName, @QueryParam("last-name") String lastName,
			@QueryParam("username") String username) throws RepositoryException
	{
		final Collection<UserData> userDataList = userService.searchUsersByFirstNameOrLastNameOrUsername(firstName, lastName, username);
		final GenericEntity<Collection<User>> users = userMapper.convertList(userDataList);

		return Response.ok(users).build();
	}

	@GET
	public Response getAllUsers() throws RepositoryException
	{
		final Iterable<UserData> userDataIterable = userService.findAll();
		final Collection<UserData> userDataList = Lists.newArrayList(userDataIterable);
		final GenericEntity<Collection<User>> users = userMapper.convertList(userDataList);

		return Response.ok(users).build();
	}

	@GET
	@Path("team/{teamId}")
	public Response getUsersByTeam(@PathParam("teamId") final Long teamId) throws RepositoryException
	{
		final TeamData teamData = teamService.findById(teamId);
		final Collection<UserData> userDataList = userService.findByTeam(teamData);
		final GenericEntity<Collection<User>> users = userMapper.convertList(userDataList);

		return Response.ok(users).build();
	}
}
