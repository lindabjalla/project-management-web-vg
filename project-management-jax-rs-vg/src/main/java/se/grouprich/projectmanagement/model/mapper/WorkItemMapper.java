package se.grouprich.projectmanagement.model.mapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import se.grouprich.projectmanagement.model.WorkItem;
import se.grouprich.projectmanagement.model.WorkItemData;

import javax.ws.rs.core.GenericEntity;
import java.util.ArrayList;
import java.util.Collection;

public final class WorkItemMapper
{
	private final MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
	private final MapperFacade mapper = mapperFactory.getMapperFacade();

	public WorkItemMapper()
	{
		mapperFactory.classMap(WorkItem.class, WorkItemData.class).exclude("user").byDefault().register();
	}

	public WorkItemData convertWorkItemToWorkItemData(final WorkItem workItem)
	{
		return mapper.map(workItem, WorkItemData.class);
	}

	public WorkItem convertWorkItemDataToWorkItem(final WorkItemData workItemData)
	{
		return mapper.map(workItemData, WorkItem.class);
	}

	public GenericEntity<Collection<WorkItem>> convertList(final Collection<WorkItemData> workItemDataList)
	{
		Collection<WorkItem> workItems = new ArrayList<>();
		workItemDataList.forEach(workItemData -> workItems.add(convertWorkItemDataToWorkItem(workItemData)));

		return new GenericEntity<Collection<WorkItem>>(workItems) {};
	}
}
