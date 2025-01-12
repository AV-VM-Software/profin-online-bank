package org.profin.accountservice.validation;

import org.profin.accountservice.dto.request.TransactionDTO;
import org.profin.accountservice.exception.ValidationException;

public interface TransactionValidator {
    void validate(TransactionDTO transactionDTO) throws ValidationException;
}
