package org.profin.transactionservice.controller;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.profin.transactionservice.dto.request.TransactionRequest;
import org.profin.transactionservice.entity.Transaction;
import org.profin.transactionservice.repository.TransactionRepository;
import org.profin.transactionservice.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final KafkaTemplate<String, Transaction> kafkaTemplate;
    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;


    @PostMapping("/createNewTransaction")
    public void createNewTransaction(@Valid @RequestBody TransactionRequest transactionRequest) {
        try {
            transactionService.createNewTransaction(transactionService.buildFromRequest(transactionRequest)).doOnSuccess(
                    savedTransacation -> kafkaTemplate.send("transactions.pending", savedTransacation)
            ).subscribe();
            log.info("Successfully connected to Kafka");
        } catch (Exception e) {
            log.error("Failed to connect to Kafka: {}", e.getMessage());
        }
    }




    @PostMapping("/checkKafkaConnection")
    public void checkKafkaConnection() {
        try {
            transactionService.createNewTransaction(transactionService.buildTransefTransaction()).doOnSuccess(
                    savedTransacation -> kafkaTemplate.send("transactions.pending", savedTransacation)
            ).subscribe();
            log.info("Successfully connected to Kafka");
        } catch (Exception e) {
            log.error("Failed to connect to Kafka: {}", e.getMessage());
        }
    }
    @PostMapping("/saveTransaction")
    public Mono<Transaction> saveTransaction() {
        return transactionService.createNewTransaction(transactionService.buildTransefTransaction());
    }
}
