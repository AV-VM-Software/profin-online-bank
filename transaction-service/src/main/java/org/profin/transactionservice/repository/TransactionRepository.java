package org.profin.transactionservice.repository;

import org.profin.transactionservice.entity.Transaction;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface TransactionRepository extends ReactiveCrudRepository<Transaction,Long> {

    @Modifying
    @Transactional
    @Query("INSERT INTO transactions (user_id, recipient_id, id_sender_account, id_recipient_account, transaction_type, payment_status, amount, created_at) " +
            "VALUES (:#{#transaction.userId}, :#{#transaction.recipientId}, :#{#transaction.idSenderAccount}, :#{#transaction.idRecipientAccount}, " +
            ":#{#transaction.transactionType}, :#{#transaction.paymentStatus}, :#{#transaction.amount}, :#{#transaction.createdAt}) " +
            "RETURNING *")
    Mono<Transaction> saveTransaction(Transaction transaction);

}
