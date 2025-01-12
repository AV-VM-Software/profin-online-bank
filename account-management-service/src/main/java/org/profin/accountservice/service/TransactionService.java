package org.profin.accountservice.service;

import org.profin.accountservice.dto.TransactionDTO;
import org.profin.accountservice.exception.ValidationException;
import org.profin.accountservice.repository.BankAccountRepository;
import org.profin.accountservice.repository.UserRepository;
import org.profin.accountservice.validation.BalanceValidationHandler;
import org.profin.accountservice.validation.TransactionAmountValidationHandler;
import org.profin.accountservice.validation.UserExistenceValidationHandler;
import org.profin.accountservice.validation.ValidationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final ValidationHandler validationChain;

    @Autowired
    public TransactionService(UserRepository userRepository, BankAccountRepository bankAccountRepository) {
        // Формируем цепочку обработчиков
        this.validationChain = new UserExistenceValidationHandler(userRepository);
        ValidationHandler balanceHandler = new BalanceValidationHandler(bankAccountRepository);
        ValidationHandler amountHandler = new TransactionAmountValidationHandler();

        // Настроим цепочку
        this.validationChain.setNextHandler(balanceHandler);
        balanceHandler.setNextHandler(amountHandler);
    }

    public void processTransaction(TransactionDTO transaction) throws ValidationException {
        // Запуск цепочки валидации
        validationChain.validate(transaction);

        // Логика обработки транзакции после успешной валидации
        // Например, сохранение транзакции в базе данных
        System.out.println("Transaction validated and processed: " + transaction);
    }


}

