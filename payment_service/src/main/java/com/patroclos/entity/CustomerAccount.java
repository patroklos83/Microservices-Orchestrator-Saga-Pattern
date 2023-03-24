package com.patroclos.entity;

import java.util.UUID;

import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.ToString;
import nonapi.io.github.classgraph.json.Id;

@Data
@ToString
@Table("customerAccounts")
public class CustomerAccount {

    @Id
    private UUID id;
    private Integer customerId;
    private Double balance;
}
