package com.patroclos.configuration;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient inventoryClient(@Value("${service.endpoints.inventory}") String endpoint){
        return WebClient.builder()
        		.clientConnector(connector())
                .baseUrl(endpoint)
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
    			.responseTimeout(Duration.ofSeconds(20)); 
    	 
        ReactorClientHttpConnector conn = new ReactorClientHttpConnector(client);
        
        return conn;
    }

}
