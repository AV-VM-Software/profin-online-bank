package org.profin.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.profin.dto.ProceededTransactionDTO;
import org.profin.dto.TransactionDTO;
import org.profin.entity.PaymentStatus;
import org.profin.entity.Transaction;
import org.profin.entity.TransactionType;
import org.profin.mapper.TransactionMapper;
import org.profin.repository.TransactionRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
/**
 * Service class encapsulating business logic for handling transactions.
 * In addition to saving and updating transactions, it broadcasts them
 * to Kafka and listens for processed transactions from Kafka.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {


    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final KafkaTemplate<String, TransactionDTO> kafkaTemplate;

    /**
     * Saves a new Transaction to the database with a default payment status of PENDING,
     * then sends it to the appropriate Kafka topic.
     *
     * @param transaction the Transaction entity to be saved and sent
     * @return a Mono of Transaction representing the saved entity
     */
    public Mono<Transaction> createNewTransaction(Transaction transaction) {
        log.debug("TransactionService: Saving transaction: {}", transaction);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setPaymentStatus(PaymentStatus.PENDING);

        return transactionRepository.save(transaction)
                .map(savedTransaction -> {
                    // Convert to DTO
                    TransactionDTO dto = TransactionDTO.builder()
                            .id(savedTransaction.getId())
                            .userId(savedTransaction.getUserId())
                            .recipientId(savedTransaction.getRecipientId())
                            .idSenderAccount(savedTransaction.getIdSenderAccount())
                            .idRecipientAccount(savedTransaction.getIdRecipientAccount())
                            .transactionType(savedTransaction.getTransactionType())
                            .paymentStatus(savedTransaction.getPaymentStatus())
                            .amount(savedTransaction.getAmount())
                            .build();

                    // Send to Kafka
                    kafkaTemplate.send("transactions.pending", dto)
                            .whenComplete((result, ex) -> {
                                if (ex == null) {
                                    log.info("Transaction sent to Kafka: {} with offset: {}",
                                            dto.getId(),
                                            result.getRecordMetadata().offset());
                                } else {
                                    log.error("Failed to send transaction {} to Kafka: {}",
                                            dto.getId(),
                                            ex.getMessage());
                                }
                            });

                    return savedTransaction;
                })
                .doOnSuccess(savedTransaction ->
                        log.debug("TransactionService: Transaction saved and sent to Kafka: {}",
                                savedTransaction))
                .doOnError(throwable ->
                        log.error("TransactionService: Error processing transaction: {}",
                                throwable.getMessage()));
    }


    /**
     * Persists updates to an existing Transaction in the database.
     *
     * @param transaction the Transaction entity to update
     * @return a Mono of Transaction reflecting the updated entity
     */
    public Mono<Transaction> updateTransaction(Transaction transaction) {
        log.debug("TransactionService: Updating transaction: {}", transaction);

        return transactionRepository.save(transaction)
                .doOnSuccess(updatedTransaction ->
                        log.debug("TransactionService: Transaction updated: {}",
                                updatedTransaction))
                .doOnError(throwable ->
                        log.error("TransactionService: Error updating transaction: {}",
                                throwable.getMessage()));
    }
    /**
     * Asynchronously sends a TransactionDTO to a specified Kafka topic and
     * returns a CompletableFuture for handling the result.
     *
     * @param dto   the transaction data transfer object to send
     * @param topic the target Kafka topic
     * @return a CompletableFuture containing sending results
     */
    private CompletableFuture<SendResult<String, TransactionDTO>> sendTransactionToKafka(TransactionDTO dto, String topic) {
        return kafkaTemplate.send(topic, dto)
                .thenApply(result -> {
                    log.info("Transaction sent to Kafka: {} with offset: {}",
                            dto.getId(), result.getRecordMetadata().offset());
                    return result;
                })
                .exceptionally(ex -> {
                    log.error("Unable to send transaction {} to Kafka: {}",
                            dto.getId(), ex.getMessage());
                    throw new RuntimeException(ex);
                });
    }

    /**
     * Kafka listener for "transactions.processed" messages. Updates the corresponding
     * transaction in the database and then forwards a notification to another Kafka topic,
     * such as "transactions.notifications".
     *
     * @param proceededTransactionDTO the fully processed transaction DTO received from Kafka
     */
@KafkaListener(topics = "transactions.processed", groupId = "transaction-service")
public void listenForProcessedTransaction(ProceededTransactionDTO proceededTransactionDTO) {
    log.info("TransactionService: Received processed transaction: {}", proceededTransactionDTO);

    transactionRepository.findById(proceededTransactionDTO.getId())
            .flatMap(transaction -> {
                transaction.setPaymentStatus(proceededTransactionDTO.getPaymentStatus());
                return updateTransaction(transaction);
            })
            .flatMap(updatedTransaction -> {
                ProceededTransactionDTO dto = ProceededTransactionDTO.builder()
                        .id(updatedTransaction.getId())
                        .userId(updatedTransaction.getUserId())
                        .recipientId(updatedTransaction.getRecipientId())
                        .amount(updatedTransaction.getAmount())
                        .idRecipientAccount(updatedTransaction.getIdRecipientAccount())
                        .idSenderAccount(updatedTransaction.getIdSenderAccount())
                        .paymentStatus(updatedTransaction.getPaymentStatus())
                        .transactionType(updatedTransaction.getTransactionType())
                        .userEmail(proceededTransactionDTO.getUserEmail())
                        .recipientEmail(proceededTransactionDTO.getRecipientEmail())
                        .build();
                return Mono.fromFuture(sendTransactionToKafka(dto, "transactions.notifications"));
            })
            .subscribe(
                    result -> log.info("Transaction successfully processed and sent to notifications: {}", result),
                    error -> log.error("Error processing transaction: {}", error.getMessage()),
                    () -> log.info("Transaction processing completed")
            );
}




    /**
     * Development helper method for building a basic transaction object
     * withTRANSFER type and PENDING status.
     *
     * @return a new Transaction pre-populated with test data
     */
    public Transaction buildTransefTransaction() {
        return new Transaction().builder().userId(1L).
        recipientId(2L).
        amount(BigDecimal.valueOf(100.0)).
        idSenderAccount(1L).
        idRecipientAccount(2L).
        transactionType(TransactionType.TRANSFER).
        paymentStatus(PaymentStatus.PENDING).
        createdAt(LocalDateTime.now()).
                build();

    }


}
