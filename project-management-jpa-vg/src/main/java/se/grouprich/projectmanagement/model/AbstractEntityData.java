package se.grouprich.projectmanagement.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.RandomStringUtils;

@MappedSuperclass
public abstract class AbstractEntityData
{
	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false, unique = true)
	private String controlId;

	protected AbstractEntityData()
	{
		controlId = RandomStringUtils.randomAlphanumeric(6);
	}

	public Long getId()
	{
		return id;
	}

	public String getControlId()
	{
		return controlId;
	}
}
