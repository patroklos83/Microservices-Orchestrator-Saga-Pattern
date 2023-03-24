package com.patroclos.common.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PaymentRequestDTO {
    private Integer customerId;
    private UUID orderId;
    private Double amount;
}
