package org.profin.transactionservice.repository;

import org.profin.transactionservice.entity.Transaction;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends ReactiveCrudRepository<Transaction,Long> {
}
