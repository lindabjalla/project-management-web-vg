package se.grouprich.projectmanagement.model;

import se.grouprich.projectmanagement.status.TeamStatus;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Team")
public class TeamData extends AbstractEntityData
{
	@Column(nullable = false)
	private String name;

	@OneToMany(mappedBy = "team", cascade = CascadeType.MERGE)
	private Set<UserData> users;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private TeamStatus status;

	protected TeamData() {}

	public TeamData(final String name, final TeamStatus status)
	{
		this.name = name;
		this.status = status;
		users = new HashSet<>();
	}

	public String getName()
	{
		return name;
	}

	public Set<UserData> getUsers()
	{
		return users;
	}

	public TeamStatus getStatus()
	{
		return status;
	}

	public TeamData setName(final String name)
	{
		this.name = name;
		return this;
	}

	public void setStatus(final TeamStatus status)
	{
		this.status = status;
	}

	public TeamData addUser(final UserData user)
	{
		if (!users.contains(user))
		{
			user.setTeam(this);
			users.add(user);
		}
		return this;
	}

	@Override
	public boolean equals(final Object other)
	{
		if (this == other)
		{
			return true;
		}
		if (other instanceof TeamData)
		{
			TeamData otherTeam = (TeamData) other;
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
