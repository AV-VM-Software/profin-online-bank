package org.profin.accountservice.validation;


import org.profin.accountservice.dto.TransactionDTO;
import org.profin.accountservice.exception.ValidationException;
import org.profin.accountservice.model.BankAccount;
import org.profin.accountservice.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class BalanceValidationHandler extends ValidationHandler {

    private final BankAccountRepository bankAccountRepository;

    // Инжектим BankAccountRepository
    @Autowired
    public BalanceValidationHandler(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public void validate(TransactionDTO transactionDTO) throws ValidationException {
        // Получаем BankAccount через репозиторий, который возвращает Optional
        Optional<BankAccount> optionalAccount = bankAccountRepository.findById(transactionDTO.getIdSenderAccount());

        // Проверка, что банковский счет найден
        BankAccount account = optionalAccount.orElseThrow(() ->
                new ValidationException("Bank account not found for user " + transactionDTO.getUserId()));

        // Проверка достаточности баланса
        if (account.getBalance().compareTo(transactionDTO.getAmount()) < 0) {
            throw new ValidationException("Insufficient balance");
        }

        // Передаем дальше по цепочке
        super.validate(transactionDTO);
    }
}