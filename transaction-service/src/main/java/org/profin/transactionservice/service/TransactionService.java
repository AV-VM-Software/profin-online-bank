package org.profin.transactionservice.service;


import lombok.RequiredArgsConstructor;
import org.profin.transactionservice.entity.Currency;
import org.profin.transactionservice.entity.PaymentStatus;
import org.profin.transactionservice.entity.Transaction;
import org.profin.transactionservice.entity.TransactionType;
import org.profin.transactionservice.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionService {


    private final TransactionRepository transactionRepository;


    //todo use dto or map from dto on controller level
    private Mono<Transaction> saveTransaction(Transaction transaction) {
        return Mono.fromCallable(() -> {
            //save to db
            transactionRepository.save(transaction);
            return transaction;
        });
    }



    //dev mode
    private Transaction buildTransefTransaction() {
        return new Transaction().builder().userId(1L).
        recipientId(2L).
        amount(BigDecimal.valueOf(100.0)).
        uuidSenderAccount("uuidSenderAccount").
        uuidRecipientAccount("uuidRecipientAccount").
        transactionType(TransactionType.TRANSFER).
        paymentStatus(PaymentStatus.PENDING).
        createdAt(LocalDateTime.now()).
        currency(Currency.USD).
        build();

    }
}
