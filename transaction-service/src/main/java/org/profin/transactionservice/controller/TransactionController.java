package org.profin.transactionservice.controller;
import lombok.RequiredArgsConstructor;
import org.profin.transactionservice.entity.Transaction;
import org.profin.transactionservice.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PutMapping("/createTestTransaction")
    public Mono<ResponseEntity<Transaction>> createTestTransaction() {
        return transactionService.saveTransaction(transactionService.buildTransefTransaction())
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
