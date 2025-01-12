package org.profin.accountservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private Long id;              // Уникальный идентификатор транзакции

    private Long userId;          // ID of sender
    private Long recipientId;     // ID of recipient null if transaction is not transfer

    private Long idSenderAccount; // UUID of sender account
    private Long idRecipientAccount; // UUID of recipient account null if transaction is not transfer


    private BigDecimal amount;    // Сумма транзакции (ex, 100.00)

//    private LocalDateTime createdAt; //beter than Date or might be bug
}
