package org.profin.accountservice.validation;

import org.profin.accountservice.dto.TransactionDTO;
import org.profin.accountservice.exception.ValidationException;
import org.profin.accountservice.repository.UserRepository;
import org.profin.accountservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class UserExistenceValidationHandler extends ValidationHandler {

    private final UserRepository userRepository;

    // Инжектим UserRepository
    @Autowired
    public UserExistenceValidationHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void validate(TransactionDTO transaction) throws ValidationException {
        // Проверка существования пользователя с использованием репозитория
        if (!userRepository.existsById(transaction.getUserId())) {
            throw new ValidationException("User not found");
        }
        super.validate(transaction); // Передаем дальше по цепочке
    }
}
