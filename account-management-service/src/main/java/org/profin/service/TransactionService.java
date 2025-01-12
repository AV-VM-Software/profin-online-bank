package org.profin.service;

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
    public void processTransaction(TransactionDTO transactionDTO) throws ValidationException {
        // Валидация данных транзакции
        validationChain.validate(transactionDTO);

        // Проверка, что это перевод (Transfer)
        if (transactionDTO.getTransactionType() != TransactionType.TRANSFER) {
            throw new ValidationException("Unsupported transaction type: " + transactionDTO.getTransactionType());
        }

        // Получение счетов из базы данных
        BankAccount senderAccount = bankAccountRepository.findById(transactionDTO.getIdSenderAccount())
                .orElseThrow(() -> new IllegalArgumentException("Sender account not found"));
        BankAccount recipientAccount = bankAccountRepository.findById(transactionDTO.getIdRecipientAccount())
                .orElseThrow(() -> new IllegalArgumentException("Recipient account not found"));

        BigDecimal amount = transactionDTO.getAmount();

        // Проверка баланса отправителя
        if (senderAccount.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient funds on sender's account");
        }

        // Обновление баланса отправителя и получателя
        senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
        recipientAccount.setBalance(recipientAccount.getBalance().add(amount));

        // Сохранение изменений в базе данных
        bankAccountRepository.save(senderAccount); // Обновляет баланс отправителя
        bankAccountRepository.save(recipientAccount); // Обновляет баланс получателя

        // Обновление статуса транзакции
        transactionDTO.setPaymentStatus(PaymentStatus.COMPLETED);

        System.out.println("Transaction completed successfully: " + transactionDTO);
    }

}

