package org.profin.validation;



import org.profin.dto.TransactionDTO;
import org.profin.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * This class handles the validation of the transaction amount during the transaction process.
 * It ensures that the transaction amount is greater than zero before proceeding.
 * It is part of the validation chain and will throw an exception if the amount is invalid.
 */
@Component
public class TransactionAmountValidationHandler extends ValidationHandler {

    /**
     * Validates the transaction amount to ensure it is greater than zero.
     * If the transaction amount is invalid (less than or equal to zero), a ValidationException is thrown.
     *
     * @param transactionDTO the transaction data transfer object containing transaction details
     * @throws ValidationException if the transaction amount is less than or equal to zero
     */
    @Override
    public void validate(TransactionDTO transactionDTO) throws ValidationException {
        // Check if the transaction amount is greater than zero
        if (transactionDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Transaction amount must be greater than zero");
        }

        // If validation passes, pass control to the next handler in the chain
        super.validate(transactionDTO);
    }
}
