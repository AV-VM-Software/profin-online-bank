package org.profin.accountservice.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.profin.accountservice.dto.PaymentStatus;
import org.profin.accountservice.dto.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@Getter
@Setter
public class KafkaTransaction {
    private Long id;

    private Long userId;

    private Long recipientId;
    private Long idSenderAccount;

    private Long idRecipientAccount; // UUID of recipient account null if transaction is not transfer

    private TransactionType transactionType; // Withdrawal, Deposit, Transfer

    private PaymentStatus paymentStatus; // Pending - on create, Processsing- putted on kafka topic, Completed, Failed

    private BigDecimal amount;    // Сумма транзакции (ex, 100.00)

    private LocalDateTime createdAt; //beter than Date or might be bug
}
