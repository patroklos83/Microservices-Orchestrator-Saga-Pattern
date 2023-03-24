package com.patroclos.common.dto;

import lombok.Data;

import java.util.UUID;

import com.patroclos.common.enums.PaymentStatus;

@Data
public class PaymentResponseDTO {
    private Integer customerId;
    private UUID orderId;
    private Double amount;
    private PaymentStatus status;
}
