package com.patroclos.common.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class OrchestratorRequestDTO {

    private Integer customerId;
    private UUID itemId;
    private UUID orderId;
    private Double amount;

}
