package com.patroclos.eventhandler;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.patroclos.service.OrderService;

import reactor.core.publisher.Flux;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Configuration
public class OrderListener {
	
	private static org.slf4j.Logger Logger = LoggerFactory.getLogger(OrderListener.class);

    @Autowired
    private Flux<com.patroclos.common.dto.OrchestratorRequestDTO> flux;

   // @Autowired
   // private com.patroclos.service.OrderEventService orderEventService;
    
    @Autowired
    private OrderService orderService;

    @Bean
    public Supplier<Flux<com.patroclos.common.dto.OrchestratorRequestDTO>> supplier(){
        return () -> flux;
    };

    @Bean
    public Consumer<Flux<com.patroclos.common.dto.OrchestratorResponseDTO>> consumer(){
        return f -> f
                .doOnNext(m -> Logger.info("Consuming orchestrator event: " + m))
                .flatMap(responseDTO -> this.orderService.updateOrder(responseDTO))
                .subscribe();
    };

}
