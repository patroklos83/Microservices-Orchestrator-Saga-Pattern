package com.patroclos.orchestration.steps;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.patroclos.common.dto.InventoryResponseDTO;
import com.patroclos.common.enums.InventoryStatus;
import com.patroclos.enums.ProcessStepType;

import reactor.core.publisher.Mono;

public abstract class ProcessStep  {

	protected WebClient webClient;
	protected ProcessStepStatus status;
	protected ProcessStepType type;
	protected String error;
	//
	//	public abstract ProcessStepStatus getStatus();
	//	public abstract ProcessStepType getType();
	//	public abstract void setType(ProcessStepType type);
	//	public abstract String getError();
	public abstract Mono<Boolean> process();
	public abstract Mono<Boolean> revert();
	public abstract ProcessStep copyStep();

	public ProcessStepStatus getStatus() {
		return this.status;
	}

	public void setStatus(ProcessStepStatus status) {
		this.status = status;
	}

	public ProcessStepType getType() {
		return this.type;
	}

	public void setType(ProcessStepType type) {
		this.type = type;
	}

	public String getError() {
		return this.error;
	}
}