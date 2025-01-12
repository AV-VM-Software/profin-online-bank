package org.profin.validation;

import org.profin.dto.TransactionDTO;
import org.profin.exception.ValidationException;

public interface TransactionValidator {
    void validate(TransactionDTO transactionDTO) throws ValidationException;
}
