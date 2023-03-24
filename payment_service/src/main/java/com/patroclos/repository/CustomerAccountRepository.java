package com.patroclos.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.patroclos.entity.CustomerAccount;

import reactor.core.publisher.Mono;

public interface CustomerAccountRepository extends ReactiveCrudRepository<CustomerAccount, UUID> {

	Mono<CustomerAccount> findByCustomerId(Integer customerId);
}
