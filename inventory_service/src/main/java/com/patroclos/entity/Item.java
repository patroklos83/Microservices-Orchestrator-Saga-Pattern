package com.patroclos.entity;

import lombok.Data;
import lombok.ToString;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@ToString
@Table("Items")
public class Item {

	@Id
    private UUID id;
    private Integer itemId;
    private Double price;
    @Column("stock_available")
    private Integer stockAvailable;
}
