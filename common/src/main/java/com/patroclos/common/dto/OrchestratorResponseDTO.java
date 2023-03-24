package com.patroclos.common.dto;

import lombok.Data;
import java.util.UUID;

import com.patroclos.common.enums.OrderStatus;

@Data
public class OrchestratorResponseDTO {

    private Integer customerId;
    private UUID orderId;
    private Double amount;
    private OrderStatus status;

}
