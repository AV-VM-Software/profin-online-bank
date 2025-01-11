package org.profin.accountservice.validation;

import org.profin.accountservice.dto.TransactionDTO;
import org.profin.accountservice.exception.ValidationException;

public abstract class ValidationHandler implements TransactionValidator {
    protected ValidationHandler nextHandler; // Следующий обработчик в цепочке

    public void setNextHandler(ValidationHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public void validate(TransactionDTO transaction) throws ValidationException {
        if (nextHandler != null) {
            nextHandler.validate(transaction); // Передача дальше по цепочке
        }
    }
}