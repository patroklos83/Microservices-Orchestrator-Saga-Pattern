package com.patroclos.entity;

import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.patroclos.common.enums.OrderStatus;
import com.patroclos.enums.ProcessStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;

@Data
@ToString
@Table(name = "orchestratorProcess")
@Entity
public class OrchestratorProcess {

    @Id
    private UUID id;
    private ProcessStatus status;
    
}