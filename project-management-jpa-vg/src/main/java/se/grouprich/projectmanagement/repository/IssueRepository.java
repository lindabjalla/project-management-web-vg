package se.grouprich.projectmanagement.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import se.grouprich.projectmanagement.model.IssueData;
import se.grouprich.projectmanagement.model.WorkItemData;

import java.util.List;

public interface IssueRepository extends CrudRepository<IssueData, Long>
{
	@Query("SELECT i.workItem FROM #{#entityName} i")
	List<WorkItemData> findWorkItemsHavingIssue();

	@Transactional
	List<IssueData> removeByWorkItem(WorkItemData workItem);
}
