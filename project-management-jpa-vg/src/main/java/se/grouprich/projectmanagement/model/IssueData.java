package se.grouprich.projectmanagement.model;

import javax.persistence.*;

@Entity
@Table(name = "Issue")
public class IssueData extends AbstractEntityData
{
	@Column(name = "issue", columnDefinition = "TEXT", nullable = false)
	private String description;

	@ManyToOne(cascade = CascadeType.MERGE)
	private WorkItemData workItem;

	protected IssueData() {}

	public IssueData(final String description)
	{
		this.description = description;
	}

	public String getDescription()
	{
		return description;
	}

	public WorkItemData getWorkItem()
	{
		return workItem;
	}

	public IssueData setDescription(final String description)
	{
		this.description = description;
		return this;
	}

	public IssueData setWorkItem(final WorkItemData workItem)
	{
		this.workItem = workItem;
		return this;
	}

	@Override
	public boolean equals(final Object other)
	{
		if (this == other)
		{
			return true;
		}
		if (other instanceof IssueData)
		{
			IssueData otherIssue = (IssueData) other;
			return getControlId().equals(otherIssue.getControlId()) && description.equals(otherIssue.description);
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		result += getControlId().hashCode() * 37;
		result += description.hashCode() * 37;

		return result;
	}

	@Override
	public String toString()
	{
		return "Issue [id=" + getId() + ", controlId=" + getControlId() + ", description=" + description + ", workItem=" + workItem + "]";
	}
}
