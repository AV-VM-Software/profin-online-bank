package org.profin.controller;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.profin.dto.TransactionDTO;
import org.profin.entity.Transaction;
import org.profin.mapper.TransactionMapper;
import org.profin.repository.TransactionRepository;
import org.profin.service.TransactionService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * REST controller that manages incoming HTTP requests for transaction-related
 * operations. It provides endpoints to create new transactions, check Kafka
 * connectivity, and save transactions in development scenarios.
 */
@Slf4j
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final KafkaTemplate<String, TransactionDTO> kafkaTemplate;
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;

    /**
     * Endpoint to create a new transaction by mapping a TransactionDTO to a Transaction
     * entity, saving it, and then returning the saved TransactionDTO.
     *
     * @param transactionRequest the transaction model received from the request body
     * @return a Mono of TransactionDTO representing the saved transaction
     */
    @PostMapping("/createNewTransaction")
    public Mono<TransactionDTO> createNewTransaction(@Valid @RequestBody TransactionDTO transactionRequest) {
        return transactionMapper.mapFromDto(transactionRequest)
                .flatMap(transactionService::createNewTransaction)
                .map(savedTransaction -> {
                    log.info("Transaction saved and sent to kafka transactions.pending: {}", savedTransaction);
                    return transactionMapper.mapToTransactionDTO(savedTransaction);
                })
                .doOnSuccess(dto -> log.info("Transaction processed successfully: {}", dto.getId()))
                .doOnError(error -> log.error("Transaction processing failed: {}", error.getMessage()));
    }


    /**
     * Endpoint to verify Kafka connectivity by creating and sending a transaction
     * to a Kafka topic. Logs success or failure accordingly.
     */
    @PostMapping("/checkKafkaConnection")
    public void checkKafkaConnection() {
        try {
            transactionService.createNewTransaction(transactionService.buildTransefTransaction())
                    .map(transaction -> {
                        // Convert Transaction to TransactionDTO
                        TransactionDTO dto = TransactionDTO.builder()
                                .id(transaction.getId())
                                .amount(transaction.getAmount())
                                .idRecipientAccount(transaction.getIdRecipientAccount())
                                .idSenderAccount(transaction.getIdSenderAccount())
                                .paymentStatus(transaction.getPaymentStatus())
                                .transactionType(transaction.getTransactionType())
                                .build();

                        // Send to Kafka and return the transaction
                        kafkaTemplate.send("transactions.pending", dto);
                        return transaction;
                    })
                    .subscribe(
                            transaction -> log.info("Transaction processed and sent to Kafka: {}", transaction.getId()),
                            error -> log.error("Error processing transaction: {}", error.getMessage())
                    );

            log.info("Successfully connected to Kafka");
        } catch (Exception e) {
            log.error("Failed to connect to Kafka: {}", e.getMessage());
        }
    }

    //dev
    /**
     * Development endpoint to create and save a sample transaction directly.
     *
     * @return a Mono of Transaction regarding the newly created transaction
     */
    @PostMapping("/saveTransaction")
    public Mono<Transaction> saveTransaction() {
        return transactionService.createNewTransaction(transactionService.buildTransefTransaction());
    }
}
