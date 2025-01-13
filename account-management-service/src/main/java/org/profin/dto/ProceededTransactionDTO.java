package org.profin.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProceededTransactionDTO {
    private Long id;

    private Long userId;

    //might be null
    private Long recipientId;
    private Long idSenderAccount;

    //might be null
    private Long idRecipientAccount; // UUID of recipient account null if transaction is not transfer

    private TransactionType transactionType; // Withdrawal, Deposit, Transfer

    private PaymentStatus paymentStatus; // Pending - on create, Processsing- putted on kafka topic, Completed, Failed

    private BigDecimal amount;    // Сумма транзакции (ex, 100.00)

    private String userEmail;
    //might be null
    private String recipientEmail;

}