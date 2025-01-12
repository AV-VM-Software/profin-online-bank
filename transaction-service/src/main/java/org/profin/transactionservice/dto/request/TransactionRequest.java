package org.profin.transactionservice.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.profin.transactionservice.entity.PaymentStatus;
import org.profin.transactionservice.entity.TransactionType;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionRequest(
        @NotNull(message = "User ID cannot be null")
        Long userId,

        @NotNull(message = "Recipient ID cannot be null")
        Long recipientId,

        @NotNull(message = "Sender account ID cannot be null")
        Long idSenderAccount,

        @NotNull(message = "Recipient account ID cannot be null")
        Long idRecipientAccount,

        @NotNull(message = "Transaction type cannot be null")
        TransactionType transactionType,

        @NotNull(message = "Amount cannot be null")
        @Positive(message = "Amount must be positive")
        BigDecimal amount
) {}


