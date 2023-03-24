package com.patroclos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.patroclos.dto.OrchestratorProcessDTO;
import com.patroclos.entity.OrchestratorProcess;
import com.patroclos.service.OrchestratorService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("orchestrator")
public class OrchestratorController {
	
	@Autowired
	private OrchestratorService orchestratorService;
	
	@GetMapping("/processes")
	public Flux<OrchestratorProcessDTO> getAllOrchestratorProcesses() {
		Flux<OrchestratorProcessDTO> processFlux =  orchestratorService.getOrchestratorProcesses();
		return processFlux;
	}
	
	@GetMapping("/process/{id}")
	public Mono<OrchestratorProcessDTO> getOrchestratorProcess(@PathVariable String id) {
		Mono<OrchestratorProcessDTO> processMono =  orchestratorService.getOrchestratorProcess(id);
		return processMono;
	}

}
