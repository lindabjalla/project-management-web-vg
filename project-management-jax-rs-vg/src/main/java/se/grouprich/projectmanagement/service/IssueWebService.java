package se.grouprich.projectmanagement.service;

import se.grouprich.projectmanagement.Loader;
import se.grouprich.projectmanagement.exception.InvalidValueException;
import se.grouprich.projectmanagement.exception.RepositoryException;
import se.grouprich.projectmanagement.model.Issue;
import se.grouprich.projectmanagement.model.IssueData;
import se.grouprich.projectmanagement.model.WorkItem;
import se.grouprich.projectmanagement.model.WorkItemData;
import se.grouprich.projectmanagement.model.mapper.IssueMapper;
import se.grouprich.projectmanagement.model.mapper.WorkItemMapper;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Path("/issue")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class IssueWebService
{
	private static final IssueService issueService = Loader.getBean(IssueService.class);
	private static final WorkItemService workItemService = Loader.getBean(WorkItemService.class);
	private static final IssueMapper issueMapper = new IssueMapper();
	private static final WorkItemMapper workItemMapper = new WorkItemMapper();

	@Context
	private UriInfo uriInfo;

	@POST
	public Response createIssue(final Issue issue) throws InvalidValueException
	{
		final IssueData issueData = issueMapper.convertIssueToIssueData(issue);
		final IssueData createdIssue = issueService.createOrUpdate(issueData);
		final URI location = uriInfo.getAbsolutePathBuilder().path(getClass(), "getIssue").build(createdIssue.getId());

		return Response.created(location).build();
	}

	@GET
	@Path("{id}")
	public Response getIssue(@PathParam("id") final Long id) throws RepositoryException
	{
		final IssueData issueData = issueService.findById(id);
		final Issue issue = issueMapper.convertIssueDataToIssue(issueData);

		return Response.ok(issue).build();
	}

	@PUT
	@Path("{id}")
	public Response updateIssue(@PathParam("id") final Long id, final Issue issue) throws InvalidValueException, RepositoryException
	{
		final IssueData issueData = issueService.findById(id);
		final IssueData updateIssueData = issueMapper.updateIssueData(issue, issueData);
		issueService.createOrUpdate(updateIssueData);

		return Response.noContent().build();
	}

	@DELETE
	@Path("{id}")
	public Response deleteIssue(@PathParam("id") final Long id) throws RepositoryException, InvalidValueException
	{
		issueService.deleteById(id);
		return Response.noContent().build();
	}

	@PUT
	@Path("{issueId}/work-item/{workItemId}")
	public Response addIssueToWorkItem(@PathParam("issueId") final Long issueId, @PathParam("workItemId") final Long workItemId)
			throws InvalidValueException, RepositoryException
	{
		final IssueData issueData = issueService.findById(issueId);
		final WorkItemData workItemData = workItemService.findById(workItemId);

		issueService.addIssueToWorkItem(workItemData, issueData);

		return Response.noContent().build();
	}

	@GET
	@Path("work-item")
	public Response getWorkItemsWithIssue() throws RepositoryException
	{
		final Set<WorkItemData> workItemDataSet = issueService.fetchWorkItemsHavingIssue();
		final List<WorkItemData> workItemDataList = new ArrayList<>(workItemDataSet);
		final GenericEntity<Collection<WorkItem>> workItems = workItemMapper.convertList(workItemDataList);

		return Response.ok(workItems).build();
	}
}
