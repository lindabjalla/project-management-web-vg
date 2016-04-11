package se.grouprich.projectmanagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories("se.grouprich.projectmanagement.repository")
@EnableTransactionManagement
public class AppTestConfig extends AppConfig
{
	@Bean(destroyMethod = "close")
	public DataSource dataSource()
	{
		HikariConfig config = new HikariConfig();

		config.setDriverClassName("org.h2.Driver");
		config.setJdbcUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=0");
		config.setUsername("sa");
		config.setPassword("");
		config.addDataSourceProperty("characterEncoding", "utf8");

		return new HikariDataSource(config);
	}

	@Bean
	public JpaVendorAdapter jpaVendorAdapter()
	{
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setDatabase(Database.H2);
		adapter.setGenerateDdl(true);
		adapter.setShowSql(true); //visar Sql-log

		return adapter;
	}
}
