package se.grouprich.projectmanagement.model.mapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import se.grouprich.projectmanagement.model.Issue;
import se.grouprich.projectmanagement.model.IssueData;

public final class IssueMapper
{
	private final MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
	private final MapperFacade mapperFacade = mapperFactory.getMapperFacade();

	public IssueMapper()
	{
		mapperFactory.classMap(Issue.class, IssueData.class).exclude("workItem").byDefault().register();
	}
	
	public IssueData convertIssueToIssueData(final Issue issue)
	{
		return mapperFacade.map(issue, IssueData.class);
	}
	
	public Issue convertIssueDataToIssue(final IssueData issueData)
	{
		return mapperFacade.map(issueData, Issue.class);
	}
	
	public IssueData updateIssueData(final Issue issue, final IssueData issueData)
	{
		issueData.setDescription(issue.getDescription());
		return issueData;		
	}
}
