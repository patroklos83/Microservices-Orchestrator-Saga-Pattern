package com.patroclos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.patroclos.common.dto.OrchestratorRequestDTO;
import com.patroclos.common.dto.OrchestratorResponseDTO;
import com.patroclos.common.dto.OrderRequestDTO;
import com.patroclos.common.dto.OrderResponseDTO;
import com.patroclos.common.enums.OrderStatus;
import com.patroclos.entitty.Order;
import com.patroclos.repository.OrderRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.LocalDateTime;

import org.modelmapper.*;
import org.slf4j.LoggerFactory;

@Service
public class OrderService {

	private static org.slf4j.Logger Logger = LoggerFactory.getLogger(OrderService.class);

	@Autowired
	private InventoryService inventoryService;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private Sinks.Many<OrchestratorRequestDTO> sink;

	@Autowired
	private ModelMapper modelMapper;

	public Mono<Order> createOrder(OrderRequestDTO orderRequestDTO) throws Exception{
		return this.orderRepository.save(this.convertToEntity(orderRequestDTO))
				.doOnNext(e -> orderRequestDTO.setOrderId(e.getId()))
				.doOnNext(e -> 
				{
					Logger.info("Order created with Pending Status");
					this.sink.tryEmitNext(getOrchestratorRequestDTO(orderRequestDTO));
				});
	}

	public Flux<OrderResponseDTO> getAllOrders() {
		return this.orderRepository.findAll().map(this::convertToDTO);
	}

	private Order convertToEntity(OrderRequestDTO dto) {
		Order order = modelMapper.map(dto, Order.class);

		inventoryService.getInventoryItem(order.getItemId())
		.map(i ->
		{
			if (i== null)
				throw new RuntimeException("Item not found");
			return i;
		})
		.doOnSuccess(c -> 
		{
			order.setPrice(c.getPrice());
			order.setId(dto.getOrderId());
			order.setStatus(OrderStatus.PENDING);	
			order.setCreated(LocalDateTime.now());
		})
		.block();

		return order;
	}

	private OrderResponseDTO convertToDTO(Order order){
		OrderResponseDTO dto = modelMapper.map(order, OrderResponseDTO.class);
		dto.setOrderId(order.getId());
		dto.setAmount(order.getPrice());		
		return dto;
	}

	public OrchestratorRequestDTO getOrchestratorRequestDTO(OrderRequestDTO orderRequestDTO) {
		OrchestratorRequestDTO requestDTO = new OrchestratorRequestDTO();
		requestDTO.setCustomerId(orderRequestDTO.getCustomerId());
		
		inventoryService.getInventoryItem(orderRequestDTO.getItemId())
		.map(i ->
		{
			if (i== null)
				throw new RuntimeException("Item not found");
			return i;
		})
		.doOnSuccess(c -> 
		{
			requestDTO.setAmount(c.getPrice());
			requestDTO.setOrderId(orderRequestDTO.getOrderId());
			requestDTO.setItemId(orderRequestDTO.getItemId());
			requestDTO.setCustomerId(orderRequestDTO.getCustomerId());
		})
		.block();
		
		return requestDTO;
	}
	
	public Mono<Void> updateOrder(final OrchestratorResponseDTO responseDTO){
        return this.orderRepository.findById(responseDTO.getOrderId())
                .doOnNext(p -> p.setStatus(responseDTO.getStatus()))
                .flatMap(this.orderRepository::save)
                .doOnSuccess(s -> Logger.info("Order updated with Status " + responseDTO.getStatus()))
                .then();
	}

}
