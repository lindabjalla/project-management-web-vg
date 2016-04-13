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
import java.util.List;

import static javax.ws.rs.client.Entity.json;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql(statements = "DROP ALL OBJECTS", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
public final class UserWebServiceTest extends JerseyTest
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
		final User user = new User(null, null, "userTest01", "12U&u", "firstName", "lastName", UserStatus.ACTIVE, null);
		target("user").request().post(json(user));

		final User user2 = new User(null, null, "userTest02", "8R#rh", "firstName", "lastName", UserStatus.ACTIVE, null);
		target("user").request().post(json(user2));

		final Team team = new Team(null, null, "team1", TeamStatus.ACTIVE, null);
		target("team").request().post(json(team));
	}

	@Test
	public void shouldCreateUser()
	{
		final User user = new User(null, null, "userTest03", "3K€0h", "firstName3", "lastName3", UserStatus.ACTIVE, null);
		final Response responsePost = target("user").request().post(json(user));
		final Response responseGet = target("user/4").request().get();
		final User userEntity = responseGet.readEntity(User.class);

		assertThat(responsePost.getStatus(), equalTo(201));
		assertThat(responsePost.getLocation().toString(), equalTo("http://localhost:9998/user/4"));
		assertThat(userEntity.getId(), equalTo(4L));
		assertThat(userEntity.getUsername(), equalTo("userTest03"));
	}

	@Test
	public void shouldGetUser()
	{
		final Response response = target("user/1").request().get();
		final User user = response.readEntity(User.class);

		assertThat(response.getStatus(), equalTo(200));
		assertThat(user.getId(), equalTo(1L));
		assertThat(user.getUsername(), equalTo("userTest01"));
	}

	@Test
	public void shouldUpdateUser()
	{
		final User user = new User(null, null, "userUpdated", "12U&u", "firstName", "lastName", UserStatus.INACTIVE, null);
		final Response responsePut = target("user/1").request().put(json(user));
		final Response responseGet = target("user/1").request().get();
		final User updatedUser = responseGet.readEntity(User.class);

		assertThat(responsePut.getStatus(), equalTo(204));
		assertThat(updatedUser.getId(), equalTo(1L));
		assertThat(updatedUser.getUsername(), equalTo("userUpdated"));
		assertThat(updatedUser.getStatus(), equalTo(UserStatus.INACTIVE));
	}

	@Test
	public void shouldDeleteUser()
	{
		final Response responseDelete = target("user/1").request().delete();
		final Response responseGet = target("user/1").request().get();

		assertThat(responseDelete.getStatus(), equalTo(204));
		assertThat(responseGet.getStatus(), equalTo(404));
	}

	@Test
	public void shouldGetUserByControlId()
	{
		final Response responseGotById = target("user/1").request().get();
		final User userGotById = responseGotById.readEntity(User.class);

		final Response responseGotByControlId = target("user/control-id/" + userGotById.getControlId()).request().get();
		final User userGotByControlId = responseGotByControlId.readEntity(User.class);

		assertThat(responseGotByControlId.getStatus(), equalTo(200));
		assertThat(userGotByControlId, equalTo(userGotById));
		assertThat(userGotByControlId.getId(), equalTo(1L));
	}

	@Test
	public void shouldSearchUsersByFirstNameOrLastNameOrUsername()
	{
		final Response responseFirstName = target("user/search").queryParam("first-name", "firstName").request().get();
		final List<User> usersFoundByFirstName = responseFirstName.readEntity(new GenericType<List<User>>(){});

		final Response responseLastName = target("user/search").queryParam("last-name", "lastName").request().get();
		final List<User> usersFoundByLastName = responseLastName.readEntity(new GenericType<List<User>>(){});

		final Response responseUsername = target("user/search").queryParam("username", "userTest01").request().get(Response.class);
		final List<User> usersFoundByUsername = responseUsername.readEntity(new GenericType<List<User>>(){});

		assertThat(responseFirstName.getStatus(), equalTo(200));
		assertThat(responseLastName.getStatus(), equalTo(200));
		assertThat(responseUsername.getStatus(), equalTo(200));

		assertThat(usersFoundByFirstName, hasSize(2));
		assertThat(usersFoundByLastName, hasSize(2));
		assertThat(usersFoundByUsername, hasSize(1));

		assertThat(usersFoundByFirstName.get(0).getFirstName(), equalTo("firstName"));
		assertThat(usersFoundByFirstName.get(1).getFirstName(), equalTo("firstName"));
		assertThat(usersFoundByLastName.get(0).getLastName(), equalTo("lastName"));
		assertThat(usersFoundByLastName.get(1).getLastName(), equalTo("lastName"));
		assertThat(usersFoundByUsername.get(0).getUsername(), equalTo("userTest01"));
	}

	@Test
	public void shouldGetAllUsers()
	{
		final Response response = target("user").request().get();
		final Collection<User> users = response.readEntity(new GenericType<Collection<User>>(){});

		assertThat(response.getStatus(), equalTo(200));
		assertThat(users, hasSize(2));
	}

	@Test
	public void shouldGetUsersByTeam()
	{
		final Team dummyTeam = new Team(null, null, "dummy", TeamStatus.ACTIVE, null);
		target("team/3/user/1").request().put(json(dummyTeam));
		target("team/3/user/2").request().put(json(dummyTeam));

		final Response response = target("user/team/3").request().get();
		final List<User> users = response.readEntity(new GenericType<List<User>>(){});

		assertThat(response.getStatus(), equalTo(200));
		assertThat(users, hasSize(2));
		assertThat(users.get(0).getTeam().getId(), equalTo(3L));
		assertThat(users.get(1).getTeam().getId(), equalTo(3L));
	}
}
