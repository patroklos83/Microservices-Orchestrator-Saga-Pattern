package com.patroclos.repository;

import java.util.UUID;
import com.patroclos.entity.Item;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Mono;

public interface ItemRepository extends ReactiveCrudRepository<Item, UUID> {

	Mono<Item> findById(UUID id);

}