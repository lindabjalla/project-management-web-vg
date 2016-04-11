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
import se.grouprich.projectmanagement.model.Team;
import se.grouprich.projectmanagement.model.User;
import se.grouprich.projectmanagement.model.WorkItem;
import se.grouprich.projectmanagement.status.TeamStatus;
import se.grouprich.projectmanagement.status.UserStatus;
import se.grouprich.projectmanagement.status.WorkItemStatus;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.List;

import static javax.ws.rs.client.Entity.json;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppTestConfig.class)
@Sql(statements = "DROP ALL OBJECTS", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
public final class WorkItemWebServiceTest extends JerseyTest
{
	@Override
	protected Application configure()
	{
		final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Loader.class);
		return new ResourceConfig().packages("se.grouprich.projectmanagement").property("contextConfig", context);
	}

	@Before
	public void setUpTestData()
	{
		final WorkItem workItem = new WorkItem(null, null, "WorkItemTest", "Test for work item", WorkItemStatus.UNSTARTED, null);
		target("work-item").request().post(json(workItem));

		final User user = new User(null, null, "userTest01", "12U&u", "firstName", "lastName", UserStatus.ACTIVE, null);
		target("user").request().post(json(user));

		final WorkItem workItem2 = new WorkItem(null, null, "WorkItemTest2", "Test for work item 2", WorkItemStatus.UNSTARTED, null);
		target("work-item").request().post(json(workItem2));

		final Team team = new Team(null, null, "team1", TeamStatus.ACTIVE, null);
		target("team").request().post(json(team));
	}

	@Test
	public void shouldCreateWorkItem()
	{
		final WorkItem workItem = new WorkItem(null, null, "WorkItemTest3", "Test for work item 3", WorkItemStatus.UNSTARTED, null);
		final Response responsePost = target("work-item").request().post(json(workItem));
		final Response responseGet = target("work-item/5").request().get();
		final WorkItem workItemEntity = responseGet.readEntity(WorkItem.class);

		assertThat(responsePost.getStatus(), equalTo(201));
		assertThat(responsePost.getLocation().toString(), equalTo("http://localhost:9998/work-item/5"));
		assertThat(workItemEntity.getId(), equalTo(5L));
		assertThat(workItemEntity.getTitle(), equalTo("WorkItemTest3"));
	}

	@Test
	public void shouldGetWorkItem()
	{
		final Response response = target("work-item/1").request().get();
		final WorkItem workItem = response.readEntity(WorkItem.class);

		assertThat(response.getStatus(), equalTo(200));
		assertThat(workItem.getId(), equalTo(1L));
		assertThat(workItem.getTitle(), equalTo("WorkItemTest"));
	}

	@Test
	public void shouldChangeWorkItemStatus()
	{
		final Response responsePut = target("work-item/1/status/STARTED").request()
				.put(json(new WorkItem(null, null, "dummy", "dummy", WorkItemStatus.UNSTARTED, null)));
		final Response responseGet = target("work-item/1").request().get();
		final WorkItem workItem = responseGet.readEntity(WorkItem.class);

		assertThat(responsePut.getStatus(), equalTo(204));
		assertThat(workItem.getStatus(), equalTo(WorkItemStatus.STARTED));
		assertThat(workItem.getId(), equalTo(1L));
	}

	@Test
	public void shouldDeleteWorkItem()
	{
		final Response responseDelete = target("work-item/1").request().delete();
		final Response responseGet = target("work-item/1").request().get();

		assertThat(responseDelete.getStatus(), equalTo(204));
		assertThat(responseGet.getStatus(), equalTo(404));
	}

	@Test
	public void shouldAssignWorkItemToUser()
	{
		final Response responsePut = target("work-item/1/user/2").request()
				.put(json(new WorkItem(null, null, "dummy", "dummy", WorkItemStatus.UNSTARTED, null)));
		final Response responseGet = target("work-item/1").request().get();
		final WorkItem workItem = responseGet.readEntity(WorkItem.class);

		assertThat(responsePut.getStatus(), equalTo(204));
		assertThat(workItem.getId(), equalTo(1L));
		assertThat(workItem.getUser(), notNullValue());
		assertThat(workItem.getUser().getId(), equalTo(2L));
	}

	@Test
	public void shouldGetWorkItemsByStatus()
	{
		target("work-item/1/status/STARTED").request()
				.put(json(new WorkItem(null, null, "dummy", "dummy", WorkItemStatus.UNSTARTED, null)));

		final Response response = target("work-item/status/STARTED").request().get();
		final List<WorkItem> workItems = response.readEntity(new GenericType<List<WorkItem>>(){});

		assertThat(response.getStatus(), equalTo(200));
		assertThat(workItems, hasSize(1));
		assertThat(workItems.get(0).getStatus(), equalTo(WorkItemStatus.STARTED));
	}

	@Test
	public void shouldGetWorkItemsForTeam()
	{
		target("team/4/user/2").request().put(json(new Team(null, null, "dummy", TeamStatus.ACTIVE, null)));
		target("work-item/1/user/2").request().put(json(new WorkItem(null, null, "dummy", "dummy", WorkItemStatus.UNSTARTED, null)));

		final Response response = target("work-item/team/4").request().get();
		final List<WorkItem> workItems = response.readEntity(new GenericType<List<WorkItem>>(){});

		assertThat(response.getStatus(), equalTo(200));
		assertThat(workItems, hasSize(1));
		assertThat(workItems.get(0).getUser().getTeam().getId(), equalTo(4L));
	}

	@Test
	public void shouldGetWorkItemsForUser()
	{
		target("work-item/1/user/2").request().put(json(new WorkItem(null, null, "dummy", "dummy", WorkItemStatus.UNSTARTED, null)));

		final Response response = target("work-item/user/2").request().get();
		final List<WorkItem> workItems = response.readEntity(new GenericType<List<WorkItem>>(){});

		assertThat(response.getStatus(), equalTo(200));
		assertThat(workItems, hasSize(1));
		assertThat(workItems.get(0).getUser().getId(), equalTo(2L));
	}

	@Test
	public void shouldSearchWorkItemsByDescriptionContaining()
	{
		final Response response = target("work-item/search").queryParam("keyword", "2").request().get();
		final List<WorkItem> workItems = response.readEntity(new GenericType<List<WorkItem>>(){});

		assertThat(response.getStatus(), equalTo(200));
		assertThat(workItems, hasSize(1));
		assertThat(workItems.get(0).getDescription(), containsString("2"));
	}
}