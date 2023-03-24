package com.patroclos.configuration;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.web.reactive.function.client.WebClient;

import io.r2dbc.spi.ConnectionFactory;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@Configuration
public class WebClientConfig {

	public static String INVENTORY_URL;
	public static String PAYMENT_URL;

	@Value("${service.endpoints.inventory}")
	public void setInventoryUrl(final String propertyName) {
		INVENTORY_URL = propertyName;
	}
	
	@Value("${service.endpoints.payment}")
	public void setPaymentUrl(final String propertyName) {
		PAYMENT_URL = propertyName;
	}


	@Bean
	public WebClient paymentClient(@Value("${service.endpoints.payment}") String endpoint){
		return WebClient.builder()
				.clientConnector(connector())
				.baseUrl(endpoint)
				.build();
	}

	@Bean
	public WebClient inventoryClient(@Value("${service.endpoints.inventory}") String endpoint){
		return WebClient.builder()
				.clientConnector(connector())
				.baseUrl(endpoint)
				.build();
	}


	public static WebClient paymentClient(){
		return WebClient.builder()
				.clientConnector(connector())
				.baseUrl(PAYMENT_URL)
				.build();
	}


	public static WebClient inventoryClient(){
		return WebClient.builder()
				.clientConnector(connector())
				.baseUrl(INVENTORY_URL)
				.build();
	}


	private static ClientHttpConnector connector() { 	 
		ConnectionProvider provider = ConnectionProvider
				.builder("webclient-conn-pool")
				.maxConnections(50)
				.maxIdleTime(Duration.ofSeconds(60 * 4))
				.maxLifeTime(Duration.ofSeconds(60))
				.pendingAcquireTimeout(Duration.ofSeconds(30))
				.pendingAcquireTimeout(Duration.ofMillis(30 * 100))                
				.build();

		HttpClient client = HttpClient.create(provider)
				//.wiretap(true); //log				
				.responseTimeout(Duration.ofSeconds(120)); 

		ReactorClientHttpConnector conn = new ReactorClientHttpConnector(client);

		return conn;
	}

	@Bean
	public DatabaseClient databaseClient(ConnectionFactory connectionFactory) {
		return DatabaseClient.builder()
				.connectionFactory(connectionFactory)
				.namedParameters(true)
				.build();
	}

}
