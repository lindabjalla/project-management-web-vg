package se.grouprich.projectmanagement.service;

import org.apache.commons.lang3.EnumUtils;
import se.grouprich.projectmanagement.Loader;
import se.grouprich.projectmanagement.exception.InvalidStatusException;
import se.grouprich.projectmanagement.exception.InvalidValueException;
import se.grouprich.projectmanagement.exception.RepositoryException;
import se.grouprich.projectmanagement.model.TeamData;
import se.grouprich.projectmanagement.model.UserData;
import se.grouprich.projectmanagement.model.WorkItem;
import se.grouprich.projectmanagement.model.WorkItemData;
import se.grouprich.projectmanagement.model.mapper.WorkItemMapper;
import se.grouprich.projectmanagement.status.WorkItemStatus;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.Collection;
import java.util.List;

@Path("/work-item")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public final class WorkItemWebService
{
	private static final WorkItemService workItemService = Loader.getBean(WorkItemService.class);
	private static final UserService userService = Loader.getBean(UserService.class);
	private static final TeamService teamService = Loader.getBean(TeamService.class);
	private static final WorkItemMapper workItemMapper = new WorkItemMapper();

	@Context
	private UriInfo uriInfo;

	@POST
	public Response createWorkItem(final WorkItem workItem) throws InvalidValueException
	{
		workItem.setStatus(WorkItemStatus.UNSTARTED);
		final WorkItemData workItemData = workItemMapper.convertWorkItemToWorkItemData(workItem);
		final WorkItemData createdWorkItem = workItemService.createOrUpdate(workItemData);
		final URI location = uriInfo.getAbsolutePathBuilder().path(getClass(), "getWorkItem").build(createdWorkItem.getId());

		return Response.created(location).build();
	}

	@GET
	@Path("{id}")
	public Response getWorkItem(@PathParam("id") final Long id) throws RepositoryException
	{
		WorkItemData workItemData = workItemService.findById(id);
		WorkItem workItem = workItemMapper.convertWorkItemDataToWorkItem(workItemData);

		return Response.ok(workItem).build();
	}

	@PUT
	@Path("{id}/status/{status}")
	public Response changeWorkItemStatus(@PathParam("id") final Long id, @PathParam("status") final WorkItemStatus status) throws RepositoryException, InvalidValueException
	{
		final WorkItemData workItemData = workItemService.findById(id);

		if (!EnumUtils.isValidEnum(WorkItemStatus.class, status.toString()))
		{
			throw new InvalidStatusException(status.toString());
		}
		workItemService.changeWorkItemStatus(workItemData, status);

		return Response.noContent().build();
	}

	@DELETE
	@Path("{id}")
	public Response deleteWorkItem(@PathParam("id") final Long id) throws RepositoryException
	{
		final WorkItemData workItemData = workItemService.findById(id);
		workItemService.removeWorkItem(workItemData);

		return Response.noContent().build();
	}

	@PUT
	@Path("{workItemId}/user/{userId}")
	public Response assignWorkItemToUser(@PathParam("workItemId") final Long workItemId, @PathParam("userId") final Long userId)
			throws RepositoryException, InvalidValueException
	{
		final UserData userData = userService.findById(userId);
		final WorkItemData workItemData = workItemService.findById(workItemId);
		workItemService.assignWorkItemToUser(userData, workItemData);

		return Response.noContent().build();
	}

	@GET
	@Path("status/{status}")
	public Response getWorkItemsByStatus(@PathParam("status") final WorkItemStatus status) throws RepositoryException
	{
		if (!EnumUtils.isValidEnum(WorkItemStatus.class, status.toString()))
		{
			throw new InvalidStatusException(status.toString());
		}
		final List<WorkItemData> workItemDataList = workItemService.fetchWorkItemsByStatus(status);
		final GenericEntity<Collection<WorkItem>> workItems = workItemMapper.convertList(workItemDataList);

		return Response.ok(workItems).build();
	}

	@GET
	@Path("team/{teamId}")
	public Response getWorkItemsForTeam(@PathParam("teamId") final Long teamId) throws RepositoryException
	{
		final TeamData teamData = teamService.findById(teamId);
		final List<WorkItemData> workItemDataList = workItemService.fetchWorkItemsForTeam(teamData);
		final GenericEntity<Collection<WorkItem>> workItems = workItemMapper.convertList(workItemDataList);

		return Response.ok(workItems).build();
	}

	@GET
	@Path("user/{userId}")
	public Response getWorkItemsForUser(@PathParam("userId") final Long userId) throws RepositoryException
	{
		final UserData userData = userService.findById(userId);
		final List<WorkItemData> workItemDataList = workItemService.fetchWorkItemsForUser(userData);
		final GenericEntity<Collection<WorkItem>> workItems = workItemMapper.convertList(workItemDataList);

		return Response.ok(workItems).build();
	}

	@GET
	@Path("search")
	public Response searchWorkItemsByDescriptionContaining(@QueryParam("keyword") final String keyword) throws RepositoryException
	{
		final List<WorkItemData> workItemDataList = workItemService.searchWorkItemsByDescriptionContaining(keyword);
		final GenericEntity<Collection<WorkItem>> workItems = workItemMapper.convertList(workItemDataList);

		return Response.ok(workItems).build();
	}
}



