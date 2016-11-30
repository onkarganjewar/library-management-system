package edu.sjsu.cmpe275.project.configuration;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Onkar Ganjewar
 */
@Configuration
@EnableTransactionManagement
@PropertySource(value = { "classpath:application.properties" })
@ComponentScan(basePackages = { "edu.sjsu.cmpe275.project" })
public class HibernateConfigTest {

	@Autowired
	private Environment env;

	@Bean(name = "dataSource")
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getRequiredProperty("jdbc.driverClassName"));
		dataSource.setUrl(env.getRequiredProperty("jdbc.url"));
		dataSource.setUsername(env.getRequiredProperty("jdbc.username"));
		dataSource.setPassword(env.getRequiredProperty("jdbc.password"));
		return dataSource;
	}

	@Bean(name = "sessionFactory")
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		sessionFactory.setPackagesToScan(new String[] { "edu.sjsu.cmpe275.project.model" });
		sessionFactory.setHibernateProperties(hibernateProperties());
		return sessionFactory;
	}

	@Bean(name = "transactionManager")
	public HibernateTransactionManager transactionManager(SessionFactory s) {
		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(s);
		return txManager;
	}

	@SuppressWarnings("serial")
	private Properties hibernateProperties() {
		return new Properties() {
			{
				setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
				setProperty("hibernate.enable_lazy_load_no_trans",
						env.getProperty("hibernate.enable_lazy_load_no_trans"));
				setProperty("hibernate.connection.charSet", env.getProperty("hibernate.connection.charSet"));
				setProperty("hibernate.dialect", env.getRequiredProperty("hibernate.dialect"));
				setProperty("hibernate.show_sql", env.getRequiredProperty("hibernate.show_sql"));
				setProperty("hibernate.format_sql", env.getRequiredProperty("hibernate.format_sql"));
				setProperty("hibernate.ejb.naming_strategy", env.getRequiredProperty("hibernate.ejb.naming_strategy"));
			}
		};
	}
}
