package se.grouprich.projectmanagement;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/*")
public final class Loader extends Application
{
	private static AnnotationConfigApplicationContext context;

	public static <T> T getBean(Class<T> type)
	{
		return context.getBean(type);
	}

	@PostConstruct
	public void init()
	{
		context = new AnnotationConfigApplicationContext();
		context.scan("se.grouprich.projectmanagement");
		context.refresh();
	}

	@PreDestroy
	public void destroy()
	{
		context.destroy();
	}
}
