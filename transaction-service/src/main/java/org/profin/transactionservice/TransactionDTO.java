package org.profin.transactionservice;

import lombok.*;
import org.profin.transactionservice.entity.PaymentStatus;
import org.profin.transactionservice.entity.TransactionType;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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