package com.patroclos.common.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

import com.patroclos.common.enums.OrderStatus;

@Data
public class OrderResponseDTO {

    private UUID orderId;
    private Integer customerId;
    private UUID itemId;
    private Double amount;
    private OrderStatus status;
    private LocalDateTime created;

}
