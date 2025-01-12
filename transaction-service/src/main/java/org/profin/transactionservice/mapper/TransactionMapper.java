package org.profin.transactionservice.mapper;

import lombok.RequiredArgsConstructor;
import org.profin.transactionservice.dto.TransactionDTO;
import org.profin.transactionservice.entity.Transaction;
import org.profin.transactionservice.repository.TransactionRepository;
import org.profin.transactionservice.service.TransactionService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TransactionMapper {

    private final TransactionRepository transactionRepository;
    private final TransactionService transactionService;


    public Mono<Transaction> mapFromDto(TransactionDTO transactionRequest) {
        if (transactionRequest.getId() != null) {
            // Update existing transaction
            return transactionRepository.findById(transactionRequest.getId())
                    .flatMap(existingTransaction -> {
                        // Update fields
                        existingTransaction.setPaymentStatus(transactionRequest.getPaymentStatus());
                        existingTransaction.setAmount(transactionRequest.getAmount());
                        existingTransaction.setIdRecipientAccount(transactionRequest.getIdRecipientAccount());
                        existingTransaction.setIdSenderAccount(transactionRequest.getIdSenderAccount());
                        existingTransaction.setTransactionType(transactionRequest.getTransactionType());
                        existingTransaction.setUserId(transactionRequest.getUserId());
                        existingTransaction.setRecipientId(transactionRequest.getRecipientId());

                        return transactionRepository.save(existingTransaction);
                    })
                    .switchIfEmpty(transactionService.createNewTransaction(transactionRequest));
        } else {
            // Create new transaction
            return transactionService.createNewTransaction(transactionRequest);
        }
    }

    public TransactionDTO mapToTransactionDTO(Transaction transaction) {
        return TransactionDTO.builder()
                .id(transaction.getId())
                .userId(transaction.getUserId())
                .recipientId(transaction.getRecipientId())
                .idSenderAccount(transaction.getIdSenderAccount())
                .idRecipientAccount(transaction.getIdRecipientAccount())
                .transactionType(transaction.getTransactionType())
                .paymentStatus(transaction.getPaymentStatus())
                .amount(transaction.getAmount())
                .build();
    }
}
