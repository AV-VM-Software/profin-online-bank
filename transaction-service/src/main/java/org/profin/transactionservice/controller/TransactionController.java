package org.profin.transactionservice.controller;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.profin.transactionservice.entity.Transaction;
import org.profin.transactionservice.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TransactionController {

    private final KafkaTemplate<String, Transaction> kafkaTemplate;
    private final TransactionService transactionService;

    @PutMapping("/createTestTransaction")
    public Mono<ResponseEntity<Transaction>> createTestTransaction() {
        return transactionService.saveTransaction(transactionService.buildTransefTransaction())
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PostConstruct
    public void checkKafkaConnection() {
        try {
            kafkaTemplate.send("transactions.pending", transactionService.buildTransefTransaction());
            log.info("Successfully connected to Kafka");
        } catch (Exception e) {
            log.error("Failed to connect to Kafka: {}", e.getMessage());
        }
    }
}
