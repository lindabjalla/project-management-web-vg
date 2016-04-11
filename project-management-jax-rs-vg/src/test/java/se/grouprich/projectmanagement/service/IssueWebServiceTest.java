package se.grouprich.projectmanagement.service;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.grouprich.projectmanagement.AppTestConfig;
import se.grouprich.projectmanagement.Loader;
import se.grouprich.projectmanagement.exception.InvalidValueException;
import se.grouprich.projectmanagement.model.Issue;
import se.grouprich.projectmanagement.model.WorkItem;
import se.grouprich.projectmanagement.status.WorkItemStatus;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.Collection;

import static javax.ws.rs.client.Entity.json;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppTestConfig.class)
@Sql(statements = "DROP ALL OBJECTS", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
public final class IssueWebServiceTest extends JerseyTest
{
	@Override
	protected Application configure()
	{
		return new ResourceConfig().packages("se.grouprich.projectmanagement")
				.property("contextConfig", new AnnotationConfigApplicationContext(Loader.class));
	}

	@Before
	public void setUpTestData()
	{
		final Issue issue = new Issue(null, null, "issueTest", null);
		target("issue").request().post(json(issue), Response.class);

		final WorkItem workItem = new WorkItem(null, null, "WorkItemTest", "Test for work item", WorkItemStatus.UNSTARTED, null);
		target("work-item").request().post(json(workItem), Response.class);

		final Response responseGetWorkItem = target("work-item/2").request().get(Response.class);
		final WorkItem workItemEntity = responseGetWorkItem.readEntity(WorkItem.class).setStatus(WorkItemStatus.DONE);

		target("work-item/2/status/DONE").request().put(json(workItemEntity), Response.class);

		final Issue issue2 = new Issue(null, null, "issueTest2", null);
		target("issue").request().post(json(issue2), Response.class);

		final WorkItem workItem2 = new WorkItem(null, null, "WorkItemTest2", "Test for work item2", WorkItemStatus.UNSTARTED, null);
		target("work-item").request().post(json(workItem2), Response.class);

		final Response responseGetWorkItem2 = target("work-item/4").request().get(Response.class);
		final WorkItem workItemEntity2 = responseGetWorkItem2.readEntity(WorkItem.class).setStatus(WorkItemStatus.DONE);

		target("work-item/4/status/DONE").request().put(json(workItemEntity2), Response.class);
	}

	@Test
	public void shouldCreateIssue()
	{
		final Issue issue = new Issue(null, null, "issueTest3", null);
		final Response responsePost = target("issue").request().post(json(issue), Response.class);
		final Response responseGet = target("issue/5").request().get(Response.class);
		final Issue issueEntity = responseGet.readEntity(Issue.class);

		assertThat(responsePost.getStatus(), equalTo(201));
		assertThat(responsePost.getLocation().toString(), equalTo("http://localhost:9998/issue/5"));
		assertThat(issueEntity.getId(), equalTo(5L));
		assertThat(issueEntity.getDescription(), equalTo("issueTest3"));
	}

	@Test
	public void shouldGetIssue() throws InvalidValueException
	{
		final Response response = target("issue/1").request().get(Response.class);
		final Issue issueEntity = response.readEntity(Issue.class);

		assertThat(response.getStatus(), equalTo(200));
		assertThat(issueEntity.getId(), equalTo(1L));
		assertThat(issueEntity.getDescription(), equalTo("issueTest"));
	}

	@Test
	public void shouldUpdateIssue()
	{
		final Issue issue2 = new Issue(null, null, "issueTest2", null);

		final Response responsePut = target("issue/1").request().put(json(issue2), Response.class);
		final Response responseGet = target("issue/1").request().get(Response.class);
		final Issue issueEntity = responseGet.readEntity(Issue.class);

		assertThat(responsePut.getStatus(), equalTo((204)));
		assertThat(issueEntity.getId(), equalTo(1L));
		assertThat(issueEntity.getDescription(), equalTo("issueTest2"));
	}

	@Test
	public void shouldDeleteIssue()
	{
		final Response responseDelete = target("issue/1").request().delete(Response.class);
		final Response responseGet = target("issue/1").request().get(Response.class);

		assertThat(responseDelete.getStatus(), equalTo(204));
		assertThat(responseGet.getStatus(), equalTo(404));
	}

	@Test
	public void shouldAddIssueToWorkItem()
	{
		final Response responsePut = target("issue/1/work-item/2").request().put(json(new Issue(null, null, "dummy", null)), Response.class);

		final Response responseGet = target("issue/1").request().get(Response.class);
		final Issue issue = responseGet.readEntity(Issue.class);

		assertThat(responsePut.getStatus(), equalTo(204));
		assertThat(issue.getWorkItem(), notNullValue());
		assertThat(issue.getWorkItem().getId(), equalTo(2L));
		assertThat(issue.getWorkItem().getTitle(), equalTo("WorkItemTest"));
	}

	@Test
	public void shouldGetWorkItemsWithIssue()
	{
		target("issue/1/work-item/2").request().put(json(new Issue(null, null, "dummy", null)), Response.class);
		target("issue/3/work-item/4").request().put(json(new Issue(null, null, "dummy", null)), Response.class);

		final Response response = target("issue/work-item").request().get(Response.class);
		final Collection<WorkItem> workItems = response.readEntity(new GenericType<Collection<WorkItem>>() {});

		assertThat(response.getStatus(), equalTo(200));
		assertThat(workItems, hasSize(2));
	}
}