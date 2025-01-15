package org.profin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String password;  // Лучше скрыть пароль или исключить его из вывода
    private Date createdAt;  // Время создания пользователя
    private Date updatedAt;  // Время последнего обновления

    // Список с балансами банковских счетов
    private List<BigDecimal> bankAccountBalances;
}