package se.grouprich.projectmanagement.model;

abstract class AbstractEntity
{
	private Long id;
	private String controlId;

	AbstractEntity() {}

	AbstractEntity(Long id)
	{
		this.id = id;
	}
	
	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getControlId()
	{
		return controlId;
	}

	public void setControlId(String controlId)
	{
		this.controlId = controlId;
	}
}
