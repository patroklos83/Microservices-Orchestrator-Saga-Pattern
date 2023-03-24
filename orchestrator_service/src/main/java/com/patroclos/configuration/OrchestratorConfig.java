package com.patroclos.configuration;

import java.util.function.Function;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.patroclos.common.dto.InventoryRequestDTO;
import com.patroclos.common.dto.OrchestratorRequestDTO;
import com.patroclos.common.dto.OrchestratorResponseDTO;
import com.patroclos.common.dto.PaymentRequestDTO;
import com.patroclos.orchestration.OrderProcess;
import com.patroclos.service.OrchestratorService;
import reactor.core.publisher.Flux;

@Configuration
public class OrchestratorConfig {

	@Autowired
	private OrchestratorService orchestratorService;
	
	private ModelMapper modelMapper = GlobalModelMapper.getModelMapper();
	
	@Bean
	public Function<Flux<OrchestratorRequestDTO>, Flux<OrchestratorResponseDTO>> processor(){
		return flux -> flux.flatMap(dto ->
		{
			OrderProcess process = new OrderProcess(orchestratorService, 
					modelMapper.map(dto, PaymentRequestDTO.class), 
					modelMapper.map(dto, InventoryRequestDTO.class));
			
			return process.process(dto);
		})
		.doOnNext(dto -> System.out.println("Status : " + dto.getStatus()));
	}

}
