package org.profin.accountservice.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.*;
import org.profin.accountservice.dto.PaymentStatus;
import org.profin.accountservice.dto.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
