package org.profin.validation;


import org.profin.dto.TransactionDTO;
import org.profin.exception.ValidationException;
import org.profin.repository.UserRepository;
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
    public void validate(TransactionDTO transactionDTO) throws ValidationException {
        // Проверка существования пользователя с использованием репозитория
        if (!userRepository.existsById(transactionDTO.getUserId())) {
            throw new ValidationException("User not found");
        }

        if (!userRepository.existsById(transactionDTO.getUserId())) {
            throw new ValidationException("Reciepent not found");
        }

        super.validate(transactionDTO); // Передаем дальше по цепочке
    }
}
