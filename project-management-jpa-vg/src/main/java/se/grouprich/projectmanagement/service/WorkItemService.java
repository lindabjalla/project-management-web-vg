package se.grouprich.projectmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.grouprich.projectmanagement.exception.InvalidValueException;
import se.grouprich.projectmanagement.exception.RepositoryException;
import se.grouprich.projectmanagement.model.TeamData;
import se.grouprich.projectmanagement.model.UserData;
import se.grouprich.projectmanagement.model.WorkItemData;
import se.grouprich.projectmanagement.repository.IssueRepository;
import se.grouprich.projectmanagement.repository.UserRepository;
import se.grouprich.projectmanagement.repository.WorkItemRepository;
import se.grouprich.projectmanagement.status.UserStatus;
import se.grouprich.projectmanagement.status.WorkItemStatus;

import java.util.List;

@Service
public class WorkItemService extends AbstractService<WorkItemData, WorkItemRepository>
{
	private IssueRepository issueRepository;
	private UserRepository userRepository;

	@Autowired
	WorkItemService(final WorkItemRepository workItemRepository, final IssueRepository issueRepository, final UserRepository userRepository)
	{
		super(workItemRepository, WorkItemData.class);
		this.issueRepository = issueRepository;
		this.userRepository = userRepository;
	}

	public WorkItemData createOrUpdate(final WorkItemData workItem) throws InvalidValueException
	{
		return super.createOrUpdate(workItem);
	}

	public WorkItemData changeWorkItemStatus(final WorkItemData workItem, final WorkItemStatus status) throws InvalidValueException
	{
		workItem.setStatus(status);
		return createOrUpdate(workItem);
	}

	@Transactional
	public WorkItemData removeWorkItem(final WorkItemData workItem)
	{
		issueRepository.removeByWorkItem(workItem);
		return superRepository.removeById(workItem.getId()).get(0);
	}

	@Transactional
	public WorkItemData assignWorkItemToUser(final UserData user, final WorkItemData workItem) throws InvalidValueException
	{
		final UserData savedUser = userRepository.save(user);
		if (!UserStatus.ACTIVE.equals(savedUser.getStatus()))
		{
			throw new InvalidValueException("A WorkItem can only be assigned to a User with UserStatus.ACTIVE");
		}

		final List<WorkItemData> workItemsFoundByUser = superRepository.findByUser(savedUser);
		if (workItemsFoundByUser.size() >= 5)
		{
			throw new InvalidValueException("Maximum number of work items a User can have is 5");
		}

		final WorkItemData assignedWorkItem = workItem.setUser(savedUser);
		return createOrUpdate(assignedWorkItem);
	}

	public List<WorkItemData> fetchWorkItemsByStatus(final WorkItemStatus status) throws RepositoryException
	{
		final List<WorkItemData> workItemDataList = superRepository.findByStatus(status);
		if (workItemDataList.isEmpty())
		{
			throw new RepositoryException("WorkItem with WorkItemStatus: " + status + " was not found");
		}
		return workItemDataList;
	}

	public List<WorkItemData> fetchWorkItemsForTeam(final TeamData team) throws RepositoryException
	{
		final List<WorkItemData> workItemDataList = superRepository.findByTeam(team);
		if (workItemDataList.isEmpty())
		{
			throw new RepositoryException("WorkItem for Team: " + team + " was not found");
		}
		return workItemDataList;
	}

	public List<WorkItemData> fetchWorkItemsForUser(final UserData user) throws RepositoryException
	{
		final List<WorkItemData> workItemDataList = superRepository.findByUser(user);
		if (workItemDataList.isEmpty())
		{
			throw new RepositoryException("WorkItem for User: " + user + " was not found");
		}
		return superRepository.findByUser(user);
	}

	public List<WorkItemData> searchWorkItemsByDescriptionContaining(final String keyword) throws RepositoryException
	{
		final List<WorkItemData> workItemDataList = superRepository.findByDescriptionContaining(keyword);
		if (workItemDataList.isEmpty())
		{
			throw new RepositoryException("WorkItem with keyword: " + keyword + " was not found");
		}
		return workItemDataList;
	}
}
