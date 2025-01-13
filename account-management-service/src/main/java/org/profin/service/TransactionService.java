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

@Service
@Slf4j
public class TransactionService {

    private final ValidationHandler validationChain;

    private final BankAccountRepository bankAccountRepository;

    @Autowired
    public TransactionService(UserRepository userRepository, BankAccountRepository bankAccountRepository, BankAccountRepository bankAccountRepository1) {
        // Формируем цепочку обработчиков
        this.validationChain = new UserExistenceValidationHandler(userRepository);
        this.bankAccountRepository = bankAccountRepository1;
        ValidationHandler balanceHandler = new BalanceValidationHandler(bankAccountRepository);
        ValidationHandler amountHandler = new TransactionAmountValidationHandler();

        // Настроим цепочку
        this.validationChain.setNextHandler(balanceHandler);
        balanceHandler.setNextHandler(amountHandler);
    }

    @Transactional
    public TransactionDTO processTransaction(TransactionDTO transactionDTO) throws Exception {
        // Валидация данных транзакции


        // В зависимости от типа транзакции вызываем соответствующий метод
        return switch (transactionDTO.getTransactionType()) {
            case DEPOSIT -> processDeposit(transactionDTO);
            case WITHDRAWAL -> processWithdrawal(transactionDTO);
            case TRANSFER -> processTransfer(transactionDTO);
            default ->
                    throw new ValidationException("Unsupported transaction type: " + transactionDTO.getTransactionType());
        };
    }

    // Метод для депозита (пополнение счета)
    private TransactionDTO processDeposit(TransactionDTO transactionDTO) {
        BankAccount recipientAccount = bankAccountRepository.findById(transactionDTO.getIdRecipientAccount())
                .orElseThrow(() -> new IllegalArgumentException("Recipient account not found"));

        BigDecimal amount = transactionDTO.getAmount();

        // Обновление баланса получателя
        recipientAccount.setBalance(recipientAccount.getBalance().add(amount));

        // Сохранение изменений в базе данных
        bankAccountRepository.save(recipientAccount);

        // Обновление статуса транзакции
        transactionDTO.setPaymentStatus(PaymentStatus.COMPLETED);

        log.info("Deposit completed successfully: " + transactionDTO);
        return transactionDTO;
    }

    // Метод для снятия средств
    private TransactionDTO processWithdrawal(TransactionDTO transactionDTO) {
        BankAccount senderAccount = bankAccountRepository.findById(transactionDTO.getIdSenderAccount())
                .orElseThrow(() -> new IllegalArgumentException("Sender account not found"));

        BigDecimal amount = transactionDTO.getAmount();

        // Проверка баланса отправителя
        if (senderAccount.getBalance().compareTo(amount) < 0) {
            transactionDTO.setPaymentStatus(PaymentStatus.FAILED);
            log.info("Insufficient funds for withdrawal: " + transactionDTO);
            return transactionDTO;
        }

        // Обновление баланса отправителя
        senderAccount.setBalance(senderAccount.getBalance().subtract(amount));

        // Сохранение изменений в базе данных
        bankAccountRepository.save(senderAccount);

        // Обновление статуса транзакции
        transactionDTO.setPaymentStatus(PaymentStatus.COMPLETED);

        log.info("Withdrawal completed successfully: " + transactionDTO);
        return transactionDTO;
    }

    // Метод для перевода средств между счетами
    private TransactionDTO processTransfer(TransactionDTO transactionDTO) {

        try {
            validationChain.validate(transactionDTO);
        } catch (ValidationException e) {
            transactionDTO.setPaymentStatus(PaymentStatus.FAILED);
            log.info("Transaction failed: " + e.getMessage());
            return transactionDTO;
        }

        BankAccount senderAccount = bankAccountRepository.findById(transactionDTO.getIdSenderAccount())
                .orElseThrow(() -> new IllegalArgumentException("Sender account not found"));
        BankAccount recipientAccount = bankAccountRepository.findById(transactionDTO.getIdRecipientAccount())
                .orElseThrow(() -> new IllegalArgumentException("Recipient account not found"));

        BigDecimal amount = transactionDTO.getAmount();

        // Проверка баланса отправителя
        if (senderAccount.getBalance().compareTo(amount) < 0) {
            transactionDTO.setPaymentStatus(PaymentStatus.FAILED);
            log.info("Insufficient funds for transfer: " + transactionDTO);
            return transactionDTO;
        }

        // Обновление баланса отправителя и получателя
        senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
        recipientAccount.setBalance(recipientAccount.getBalance().add(amount));

        // Сохранение изменений в базе данных
        bankAccountRepository.save(senderAccount); // Обновляет баланс отправителя
        bankAccountRepository.save(recipientAccount); // Обновляет баланс получателя

        // Обновление статуса транзакции
        transactionDTO.setPaymentStatus(PaymentStatus.COMPLETED);

        log.info("Transfer completed successfully: " + transactionDTO);
        return transactionDTO;
    }

}

