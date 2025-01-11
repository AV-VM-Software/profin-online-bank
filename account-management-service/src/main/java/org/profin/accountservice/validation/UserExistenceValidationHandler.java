package org.profin.accountservice.validation;

import org.profin.accountservice.dto.TransactionDTO;
import org.profin.accountservice.exception.ValidationException;
import org.profin.accountservice.service.UserService;

public class UserExistenceValidationHandler extends ValidationHandler {

    private final UserService userService;

    public UserExistenceValidationHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void validate(TransactionDTO transaction) throws ValidationException {
        // Проверка существования пользователя
        if (!userService.existsById(transaction.getUserId())) {
            throw new ValidationException("User not found");
        }
        super.validate(transaction); // Передаем дальше по цепочке
    }
}
