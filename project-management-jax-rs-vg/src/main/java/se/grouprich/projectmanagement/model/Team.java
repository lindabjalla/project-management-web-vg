package se.grouprich.projectmanagement.model;

import se.grouprich.projectmanagement.status.TeamStatus;

import java.util.Set;

public final class Team extends AbstractEntity
{
	private String name;
	private TeamStatus status;
	private Set<Long> userIds;

	public Team() {}

	public Team(final Long id, final String controlId, final String name, final TeamStatus status, final Set<Long> userIds)
	{
		super(id);
		this.name = name;
		this.status = status;
		this.userIds = userIds;
	}

	public String getName()
	{
		return name;
	}

	public TeamStatus getStatus()
	{
		return status;
	}

	public Set<Long> getUserIds()
	{
		return userIds;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public void setStatus(final TeamStatus status)
	{
		this.status = status;
	}

	public void setUserIds(Set<Long> userIds)
	{
		this.userIds = userIds;
	}

	@Override
	public boolean equals(final Object other)
	{
		if (this == other)
		{
			return true;
		}

		if (other instanceof Team)
		{
			Team otherTeam = (Team) other;
			return getControlId().equals(otherTeam.getControlId()) && name.equals(otherTeam.name) && status.equals(otherTeam.status);
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		result += getControlId().hashCode() * 37;
		result += name.hashCode() * 37;
		result += status.hashCode() * 37;

		return result;
	}

	@Override
	public String toString()
	{
		return "Team [id=" + getId() + ", controlId=" + getControlId() + ", name=" + name + ", status=" + status + "]";
	}
}
