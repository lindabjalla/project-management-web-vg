package se.grouprich.projectmanagement;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import se.grouprich.projectmanagement.exception.RepositoryException;
import se.grouprich.projectmanagement.exception.InvalidValueException;
import se.grouprich.projectmanagement.model.TeamData;
import se.grouprich.projectmanagement.service.TeamService;
import se.grouprich.projectmanagement.service.UserService;
import se.grouprich.projectmanagement.service.WorkItemService;

public final class Main
{
	public static void main(String[] args) throws RepositoryException, InvalidValueException
	{
		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext())
		{
			context.scan("se.grouprich.projectmanagement");
			context.refresh();
			UserService userService = context.getBean(UserService.class);
			TeamService teamService = context.getBean(TeamService.class);
			WorkItemService workItemService = context.getBean(WorkItemService.class);

//			UserData userData = new UserData("LambiLambiLambi", "iU23€%", "Lam", "Bi");
//			userService.createOrUpdate(userData);

//			TeamData team = new TeamData("Team2");
//			TeamData createdTeam = teamService.createOrUpdate(team);

			TeamData foundTeam = teamService.findById(5L);
			System.out.println(foundTeam);
			System.out.println(foundTeam.getUsers());

//			UserData userData = new UserData("SumireSumireSumire", "iU23€%", "Lam", "Bi");
//			UserData createdUser = userService.createOrUpdate(userData);
//			teamService.addUserToTeam(foundTeam, createdUser);
		}
	}
}
