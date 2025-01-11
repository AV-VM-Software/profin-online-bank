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
    private Long userId;          // ID пользователя, связанного с транзакцией
    private BigDecimal amount;    // Сумма транзакции
    private String type;          // Тип транзакции (например, "DEBIT", "CREDIT")
    private String currency;      // Валюта (например, "USD", "EUR")
    private boolean isValid;      // Флаг валидации транзакции
    private Date createdAt;       // Время создания транзакции
}
