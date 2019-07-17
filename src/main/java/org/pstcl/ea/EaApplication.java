package org.pstcl.ea;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableAutoConfiguration(exclude = { HibernateJpaAutoConfiguration.class, SecurityAutoConfiguration.class,
		MongoAutoConfiguration.class, MongoDataAutoConfiguration.class, JpaRepositoriesAutoConfiguration.class,
		ThymeleafAutoConfiguration.class })
//@EnableJpaRepositories(basePackages = {"org.pstcl"})
@EnableAsync
@SpringBootApplication
public class EaApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		ApplicationContext applicationContext=	SpringApplication.run(EaApplication.class, args);
	}
}
