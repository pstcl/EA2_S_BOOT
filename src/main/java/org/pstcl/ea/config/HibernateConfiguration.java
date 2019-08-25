package org.pstcl.ea.config;


import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan({"org.pstcl"})
@PropertySource({"classpath:application.properties"})
public class HibernateConfiguration {


	@Autowired
	private Environment environment;


	//SLDC SERVER CONFIGURATION
	//SLDC SERVER CONFIGURATION
	//SLDC SERVER CONFIGURATION: STARTS
	@Bean(name="sldcDataBase")
	public DataSource dataSourceSLDC() {
		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(this.environment.getRequiredProperty("jdbc.driverClassName"));
		dataSource.setUrl(this.environment.getRequiredProperty("jdbc.url.sldc"));
		dataSource.setUsername(this.environment.getRequiredProperty("jdbc.username"));
		dataSource.setPassword(this.environment.getRequiredProperty("jdbc.password"));
		return (DataSource) dataSource;
	}

	@Bean(name="sldcSessionFactory")
	public LocalSessionFactoryBean sessionFactorySLDC() {
		final LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(this.dataSourceSLDC());
		sessionFactory.setPackagesToScan(new String[]{"org.pstcl.ea.entity"});
		sessionFactory.setHibernateProperties(this.hibernateProperties());
		return sessionFactory;

	}
	


	private Properties hibernateProperties() {
		Properties properties = new Properties();
		properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
		properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
		properties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));
		properties.put("hibernate.hbm2ddl.auto", environment.getRequiredProperty("hibernate.hbm2ddl.auto"));
		return properties;        
	}

	
	
	

	
	@Bean(name="sldcTxnManager")
	@Autowired
	public HibernateTransactionManager transactionManagerSLDC(	@Qualifier(value="sldcSessionFactory") SessionFactory s) {
		final HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(s);
		return txManager;
	}


	//HIBERNATE SPECIFIC CONFIG ENDS
	
	//GENERIC JPA CONFIG STARTS
	
	

	
	
}