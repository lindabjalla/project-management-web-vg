package se.grouprich.projectmanagement.service;

import com.google.common.collect.Lists;
import se.grouprich.projectmanagement.Loader;
import se.grouprich.projectmanagement.exception.InvalidValueException;
import se.grouprich.projectmanagement.exception.RepositoryException;
import se.grouprich.projectmanagement.model.Team;
import se.grouprich.projectmanagement.model.TeamData;
import se.grouprich.projectmanagement.model.UserData;
import se.grouprich.projectmanagement.model.mapper.TeamMapper;
import se.grouprich.projectmanagement.status.TeamStatus;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.Collection;

@Path("/team")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TeamWebService
{
	private static final TeamService teamService = Loader.getBean(TeamService.class);
	private static final UserService userService = Loader.getBean(UserService.class);
	private static final TeamMapper teamMapper = new TeamMapper();

	@Context
	private UriInfo uriInfo;

	@POST
	public Response createTeam(final Team team) throws InvalidValueException
	{
		team.setStatus(TeamStatus.ACTIVE);
		final TeamData teamData = teamMapper.convertTeamToTeamData(team);
		final TeamData createdTeam = teamService.createOrUpdate(teamData);
		final URI location = uriInfo.getAbsolutePathBuilder().path(getClass(), "getTeam").build(createdTeam.getId());

		return Response.created(location).build();
	}

	@GET
	@Path("{id}")
	public Response getTeam(@PathParam("id") final Long id) throws RepositoryException
	{
		final TeamData teamData = teamService.findById(id);
		final Team team = teamMapper.convertTeamDataToTeam(teamData);

		return Response.ok(team).build();
	}

	@PUT
	@Path("{id}")
	public Response updateTeam(@PathParam("id") final Long id, final Team team) throws InvalidValueException, RepositoryException
	{
		final TeamData teamData = teamService.findById(id);
		final TeamData updateTeamData = teamMapper.updateTeamData(team, teamData);
		teamService.createOrUpdate(updateTeamData);

		return Response.noContent().build();
	}

	@DELETE
	@Path("{id}")
	public Response deleteTeam(@PathParam("id") final Long id) throws RepositoryException, InvalidValueException
	{
		teamService.deleteById(id);
		return Response.noContent().build();
	}

	@GET
	public Response getAllTeams() throws RepositoryException
	{
		final Iterable<TeamData> teamDataIterable = teamService.findAll();
		final Collection<TeamData> teamDataList = Lists.newArrayList(teamDataIterable);
		final GenericEntity<Collection<Team>> teams = teamMapper.convertList(teamDataList);

		return Response.ok(teams).build();
	}

	@PUT
	@Path("{teamId}/user/{userId}")
	public Response addUserToTeam(@PathParam("teamId") final Long teamId, @PathParam("userId") final Long userId) throws InvalidValueException, RepositoryException
	{
		final TeamData teamData = teamService.findById(teamId);
		final UserData userData = userService.findById(userId);
		teamService.addUserToTeam(teamData, userData);

		return Response.noContent().build();
	}
}
