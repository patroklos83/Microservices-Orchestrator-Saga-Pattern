package com.patroclos.common.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class InventoryRequestDTO {

    private Integer customerId;
    private UUID itemId;
    private UUID orderId;

}
