package org.profin.mapper;

import lombok.RequiredArgsConstructor;
import org.profin.dto.TransactionDTO;
import org.profin.entity.Transaction;
import org.profin.repository.TransactionRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
/**
 * Component responsible for mapping between TransactionDTO and Transaction
 * entities. It provides methods to create or update Transaction entities
 * based on incoming TransactionDTO objects, and to convert Transaction
 * entities back to DTOs.
 */
@Component
@RequiredArgsConstructor
public class TransactionMapper {

    private final TransactionRepository transactionRepository;

    /**
     * Maps a TransactionDTO to a Transaction entity. If the DTO contains an ID,
     * it attempts to update an existing entity; otherwise, it creates a new
     * Transaction entity.
     *
     * @param transactionRequest the incoming TransactionDTO
     * @return a Mono of Transaction reflecting the saved or updated entity
     */
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
    /**
     * Converts a Transaction entity to a TransactionDTO.
     *
     * @param transaction the Transaction entity to convert
     * @return a TransactionDTO containing the same data as the entity
     */
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
    /**
     * Creates a new Transaction entity from a TransactionDTO and saves it to
     * the database.
     *
     * @param dto the DTO containing the transaction data
     * @return a Mono of Transaction reflecting the newly saved entity
     */
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
