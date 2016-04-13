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
import se.grouprich.projectmanagement.Loader;
import se.grouprich.projectmanagement.TestConfig;
import se.grouprich.projectmanagement.model.Team;
import se.grouprich.projectmanagement.model.User;
import se.grouprich.projectmanagement.status.TeamStatus;
import se.grouprich.projectmanagement.status.UserStatus;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.Collection;

import static javax.ws.rs.client.Entity.json;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql(statements = "DROP ALL OBJECTS", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
public final class TeamWebServiceTest extends JerseyTest
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
		final Team team = new Team(null, null, "team1", TeamStatus.ACTIVE, null);
		target("team").request().post(json(team));

		final Team team2 = new Team(null, null, "team2", TeamStatus.ACTIVE, null);
		target("team").request().post(json(team2));

		final User user = new User(null, null, "userTest01", "12U&u", "firstName", "lastName", UserStatus.ACTIVE, null);
		target("user").request().post(json(user));
	}

	@Test
	public void shouldCreateTeam()
	{
		final Team team = new Team(null, null, "team3", TeamStatus.ACTIVE, null);
		final Response responsePost = target("team").request().post(json(team));
		final Response responseGet = target("team/4").request().get();
		final Team teamEntity = responseGet.readEntity(Team.class);

		assertThat(responsePost.getStatus(), equalTo(201));
		assertThat(responsePost.getLocation().toString(), equalTo("http://localhost:9998/team/4"));
		assertThat(teamEntity.getId(), equalTo(4L));
		assertThat(teamEntity.getName(), equalTo("team3"));
	}

	@Test
	public void shouldGetTeam()
	{
		final Response response = target("team/1").request().get();
		final Team teamEntity = response.readEntity(Team.class);

		assertThat(response.getStatus(), equalTo(200));
		assertThat(teamEntity.getId(), equalTo(1L));
		assertThat(teamEntity.getName(), equalTo("team1"));
	}

	@Test
	public void shouldUpdateTeam()
	{
		final Team team = new Team(null, null, "teamUpdated", TeamStatus.INACTIVE, null);
		final Response responsePut = target("team/1").request().put(json(team));
		final Response responseGet = target("team/1").request().get(Response.class);
		final Team updatedTeam = responseGet.readEntity(Team.class);

		assertThat(responsePut.getStatus(), equalTo(204));
		assertThat(updatedTeam.getId(), equalTo(1L));
		assertThat(updatedTeam.getName(), equalTo("teamUpdated"));
		assertThat(updatedTeam.getStatus(), equalTo(TeamStatus.INACTIVE));
	}

	@Test
	public void shouldDeleteTeam()
	{
		final Response responseDelete = target("team/1").request().delete();
		final Response responseGet = target("team/1").request().get();

		assertThat(responseDelete.getStatus(), equalTo(204));
		assertThat(responseGet.getStatus(), equalTo(404));
	}

	@Test
	public void shouldGetAllTeams()
	{
		final Response response = target("team").request().get();
		final Collection<Team> teams = response.readEntity(new GenericType<Collection<Team>>() {});

		assertThat(response.getStatus(), equalTo(200));
		assertThat(teams, hasSize(2));
	}

	@Test
	public void shouldAddUserToTeam()
	{
		final Response responsePut = target("team/1/user/3").request().put(json(new Team(null, null, "dummy", TeamStatus.ACTIVE, null)));
		final Response responseGet = target("team/1").request().get();
		final Team team = responseGet.readEntity(Team.class);

		assertThat(responsePut.getStatus(), equalTo(204));
		assertThat(team.getId(), equalTo(1L));
		assertThat(team.getUserIds(), not(empty()));
		assertThat(team.getUserIds(), hasSize(1));
		assertThat(team.getUserIds(), contains(3L));
	}
}
