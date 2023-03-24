package com.patroclos.orchestration;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.patroclos.common.dto.OrchestratorRequestDTO;
import com.patroclos.common.dto.OrchestratorResponseDTO;
import com.patroclos.configuration.GlobalModelMapper;
import com.patroclos.configuration.WebClientConfig;
import com.patroclos.enums.ProcessStatus;
import com.patroclos.orchestration.steps.ProcessStep;
import com.patroclos.service.OrchestratorService;

import reactor.core.publisher.Mono;

public abstract class Process {
	
	protected ModelMapper modelMapper = GlobalModelMapper.getModelMapper();

	protected OrchestratorService orchestratorService;
	protected WebClient paymentClient = WebClientConfig.paymentClient();
	protected WebClient inventoryClient =  WebClientConfig.inventoryClient();

	public ProcessStatus status = ProcessStatus.PENDING;
	public List<ProcessStep> steps = null;
	public List<ProcessStep> rollbackSteps = null;

	public abstract Mono<OrchestratorResponseDTO> revert(final Process process, final OrchestratorRequestDTO requestDTO);
	public abstract Mono<OrchestratorResponseDTO> process(OrchestratorRequestDTO requestDTO);
	public abstract OrchestratorResponseDTO getOnSuccessResponseDTO(OrchestratorRequestDTO requestDTO);
	public abstract OrchestratorResponseDTO getOnFailResponseDTO(OrchestratorRequestDTO requestDTO);

	public Process(OrchestratorService orchestratorService) {
		this.orchestratorService = orchestratorService;
	}
}