package se.grouprich.projectmanagement.model;

import se.grouprich.projectmanagement.status.UserStatus;

import javax.persistence.*;

@Entity
@Table(name = "User")
public class UserData extends AbstractEntityData
{
	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String firstName;

	@Column(nullable = false)
	private String lastName;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UserStatus status;

	@ManyToOne(cascade = CascadeType.MERGE)
	private TeamData team;

	protected UserData() {}

	public UserData(final String username, final String password, final String firstName, final String lastName, final UserStatus status)
	{
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.status = status;
	}

	public String getUsername()
	{
		return username;
	}

	public String getPassword()
	{
		return password;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public UserStatus getStatus()
	{
		return status;
	}

	public TeamData getTeam()
	{
		return team;
	}

	public UserData setUsername(final String username)
	{
		this.username = username;
		return this;
	}

	public UserData setPassword(final String password)
	{
		this.password = password;
		return this;
	}

	public UserData setStatus(final UserStatus status)
	{
		this.status = status;
		return this;
	}

	public UserData setTeam(final TeamData team)
	{
		if (this.team == null && team != null)
		{
			this.team = team;
			this.team.addUser(this);
		}
		else if (this.team != null && !this.team.equals(team))
		{
			this.team.getUsers().remove(this);
			this.team = team;
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
		if (other instanceof UserData)
		{
			UserData otherUser = (UserData) other;
			return getControlId().equals(otherUser.getControlId()) && username.equals(otherUser.username)
					&& password.equals(otherUser.password) && firstName.equals(otherUser.firstName)
					&& lastName.equals(otherUser.lastName) && status.equals(otherUser.status);
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		result += getControlId().hashCode() * 37;
		result += username.hashCode() * 37;
		result += password.hashCode() * 37;
		result += firstName.hashCode() * 37;
		result += lastName.hashCode() * 37;
		result += status.hashCode() * 37;

		return result;
	}

	@Override
	public String toString()
	{
		return "User [id=" + getId() + ", controlId=" + getControlId() + ", username=" + username + ", password=" + password +
				", firstName=" + firstName + ", lastName=" + lastName + ", status=" + status + ", team=" + team + "]";
	}
}
