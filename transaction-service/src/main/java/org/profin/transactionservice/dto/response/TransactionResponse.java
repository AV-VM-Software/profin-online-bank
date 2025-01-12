package org.profin.transactionservice.dto.response;

import org.profin.transactionservice.entity.PaymentStatus;
import org.profin.transactionservice.entity.TransactionType;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse (
        Long id,
        Long userId,
        Long recipientId,
        Long idSenderAccount,
        Long idRecipientAccount,
        TransactionType transactionType,
        PaymentStatus paymentStatus,
        BigDecimal amount,
        LocalDateTime createdAt

){
}
