package se.grouprich.projectmanagement.model;

import se.grouprich.projectmanagement.status.TeamStatus;

public final class Team extends AbstractEntity
{
	private String name;
	private TeamStatus status;

	public Team() {}

	public Team(final Long id, final String controlId, final String name, final TeamStatus status)
	{
		super(id);
		this.name = name;
		this.status = status;
	}

	public String getName()
	{
		return name;
	}

	public TeamStatus getStatus()
	{
		return status;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public void setStatus(final TeamStatus status)
	{
		this.status = status;
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
