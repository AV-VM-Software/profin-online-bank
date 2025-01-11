package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  // Связь с пользователем
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;  // Сумма транзакции

    @Column(name = "transaction_date", nullable = false, updatable = false)
    private LocalDateTime transactionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransactionStatus status;  // Статус транзакции

    @PrePersist
    protected void onCreate() {
        this.transactionDate = LocalDateTime.now();  // Устанавливаем время транзакции
    }

    // Статус транзакции
    public enum TransactionStatus {
        PENDING,  // Ожидает обработки
        COMPLETED,  // Завершена успешно
        FAILED  // Ошибка при выполнении
    }
}
