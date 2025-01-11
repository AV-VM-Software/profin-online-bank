package org.profin.transactionservice.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@Table(name = "transactions")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;              // Уникальный идентификатор транзакции
    private Long userId;          // ID of sender
    private Long recipientId;     // ID of recipient


    private BigDecimal amount;    // Сумма транзакции (ex, 100.00)
    private String type;          // Тип транзакции (например, "DEBIT", "CREDIT")?
    private String currency;      // Валюта (например, "USD", "EUR", "CZK")

    private boolean isValid;      // Флаг валидации транзакции ???
    private Date createdAt;


}
