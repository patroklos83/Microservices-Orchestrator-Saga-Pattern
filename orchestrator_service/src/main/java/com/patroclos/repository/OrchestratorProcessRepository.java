package com.patroclos.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.patroclos.entity.OrchestratorProcess;

import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface OrchestratorProcessRepository extends ReactiveCrudRepository<OrchestratorProcess, UUID> {

	Mono<OrchestratorProcess> findById(UUID orderId);
}
