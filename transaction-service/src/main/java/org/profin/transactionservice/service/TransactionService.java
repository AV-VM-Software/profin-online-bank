package org.profin.transactionservice.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.profin.transactionservice.dto.TransactionDTO;
import org.profin.transactionservice.entity.PaymentStatus;
import org.profin.transactionservice.entity.Transaction;
import org.profin.transactionservice.entity.TransactionType;
import org.profin.transactionservice.mapper.TransactionMapper;
import org.profin.transactionservice.repository.TransactionRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {


    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final KafkaTemplate<String, TransactionDTO> kafkaTemplate;

    //todo use dto or map from dto on controller level
    //saves new transaction into db and send to kafka
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
    //async send transaction to kafka
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

    //kfka consumers
    @KafkaListener(topics = "transactions.processed", groupId = "account-service")
    public void listenForProcessedTransaction(Transaction transaction) {
        log.info("TransactionService: Received processed transaction: {}", transaction);

        updateTransaction(transaction)
                .flatMap(updatedTransaction -> {
                    // Преобразуем CompletableFuture в Mono
                    return Mono.fromFuture(sendTransactionToKafka(transactionMapper.mapToTransactionDTO(updatedTransaction), "transactions.notifications"));
                })
                .subscribe(result -> log.info("Transaction sent to Kafka: {}", result),
                        error -> log.error("Error sending transaction to Kafka: {}", error.getMessage()));
    }




    //dev mode
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
