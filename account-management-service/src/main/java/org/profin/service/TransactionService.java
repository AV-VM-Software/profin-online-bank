package org.profin.service;

import lombok.extern.slf4j.Slf4j;
import org.profin.dto.PaymentStatus;
import org.profin.dto.TransactionType;
import org.profin.dto.TransactionDTO;
import org.profin.exception.ValidationException;
import org.profin.model.BankAccount;
import org.profin.repository.BankAccountRepository;
import org.profin.repository.UserRepository;
import org.profin.validation.BalanceValidationHandler;
import org.profin.validation.TransactionAmountValidationHandler;
import org.profin.validation.UserExistenceValidationHandler;
import org.profin.validation.ValidationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Service class for processing financial transactions.
 * This service handles different types of transactions such as deposits, withdrawals, and transfers.
 * It includes validation and updates to the bank account balances accordingly.
 */
@Service
@Slf4j
public class TransactionService {

    private final ValidationHandler validationChain;  // Chain of validation handlers
    private final BankAccountRepository bankAccountRepository;  // Repository for managing bank account data

    /**
     * Constructor that sets up the validation chain and repository.
     * The validation chain is used to validate transactions before processing them.
     *
     * @param userRepository repository for accessing user data
     * @param bankAccountRepository repository for accessing bank account data
     */
    @Autowired
    public TransactionService(UserRepository userRepository, BankAccountRepository bankAccountRepository, BankAccountRepository bankAccountRepository1) {
        // Forming the validation chain for processing transactions
        this.validationChain = new UserExistenceValidationHandler(userRepository);
        this.bankAccountRepository = bankAccountRepository1;

        // Setting up additional validation handlers for transaction processing
        ValidationHandler balanceHandler = new BalanceValidationHandler(bankAccountRepository);
        ValidationHandler amountHandler = new TransactionAmountValidationHandler();

        // Connecting the validation handlers to form a chain of responsibility
        this.validationChain.setNextHandler(balanceHandler);
        balanceHandler.setNextHandler(amountHandler);
    }

    /**
     * Processes a financial transaction.
     * This method validates the transaction data and processes it based on the type of transaction.
     *
     * @param transactionDTO the transaction data transfer object containing the transaction details
     * @return the updated TransactionDTO after processing
     * @throws Exception if any validation or processing step fails
     */
    @Transactional
    public TransactionDTO processTransaction(TransactionDTO transactionDTO) throws Exception {
        // Perform validation before processing the transaction
        // If validation fails, an exception will be thrown

        // Process transaction based on its type (Deposit, Withdrawal, or Transfer)
        return switch (transactionDTO.getTransactionType()) {
            case DEPOSIT -> processDeposit(transactionDTO);
            case WITHDRAWAL -> processWithdrawal(transactionDTO);
            case TRANSFER -> processTransfer(transactionDTO);
            default -> throw new ValidationException("Unsupported transaction type: " + transactionDTO.getTransactionType());
        };
    }

    /**
     * Handles the deposit transaction type.
     * Adds funds to the recipient's bank account and updates the transaction status.
     *
     * @param transactionDTO the transaction data
     * @return the updated TransactionDTO with the completed status
     */
    private TransactionDTO processDeposit(TransactionDTO transactionDTO) {
        // Retrieve recipient bank account
        BankAccount recipientAccount = bankAccountRepository.findById(transactionDTO.getIdRecipientAccount())
                .orElseThrow(() -> new IllegalArgumentException("Recipient account not found"));

        // Add the deposit amount to the recipient's balance
        BigDecimal amount = transactionDTO.getAmount();
        recipientAccount.setBalance(recipientAccount.getBalance().add(amount));

        // Save the updated account information to the database
        bankAccountRepository.save(recipientAccount);

        // Update the transaction status to "COMPLETED"
        transactionDTO.setPaymentStatus(PaymentStatus.COMPLETED);

        log.info("Deposit completed successfully: " + transactionDTO);
        return transactionDTO;
    }

    /**
     * Handles the withdrawal transaction type.
     * Deducts funds from the sender's account if there are sufficient funds and updates the transaction status.
     *
     * @param transactionDTO the transaction data
     * @return the updated TransactionDTO with the payment status
     */
    private TransactionDTO processWithdrawal(TransactionDTO transactionDTO) {
        // Retrieve sender bank account
        BankAccount senderAccount = bankAccountRepository.findById(transactionDTO.getIdSenderAccount())
                .orElseThrow(() -> new IllegalArgumentException("Sender account not found"));

        BigDecimal amount = transactionDTO.getAmount();

        // Check if sender has sufficient funds for the withdrawal
        if (senderAccount.getBalance().compareTo(amount) < 0) {
            // Insufficient funds, set payment status to FAILED
            transactionDTO.setPaymentStatus(PaymentStatus.FAILED);
            log.info("Insufficient funds for withdrawal: " + transactionDTO);
            return transactionDTO;
        }

        // Deduct the withdrawal amount from the sender's account
        senderAccount.setBalance(senderAccount.getBalance().subtract(amount));

        // Save the updated sender account information to the database
        bankAccountRepository.save(senderAccount);

        // Update the transaction status to "COMPLETED"
        transactionDTO.setPaymentStatus(PaymentStatus.COMPLETED);

        log.info("Withdrawal completed successfully: " + transactionDTO);
        return transactionDTO;
    }

    /**
     * Handles the transfer transaction type.
     * Transfers funds between the sender's and recipient's accounts, ensuring sufficient funds are available.
     *
     * @param transactionDTO the transaction data
     * @return the updated TransactionDTO with the completed status or failed if validation fails
     */
    private TransactionDTO processTransfer(TransactionDTO transactionDTO) {

        try {
            // Validate the transaction using the validation chain
            validationChain.validate(transactionDTO);
        } catch (ValidationException e) {
            // If validation fails, set payment status to FAILED
            transactionDTO.setPaymentStatus(PaymentStatus.FAILED);
            log.info("Transaction failed: " + e.getMessage());
            return transactionDTO;
        }

        // Retrieve sender and recipient bank accounts
        BankAccount senderAccount = bankAccountRepository.findById(transactionDTO.getIdSenderAccount())
                .orElseThrow(() -> new IllegalArgumentException("Sender account not found"));
        BankAccount recipientAccount = bankAccountRepository.findById(transactionDTO.getIdRecipientAccount())
                .orElseThrow(() -> new IllegalArgumentException("Recipient account not found"));

        BigDecimal amount = transactionDTO.getAmount();

        // Check if sender has sufficient funds for the transfer
        if (senderAccount.getBalance().compareTo(amount) < 0) {
            // Insufficient funds, set payment status to FAILED
            transactionDTO.setPaymentStatus(PaymentStatus.FAILED);
            log.info("Insufficient funds for transfer: " + transactionDTO);
            return transactionDTO;
        }

        // Deduct the amount from the sender's account and add to the recipient's account
        senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
        recipientAccount.setBalance(recipientAccount.getBalance().add(amount));

        // Save the updated account information for both sender and recipient
        bankAccountRepository.save(senderAccount);
        bankAccountRepository.save(recipientAccount);

        // Update the transaction status to "COMPLETED"
        transactionDTO.setPaymentStatus(PaymentStatus.COMPLETED);

        log.info("Transfer completed successfully: " + transactionDTO);
        return transactionDTO;
    }

}

