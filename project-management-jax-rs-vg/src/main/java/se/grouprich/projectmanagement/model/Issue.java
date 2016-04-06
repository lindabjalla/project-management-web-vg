package se.grouprich.projectmanagement.model;

public class Issue extends AbstractEntity
{
	private String description;
	private WorkItem workItem;

	public Issue(){}

	public Issue(final Long id, final String controlId, final String description, final WorkItem workItem)
	{
		super(id);
		this.description = description;
		this.workItem = workItem;
	}

	public String getDescription()
	{
		return description;
	}

	public WorkItem getWorkItem()
	{
		return workItem;
	}

	public void setDescription(final String description)
	{
		this.description = description;
	}

	public void setWorkItem(final WorkItem workItem)
	{
		this.workItem = workItem;
	}
	
	@Override
	public boolean equals(final Object other)
	{
		if (this == other)
		{
			return true;
		}

		if (other instanceof Issue)
		{
			Issue otherIssue = (Issue) other;
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
