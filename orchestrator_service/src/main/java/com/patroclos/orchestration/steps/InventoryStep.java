package com.patroclos.orchestration.steps;

import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.patroclos.SpringbootApplication;
import com.patroclos.common.dto.InventoryRequestDTO;
import com.patroclos.common.dto.InventoryResponseDTO;
import com.patroclos.common.enums.InventoryStatus;
import com.patroclos.enums.ProcessStepType;

import reactor.core.publisher.Mono;

public class InventoryStep extends ProcessStep {
	
	private static org.slf4j.Logger Logger = LoggerFactory.getLogger(SpringbootApplication.class);
    private final InventoryRequestDTO requestDTO;

    public InventoryStep(WebClient webClient, InventoryRequestDTO requestDTO, ProcessStepType type) {
        this.webClient = webClient;
        this.requestDTO = requestDTO;
        this.type = type;
    	this.status = ProcessStepStatus.PENDING;
    }
    
    public InventoryStep copyStep() {
       return new InventoryStep(this.webClient, this.requestDTO, this.type);
    }

    @Override
    public Mono<Boolean> process() {
    	Logger.info("Calling API {/stock/deduct}");
    	return webClient
                .post()
                .uri("/stock/deduct")
                .body(BodyInserters.fromValue(this.requestDTO))
                .retrieve()
                .bodyToMono(InventoryResponseDTO.class)
                .doOnError(throwable -> 
                {
                	this.error = throwable.getMessage();
                	this.status = ProcessStepStatus.FAILED;
                })
                .map(r -> r.getStatus().equals(InventoryStatus.INSTOCK))
                .doOnSuccess(b -> this.status = b ? ProcessStepStatus.COMPLETE : ProcessStepStatus.FAILED)
                .doOnNext(b -> this.status = b ? ProcessStepStatus.COMPLETE : ProcessStepStatus.FAILED);
    }

    @Override
    public Mono<Boolean> revert() {
    	Logger.info("Calling API {/stock/add}");
        return webClient
                    .post()
                    .uri("/stock/add")
                    .body(BodyInserters.fromValue(this.requestDTO))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .doOnError(throwable -> 
                    {
                    	this.error = throwable.getMessage();
                    	this.status = ProcessStepStatus.FAILED;
                    })
                    .map(r ->true)
                    .doOnSuccess(b -> this.setStatus(ProcessStepStatus.COMPLETE))
                    .onErrorReturn(false);
    }
}