package org.profin.accountservice.validation;

import org.profin.accountservice.dto.TransactionDTO;
import org.profin.accountservice.exception.ValidationException;

import java.math.BigDecimal;

public class TransactionAmountValidationHandler extends ValidationHandler {

    @Override
    public void validate(TransactionDTO transaction) throws ValidationException {
        // Проверка допустимой суммы транзакции
        if (transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Transaction amount must be greater than zero");
        }
        super.validate(transaction); // Передаем дальше по цепочке
    }
}
