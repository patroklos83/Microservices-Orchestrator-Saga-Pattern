package com.patroclos.common.dto;

import lombok.Data;

import java.util.UUID;

import com.patroclos.common.enums.InventoryStatus;

@Data
public class InventoryResponseDTO {

    private UUID orderId;
    private Integer customerId;
    private UUID itemId;
    private InventoryStatus status;

}
