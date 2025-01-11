package org.profin.accountservice.validation;

import org.profin.accountservice.dto.TransactionDTO;
import org.profin.accountservice.exception.ValidationException;
import org.profin.accountservice.model.BankAccount;
import org.profin.accountservice.repository.BankAccountRepository;
import org.profin.accountservice.repository.UserRepository;
import org.profin.accountservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class BalanceValidationHandler extends ValidationHandler {

    private final UserService userService;

    public BalanceValidationHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void validate(TransactionDTO transaction) throws ValidationException {
        // Получаем BankAccount через UserService
        BankAccount account = userService.getBankAccountById(transaction.getUserId());

        // Проверка, что банковский счет найден
        if (account == null) {
            throw new ValidationException("Bank account not found for user " + transaction.getUserId());
        }

        // Проверка достаточности баланса
        if (account.getBalance().compareTo(transaction.getAmount()) < 0) {
            throw new ValidationException("Insufficient balance");
        }

        // Передаем дальше по цепочке
        super.validate(transaction);
    }
}