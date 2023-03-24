package com.patroclos.entity;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Table(name = "orchestratorProcessStep")
@Entity
public class OrchestratorProcessStep {
	
    @Id
    private UUID id;
    @Column("orchestratorProcess_id")
    private String orchestratorProcessId;
    @Column("stepType")
    private String stepType;
    private String name;
    private String error;
    @Column("statusStep")
    private String statusStep;
}
