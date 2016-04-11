package se.grouprich.projectmanagement.model;

import se.grouprich.projectmanagement.status.WorkItemStatus;

public final class WorkItem extends AbstractEntity
{
	private String title;
	private String description;
	private WorkItemStatus status;
	private User user;

	public WorkItem() {}

	public WorkItem(final Long id, final String controlId, final String title, final String description, final WorkItemStatus status,
			final User user)
	{
		super(id);
		this.title = title;
		this.description = description;
		this.status = status;
		this.user = user;
	}

	public String getTitle()
	{
		return title;
	}

	public User getUser()
	{
		return user;
	}

	public String getDescription()
	{
		return description;
	}

	public WorkItemStatus getStatus()
	{
		return status;
	}

	public void setTitle(final String title)
	{
		this.title = title;
	}

	public WorkItem setUser(final User user)
	{
		this.user = user;
		return this;
	}

	public void setDescription(final String description)
	{
		this.description = description;
	}

	public WorkItem setStatus(final WorkItemStatus status)
	{
		this.status = status;
		return this;
	}

	@Override
	public boolean equals(final Object other)
	{
		if (this == other)
		{
			return true;
		}

		if (other instanceof WorkItem)
		{
			WorkItem otherWorkItem = (WorkItem) other;
			return getControlId().equals(otherWorkItem.getControlId()) && title.equals(otherWorkItem.title)
					&& user.equals(otherWorkItem.user) && description.equals(otherWorkItem.description)
					&& status.equals(otherWorkItem.status);
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		result += getControlId().hashCode() * 37;
		result += title.hashCode() * 37;
		result += user.hashCode() * 37;
		result += description.hashCode() * 37;
		result += status.hashCode() * 37;

		return result;
	}

	@Override
	public String toString()
	{
		return "WorkItem [id=" + getId() + ", title=" + title + ", user=" + user + ", description=" + description + ", status=" + status + "]";
	}
}
