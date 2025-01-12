package org.profin.mapper;

import lombok.RequiredArgsConstructor;
import org.profin.dto.TransactionDTO;
import org.profin.entity.Transaction;
import org.profin.repository.TransactionRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TransactionMapper {

    private final TransactionRepository transactionRepository;


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
                    .switchIfEmpty(createNewTransaction(transactionRequest));
        } else {
            // Create new transaction
            return createNewTransaction(transactionRequest);
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
    public Mono<Transaction> createNewTransaction(TransactionDTO dto) {
        Transaction newTransaction = Transaction.builder()
                .userId(dto.getUserId())
                .recipientId(dto.getRecipientId())
                .idSenderAccount(dto.getIdSenderAccount())
                .idRecipientAccount(dto.getIdRecipientAccount())
                .transactionType(dto.getTransactionType())
                .paymentStatus(dto.getPaymentStatus())
                .amount(dto.getAmount())
                .createdAt(LocalDateTime.now())
                .build();

        return transactionRepository.save(newTransaction);
    }
}
