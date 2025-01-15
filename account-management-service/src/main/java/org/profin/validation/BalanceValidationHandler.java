package org.profin.validation;


import org.profin.dto.TransactionDTO;
import org.profin.exception.ValidationException;
import org.profin.model.BankAccount;
import org.profin.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;


/**
 * This class handles the validation of a user's bank account balance during a transaction.
 * It is part of a validation chain that ensures the account has sufficient balance before proceeding.
 * The handler checks if the account exists and if there are enough funds for the transaction.
 */
@Component
public class BalanceValidationHandler extends ValidationHandler {

    private final BankAccountRepository bankAccountRepository;  // Repository for accessing bank account data

    /**
     * Constructor to initialize the BalanceValidationHandler with the necessary bank account repository.
     *
     * @param bankAccountRepository repository for accessing bank account data
     */
    @Autowired
    public BalanceValidationHandler(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    /**
     * Validates a transaction by checking the sender's bank account balance.
     * It checks if the account exists and if the balance is sufficient to cover the transaction amount.
     * If either check fails, a ValidationException is thrown.
     *
     * @param transactionDTO the transaction data transfer object containing transaction details
     * @throws ValidationException if the account is not found or if there are insufficient funds
     */
    @Override
    public void validate(TransactionDTO transactionDTO) throws ValidationException {
        // Retrieve the bank account using the provided sender account ID from the transaction DTO
        Optional<BankAccount> optionalAccount = bankAccountRepository.findById(transactionDTO.getIdSenderAccount());

        // Check if the bank account exists, throw exception if not found
        BankAccount account = optionalAccount.orElseThrow(() ->
                new ValidationException("Bank account not found for user " + transactionDTO.getUserId()));

        // Check if the account balance is sufficient for the transaction
        if (account.getBalance().compareTo(transactionDTO.getAmount()) < 0) {
            throw new ValidationException("Insufficient balance");
        }

        // If validation passes, pass control to the next handler in the chain
        super.validate(transactionDTO);
    }
}