package com.patroclos.common.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class OrderRequestDTO {

    private UUID orderId;
	private Integer customerId;
    private UUID itemId;
}