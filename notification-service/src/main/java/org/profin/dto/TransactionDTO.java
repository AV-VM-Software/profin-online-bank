package org.profin.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class TransactionDTO {
    private Long id;

    private Long userId;

    private Long recipientId;
    private Long idSenderAccount;

    private Long idRecipientAccount; // UUID of recipient account null if transaction is not transfer

    private TransactionType transactionType; // Withdrawal, Deposit, Transfer

    private PaymentStatus paymentStatus; // Pending - on create, Processsing- putted on kafka topic, Completed, Failed

    private BigDecimal amount;    // Сумма транзакции (ex, 100.00)

}