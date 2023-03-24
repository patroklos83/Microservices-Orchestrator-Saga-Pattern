package com.patroclos.orchestration;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import com.patroclos.common.dto.InventoryRequestDTO;
import com.patroclos.common.dto.OrchestratorRequestDTO;
import com.patroclos.common.dto.OrchestratorResponseDTO;
import com.patroclos.common.dto.PaymentRequestDTO;
import com.patroclos.common.enums.OrderStatus;
import com.patroclos.enums.ProcessStatus;
import com.patroclos.enums.ProcessStepType;
import com.patroclos.orchestration.steps.InventoryStep;
import com.patroclos.orchestration.steps.PaymentStep;
import com.patroclos.orchestration.steps.ProcessStep;
import com.patroclos.orchestration.steps.ProcessStepStatus;
import com.patroclos.service.OrchestratorService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class OrderProcess extends Process {

	private static org.slf4j.Logger Logger = LoggerFactory.getLogger(OrderProcess.class);

	public OrderProcess(OrchestratorService service, PaymentRequestDTO paymentRequest, InventoryRequestDTO inventoryRequestDTO)
	{
		super(service);
		ProcessStep paymentStep = new PaymentStep(this.paymentClient, paymentRequest, ProcessStepType.Process);
		ProcessStep stockAdjustStep = new InventoryStep(this.inventoryClient, inventoryRequestDTO, ProcessStepType.Process);
		this.steps =  new LinkedList<ProcessStep>(List.of(paymentStep, stockAdjustStep));
	}

	@Override
	public Mono<OrchestratorResponseDTO> process(OrchestratorRequestDTO requestDTO){
		return Flux.fromStream(() -> this.steps.stream()) 
				.map(s ->
				{
					return s.process().block(); // wait for each step to finish before proceeding to the next
				})
				.handle(((Boolean, synchronousSink) -> {
					if(Boolean)
					{
						synchronousSink.next(true);
					}
					else
					{
						String error = "Process Failed";
						synchronousSink.error(new Exception(error));
					}
				}))
				.then(Mono.fromCallable(() -> 
				{
					this.status = ProcessStatus.COMPLETED;
					return orchestratorService.saveProcess(this, requestDTO); //success	
				}))
				.onErrorResume(ex -> 
				{
					ex.printStackTrace();				
					Logger.info("Process Rollback");
					this.status = ProcessStatus.FAILED;
					this.rollbackSteps = new LinkedList<ProcessStep>(this.steps);
					this.rollbackSteps = this.rollbackSteps.stream()
							.filter(s -> s.getStatus().equals(ProcessStepStatus.COMPLETE))
							.map(s -> s.copyStep())
							.collect(Collectors.toList());
					
					this.rollbackSteps.forEach(s -> s.setType(ProcessStepType.RollBack));
					
					var result = revert(this, requestDTO); 
					return result;
				});
	}

	@Override
	public Mono<OrchestratorResponseDTO> revert(final Process process, final OrchestratorRequestDTO requestDTO){
		return Flux.fromStream(() -> process.rollbackSteps.stream())
				.flatMap(ProcessStep::revert)
				.retry(3)
				.then(Mono.fromCallable(() -> 
				{
					return orchestratorService.saveProcess(this, requestDTO); //success	
				}))
				.then(Mono.just(getOnFailResponseDTO(requestDTO)))
				.doOnSuccess(s -> Logger.info("Saga Rollback Process Complete"));
	}

	public OrchestratorResponseDTO getOnSuccessResponseDTO(OrchestratorRequestDTO requestDTO)
	{	
		OrchestratorResponseDTO responseDTO = modelMapper.map(requestDTO, OrchestratorResponseDTO.class);		
		responseDTO.setStatus(OrderStatus.CREATED);
		return responseDTO;
	}

	public OrchestratorResponseDTO getOnFailResponseDTO(OrchestratorRequestDTO requestDTO)
	{
		OrchestratorResponseDTO responseDTO = modelMapper.map(requestDTO, OrchestratorResponseDTO.class);
    	responseDTO.setStatus(OrderStatus.CANCELLED);
		return responseDTO;
	}
}