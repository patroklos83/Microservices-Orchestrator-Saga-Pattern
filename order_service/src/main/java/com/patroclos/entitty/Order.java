package com.patroclos.entitty;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.patroclos.common.enums.OrderStatus;

@Data
@ToString
@Table("Orders")
public class Order {

    @Id
    private UUID id;
    private Integer customerId;
    private UUID itemId;
    private Double price;
    private OrderStatus status;
    private LocalDateTime created;

}