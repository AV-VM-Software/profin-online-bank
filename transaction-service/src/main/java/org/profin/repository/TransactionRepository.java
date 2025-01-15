package org.profin.repository;

import org.profin.entity.Transaction;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
/**
 * Reactive repository interface for performing CRUD operations on
 * Transaction entities. Utilizes R2DBC for non-blocking database
 * interactions.
 */
@Repository
public interface TransactionRepository extends ReactiveCrudRepository<Transaction,Long> {



}
