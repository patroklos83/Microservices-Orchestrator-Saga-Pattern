package com.patroclos.inventory.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class ItemDTO {

    private UUID id;
    private Integer itemId;
    private Double price;
    private Integer stockAvailable;
}
