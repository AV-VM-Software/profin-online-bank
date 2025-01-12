package org.profin.accountservice.validation;

import org.profin.accountservice.dto.request.KafkaTransaction;
import org.profin.accountservice.exception.ValidationException;

public interface TransactionValidator {
    void validate(KafkaTransaction transaction) throws ValidationException;
}
