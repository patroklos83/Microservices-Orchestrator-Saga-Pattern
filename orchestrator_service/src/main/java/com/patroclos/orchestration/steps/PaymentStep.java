package com.patroclos.orchestration.steps;

import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.patroclos.SpringbootApplication;
import com.patroclos.common.dto.PaymentRequestDTO;
import com.patroclos.common.dto.PaymentResponseDTO;
import com.patroclos.common.enums.PaymentStatus;
import com.patroclos.enums.ProcessStepType;

import reactor.core.publisher.Mono;

public class PaymentStep extends ProcessStep {

	private static org.slf4j.Logger Logger = LoggerFactory.getLogger(SpringbootApplication.class);
    private final PaymentRequestDTO requestDTO;

    public PaymentStep(WebClient webClient, PaymentRequestDTO requestDTO, ProcessStepType type) {
        this.webClient = webClient;
        this.requestDTO = requestDTO;
        this.type = type;
    	this.status = ProcessStepStatus.PENDING;
    }
    
    @Override
    public ProcessStep copyStep() {
        return new PaymentStep(this.webClient, this.requestDTO, this.type);
     }

    @Override
    public Mono<Boolean> process() {
    	Logger.info("Calling API {/payment/debit}");
        return webClient
                    .post()
                    .uri("/payment/debitAccount")
                    .body(BodyInserters.fromValue(this.requestDTO))
                    .retrieve()
                    .bodyToMono(PaymentResponseDTO.class)
                    .doOnError(throwable -> 
                    {
                    	this.error = throwable.getMessage();
                    	this.status = ProcessStepStatus.FAILED;
                    })
                    .map(r -> r.getStatus().equals(PaymentStatus.APPROVED))
                    .doOnSuccess(b -> this.status = b ? ProcessStepStatus.COMPLETE : ProcessStepStatus.FAILED)
                    .doOnNext(b -> this.status = b ? ProcessStepStatus.COMPLETE : ProcessStepStatus.FAILED);
    }

    @Override
    public Mono<Boolean> revert() {
    	Logger.info("Calling API {/payment/credit}"); 
        return webClient
                .post()
                .uri("/payment/creditAccount")
                .body(BodyInserters.fromValue(this.requestDTO))
                .retrieve()
                .bodyToMono(Void.class)
                .doOnError(throwable -> 
                {
                	this.error = throwable.getMessage();
                	this.status = ProcessStepStatus.FAILED;
                })
                .map(r -> true)
                .doOnSuccess(b -> this.setStatus(ProcessStepStatus.COMPLETE))
                .onErrorReturn(false);
    }

}