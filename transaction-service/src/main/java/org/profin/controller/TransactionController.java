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

@Slf4j
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final KafkaTemplate<String, TransactionDTO> kafkaTemplate;
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;


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
    @PostMapping("/saveTransaction")
    public Mono<Transaction> saveTransaction() {
        return transactionService.createNewTransaction(transactionService.buildTransefTransaction());
    }
}
