package se.grouprich.projectmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.grouprich.projectmanagement.exception.InvalidValueException;
import se.grouprich.projectmanagement.exception.RepositoryException;
import se.grouprich.projectmanagement.model.TeamData;
import se.grouprich.projectmanagement.model.UserData;
import se.grouprich.projectmanagement.model.WorkItemData;
import se.grouprich.projectmanagement.repository.UserRepository;
import se.grouprich.projectmanagement.repository.WorkItemRepository;
import se.grouprich.projectmanagement.status.UserStatus;
import se.grouprich.projectmanagement.status.WorkItemStatus;

import java.util.List;

@Service
public class UserService extends AbstractService<UserData, UserRepository>
{
	private WorkItemRepository workItemRepository;

	@Autowired
	UserService(final UserRepository userRepository, final WorkItemRepository workItemRepository)
	{
		super(userRepository, UserData.class);
		this.workItemRepository = workItemRepository;
	}

	@Override
	public UserData createOrUpdate(final UserData user) throws InvalidValueException
	{
		if (user.getUsername() == null || user.getUsername().trim().length() < 10)
		{
			throw new InvalidValueException("Username must be longer than or equal to 10 characters");
		}
		return super.createOrUpdate(user);
	}

	public UserData findByControlId(final String controlId) throws RepositoryException
	{
		final UserData userData = superRepository.findByControlId(controlId);

		if (userData == null)
		{
			throw new RepositoryException("User with controlId: " + controlId + " was not found");
		}
		return userData;
	}

	public List<UserData> searchUsersByFirstNameOrLastNameOrUsername(final String firstName, final String lastName, final String username) throws RepositoryException
	{
		final List<UserData> userDataList = superRepository.findAllByFirstNameOrLastNameOrUsername(firstName, lastName, username);
		if (userDataList.isEmpty())
		{
			throw new RepositoryException("No user with firstName: " + firstName + ", lastName: " + lastName + " or username: " + username + " was found");
		}
		return userDataList;
	}

	public List<UserData> findByTeam(final TeamData team) throws RepositoryException
	{
		final List<UserData> userDataList = superRepository.findByTeam(team);
		if (userDataList.isEmpty())
		{
			throw new RepositoryException("No user with Team: " + team + " was found");
		}
		return userDataList;
	}

	@Transactional
	public UserData inactivateUser(final UserData user) throws InvalidValueException
	{
		user.setStatus(UserStatus.INACTIVE);

		final List<WorkItemData> workItemsFoundByUser = workItemRepository.findByUser(user);
		for (WorkItemData workItem : workItemsFoundByUser)
		{
			workItem.setStatus(WorkItemStatus.UNSTARTED);
			workItemRepository.save(workItem);
		}

		return createOrUpdate(user);
	}
}