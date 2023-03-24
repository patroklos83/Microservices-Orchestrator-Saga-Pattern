package com.patroclos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.patroclos.common.dto.OrderRequestDTO;
import com.patroclos.common.dto.OrderResponseDTO;
import com.patroclos.entitty.Order;
import com.patroclos.service.OrderService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService service;

    @PostMapping("/create")
    public Mono<Order> createOrder(OrderRequestDTO request) throws Exception{
    	return this.service.createOrder(request);
    }

    @GetMapping("/all")
    public Flux<OrderResponseDTO> getAllOrders(){
        return this.service.getAllOrders();
    }

}
