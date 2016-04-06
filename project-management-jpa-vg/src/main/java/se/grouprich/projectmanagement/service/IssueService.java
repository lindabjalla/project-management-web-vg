package se.grouprich.projectmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.grouprich.projectmanagement.exception.InvalidValueException;
import se.grouprich.projectmanagement.exception.RepositoryException;
import se.grouprich.projectmanagement.model.IssueData;
import se.grouprich.projectmanagement.model.WorkItemData;
import se.grouprich.projectmanagement.repository.IssueRepository;
import se.grouprich.projectmanagement.status.WorkItemStatus;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class IssueService extends AbstractService<IssueData, IssueRepository>
{
	@Autowired
	IssueService(final IssueRepository issueRepository)
	{
		super(issueRepository, IssueData.class);
	}

	@Transactional
	public IssueData addIssueToWorkItem(final WorkItemData workItem, final IssueData issue) throws InvalidValueException
	{
		if (!WorkItemStatus.DONE.equals(workItem.getStatus()))
		{
			throw new InvalidValueException("An Issue can only be added to a WorkItem with WorkItemStatus.DONE");
		}

		final IssueData issueAddedToWorkItem = issue.setWorkItem(workItem);
		workItem.setStatus(WorkItemStatus.UNSTARTED);

		return createOrUpdate(issueAddedToWorkItem);
	}

	public Set<WorkItemData> fetchWorkItemsHavingIssue() throws RepositoryException
	{
		final List<WorkItemData> workItemsHavingIssue = superRepository.findWorkItemsHavingIssue();
		if (workItemsHavingIssue.isEmpty())
		{
			throw new RepositoryException("No WorkItem with Issue was found");
		}
		return new HashSet<>(workItemsHavingIssue);
	}
}