package com.customer.config;

import java.beans.PropertyVetoException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration // Annotation tells Spring this is a configuration class
@EnableWebMvc // Annotation that imports the Spring MVC configuration from
				// WebMvcConfigurationSupport
@EnableTransactionManagement // Annotation for registering the necessary Spring components that power
								// annotation-driven transaction management
@ComponentScan(basePackages = "com.customer") // Annotation that tells Spring to scan the current package and all of its
												// sub-packages
@PropertySource("classpath:application.properties") // The configuration file/s to load
public class AppConfig implements WebMvcConfigurer{

	@Autowired
	private Environment env; // Special helper class provided by Spring. The Environment object, env, will
								// be loaded with the properties file from the annotation: @PropertySource

	private Logger logger = Logger.getLogger(getClass().getName()); // Helper util class for checking connection from
																	// aplication properties

	// define a bean for ViewResolver
	@Bean
	public ViewResolver viewResolver() {

		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();

		viewResolver.setPrefix("/WEB-INF/view/");
		viewResolver.setSuffix(".jsp");

		return viewResolver;
	}

	// This code reads the Environment env object to get the data. It then uses the data from env to set up
	// configs for JDBC and connection pooling @Bean
	public DataSource dataSource() {

		// create connection pool
		ComboPooledDataSource dataSource = new ComboPooledDataSource();

		// set the jdbc driver
		try {
			dataSource.setDriverClass("com.mysql.jdbc.Driver");
		} catch (PropertyVetoException exc) {
			throw new RuntimeException(exc);
		}

		// Just to make sure that app is reading the data
		logger.info("jdbc.url= " + env.getProperty("jdbc.url"));
		logger.info("jdbc.user= " + env.getProperty("jdbc.user"));

		// set database connection properties
		dataSource.setJdbcUrl(env.getProperty("jdbc.url"));
		dataSource.setUser(env.getProperty("jdbc.user"));
		dataSource.setPassword(env.getProperty("jdbc.password"));

		// set connection pool properties
		dataSource.setInitialPoolSize(Integer.parseInt(env.getProperty("connection.pool.initialPoolSize")));
		dataSource.setMinPoolSize(Integer.parseInt(env.getProperty("connection.pool.minPoolSize")));
		dataSource.setMaxPoolSize(Integer.parseInt(env.getProperty("connection.pool.maxPoolSize")));
		dataSource.setMaxIdleTime(Integer.parseInt(env.getProperty("connection.pool.maxIdleTime")));

		return dataSource;
	}
	
	
	private Properties getHibernateProperties() {
		
		// set hibernate properties
		Properties props = new Properties();
		
		props.getProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
		
		props.getProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
		
		return props;
	}
	
	// read environment property and convert to int
	public int getIntProperty(String propName) {
		
		String propValue=env.getProperty(propName);
		
		// converting to integer
		int intPropValue = Integer.parseInt(propValue);
		
		
		return intPropValue;
	}
	
	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		
		// create session factory
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		
		// set the properties
		sessionFactory.setDataSource(dataSource());
		sessionFactory.setPackagesToScan(env.getProperty("hibernate.packagesToScan"));
		sessionFactory.setHibernateProperties(getHibernateProperties());
		
		return sessionFactory;
	}
	
	//Method that configures the Hibernate transaction manager
	@Bean
	@Autowired
	public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
	
		// setup transaction manager based on session factory
		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(sessionFactory);
		
		return txManager;
	}
	
	//For using static web resources such as css, images, js etc. 
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
          .addResourceHandler("/resources/**")
          .addResourceLocations("/resources/"); 
    }	
	
}
