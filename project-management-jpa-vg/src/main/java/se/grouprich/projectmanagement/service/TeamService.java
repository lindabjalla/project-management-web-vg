package se.grouprich.projectmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.grouprich.projectmanagement.exception.InvalidValueException;
import se.grouprich.projectmanagement.exception.RepositoryException;
import se.grouprich.projectmanagement.model.TeamData;
import se.grouprich.projectmanagement.model.UserData;
import se.grouprich.projectmanagement.repository.TeamRepository;
import se.grouprich.projectmanagement.repository.UserRepository;
import se.grouprich.projectmanagement.status.TeamStatus;

import java.util.Set;

@Service
public class TeamService extends AbstractService<TeamData, TeamRepository>
{
	private UserRepository userRepository;

	@Autowired
	TeamService(final TeamRepository teamRepository, final UserRepository userRepository)
	{
		super(teamRepository, TeamData.class);
		this.userRepository = userRepository;
	}

	@Override
	public TeamData findById(final Long id) throws RepositoryException
	{
		final TeamData teamData = superRepository.findTeamById(id);
		if (teamData == null)
		{
			throw new RepositoryException("Team with id: " + id + " was not found");
		}
		return superRepository.findTeamById(id);
	}

	@Transactional
	@Override
	public TeamData deleteById(final Long id) throws RepositoryException, InvalidValueException
	{
		final TeamData team = findById(id);
		final Set<UserData> users = team.getUsers();
		if (!users.isEmpty())
		{
			users.forEach(user -> userRepository.save(user.setTeam(null)));
		}
		super.deleteById(id);
		return team;
	}

	@Transactional
	public TeamData addUserToTeam(final TeamData team, final UserData user) throws RepositoryException, InvalidValueException
	{
		userRepository.save(user);

		if (user.getTeam() != null)
		{
			throw new InvalidValueException("User is already in a Team. A User can only be in one Team at a time");
		}
		if (team.getUsers().size() >= 10)
		{
			throw new InvalidValueException("Maximum number of users in a Team is 10");
		}

		final TeamData teamUserAdded = team.addUser(user);
		return createOrUpdate(teamUserAdded);
	}

	public TeamData inactivateTeam(final TeamData team) throws InvalidValueException
	{
		team.setStatus(TeamStatus.INACTIVE);
		return createOrUpdate(team);
	}
}
