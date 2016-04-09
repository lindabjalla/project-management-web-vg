package se.grouprich.projectmanagement.model.mapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import se.grouprich.projectmanagement.model.Team;
import se.grouprich.projectmanagement.model.TeamData;

import javax.ws.rs.core.GenericEntity;
import java.util.ArrayList;
import java.util.Collection;

public final class TeamMapper
{
	private final MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
	private final MapperFacade mapper = mapperFactory.getMapperFacade();

	public TeamMapper()
	{
		mapperFactory.classMap(Team.class, TeamData.class).fieldBToA("users{id}", "userIds{}").byDefault().register();
	}

	public TeamData convertTeamToTeamData(final Team team)
	{
		return mapper.map(team, TeamData.class);
	}

	public Team convertTeamDataToTeam(final TeamData teamData)
	{
		return mapper.map(teamData, Team.class);
	}

	public TeamData updateTeamData(final Team team, final TeamData teamData)
	{
		teamData.setName(team.getName()).setStatus(team.getStatus());
		return teamData;
	}

	public GenericEntity<Collection<Team>> convertList(final Collection<TeamData> teamDataList)
	{
		Collection<Team> teams = new ArrayList<>();
		teamDataList.forEach(teamData -> teams.add(convertTeamDataToTeam(teamData)));

		return new GenericEntity<Collection<Team>>(teams){};
	}
}
