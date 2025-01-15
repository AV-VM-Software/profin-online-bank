package org.profin.validation;


import org.profin.dto.TransactionDTO;
import org.profin.exception.ValidationException;

/**
 * Abstract base class for implementing a chain of responsibility pattern for transaction validation.
 * Each concrete validation handler (e.g., user existence, balance validation) extends this class and
 * performs specific validation logic. The validation chain allows each handler to validate the transaction
 * and pass control to the next handler in the chain.
 */
public abstract class ValidationHandler implements TransactionValidator {

    // The next handler in the validation chain
    protected ValidationHandler nextHandler;

    /**
     * Sets the next handler in the validation chain.
     *
     * @param nextHandler the next validation handler to be executed after the current one
     */
    public void setNextHandler(ValidationHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    /**
     * Validates the given transaction. If the current handler has a next handler in the chain,
     * it delegates the validation to the next handler.
     * This method can be overridden by concrete handler classes to perform specific validation logic.
     *
     * @param transactionDTO the transaction data transfer object containing transaction details
     * @throws ValidationException if the validation fails in the current or next handler
     */
    @Override
    public void validate(TransactionDTO transactionDTO) throws ValidationException {
        // If there is a next handler in the chain, pass the transactionDTO to it for further validation
        if (nextHandler != null) {
            nextHandler.validate(transactionDTO);
        }
    }
}
