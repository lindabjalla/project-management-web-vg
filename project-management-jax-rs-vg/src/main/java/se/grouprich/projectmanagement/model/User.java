package se.grouprich.projectmanagement.model;

import se.grouprich.projectmanagement.status.UserStatus;

public final class User extends AbstractEntity
{
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private UserStatus status;
	private Team team;

	public User() {}

	public User(final Long id, String controlId, final String username, final String password, final String firstName,
			final String lastName, final UserStatus status, final Team team)
	{
		super(id);
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.status = status;
		this.team = team;
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

	public Team getTeam()
	{
		return team;
	}

	public User setUsername(final String username)
	{
		this.username = username;
		return this;
	}

	public User setPassword(final String password)
	{
		this.password = password;
		return this;
	}

	public User setFirstName(final String firstName)
	{
		this.firstName = firstName;
		return this;
	}

	public User setLastName(final String lastName)
	{
		this.lastName = lastName;
		return this;
	}

	public User setStatus(final UserStatus status)
	{
		this.status = status;
		return this;
	}

	public void setTeam(final Team team)
	{
		this.team = team;
	}

	@Override
	public boolean equals(final Object other)
	{
		if (this == other)
		{
			return true;
		}
		if (other instanceof User)
		{
			User otherUser = (User) other;
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
		return "User [id=" + getId() + ", userNumber=" + getControlId() + ", username=" + username + ", password=" + password + ", firstName="
				+ firstName + ", lastName=" + lastName + ", status=" + status + ", team=" + team + "]";
	}
}
