package org.profin.transactionservice.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.profin.transactionservice.entity.PaymentStatus;
import org.profin.transactionservice.entity.Transaction;
import org.profin.transactionservice.entity.TransactionType;
import org.profin.transactionservice.repository.TransactionRepository;
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

    private final KafkaTemplate<String, Transaction> kafkaTemplate;

    //todo use dto or map from dto on controller level
    public Mono<Transaction> saveTransaction(Transaction transaction) {
        log.debug("TransactionService: Saving transaction: {}", transaction);
        return transactionRepository.save(transaction).doOnSuccess(
                savedTransacation -> log.debug("TransactionService: Transaction saved: {}", savedTransacation)
        ).doOnError(
                throwable ->
                        log.error("TransactionService: Error saving transaction: {}", throwable.getMessage())

        );
    }

    public void sendTransaction(Transaction transaction) {
        kafkaTemplate.send("transactions.pending", transaction);
        log.info("Transaction sent to Kafka: {}", transaction.getId());
    }
    public void processTransaction(Transaction transaction) {
        // Using HashCode as key for Kafka message to ensure that the same transaction is not processed twice
        String key = String.format("TRANS_%d_%s",
                transaction.getId(),
                transaction.getCreatedAt()
        );
        CompletableFuture<SendResult<String, Transaction>> future =
                kafkaTemplate.send("transactions.pending", transaction);


        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Transaction sent to Kafka: {}", transaction.getId());
                //todo update status and save to db
            } else {
                log.error("Error sending transaction to Kafka: {}", ex.getMessage());
            }
        });
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
