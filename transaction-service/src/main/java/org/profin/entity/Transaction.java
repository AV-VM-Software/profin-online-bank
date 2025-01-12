package org.profin.entity;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Table(name = "transactions")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @Column("id")
    private Long id;              // Уникальный идентификатор транзакции

    @Column("user_id")
    private Long userId;
    @Column("recipient_id")// ID of sender
    private Long recipientId;     // ID of recipient null if transaction is not transfer
    @Column("id_sender_account")
    private Long idSenderAccount; // UUID of sender account
    @Column("id_recipient_account")
    private Long idRecipientAccount; // UUID of recipient account null if transaction is not transfer
    @Column("transaction_type")
    private TransactionType transactionType; // Withdrawal, Deposit, Transfer
    @Column("payment_status")
    private PaymentStatus paymentStatus; // Pending - on create, Processsing- putted on kafka topic, Completed, Failed

    private BigDecimal amount;    // Сумма транзакции (ex, 100.00)

    @Column("created_at")
    private LocalDateTime createdAt; //beter than Date or might be bug

}
