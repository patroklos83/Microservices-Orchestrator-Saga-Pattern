package com.patroclos;

import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import io.r2dbc.spi.ConnectionFactory;

@SpringBootApplication
@ComponentScan(basePackages = "com.patroclos.*")
public class SpringbootApplication {	
	
	private static org.slf4j.Logger Logger = LoggerFactory.getLogger(SpringbootApplication.class);

	public static void main(String[] args) {
		//System.setProperty("server.servlet.context-path", "/payment-service");
		SpringApplication.run(SpringbootApplication.class, args);
	} 
	
	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
		Logger.info("PAYMENT SERVICE STARTED");
	}

	@Bean
	public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
		ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
		initializer.setConnectionFactory(connectionFactory);
		initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
		return initializer;
	}
}
