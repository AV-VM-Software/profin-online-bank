package org.profin.validation;


import org.profin.dto.TransactionDTO;
import org.profin.exception.ValidationException;
import org.profin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * This class handles the validation of a user's existence during the transaction process.
 * It checks if both the sender and the recipient users exist in the system before proceeding with the transaction.
 * It is part of the validation chain and will throw an exception if any user is not found.
 */
@Component
public class UserExistenceValidationHandler extends ValidationHandler {

    private final UserRepository userRepository;  // Repository for managing user data

    /**
     * Constructor to initialize the UserExistenceValidationHandler with the necessary user repository.
     *
     * @param userRepository repository for accessing user data
     */
    @Autowired
    public UserExistenceValidationHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Validates the existence of a user (sender and recipient) before proceeding with the transaction.
     * It checks if both the sender and recipient users exist in the system by querying the user repository.
     * If either user is not found, a ValidationException is thrown.
     *
     * @param transactionDTO the transaction data transfer object containing transaction details
     * @throws ValidationException if the sender or recipient user is not found
     */
    @Override
    public void validate(TransactionDTO transactionDTO) throws ValidationException {
        // Check if the sender user exists
        if (!userRepository.existsById(transactionDTO.getUserId())) {
            throw new ValidationException("User not found");
        }

        // Check if the recipient user exists (this check may need to reference a different user ID depending on your design)
        if (!userRepository.existsById(transactionDTO.getIdRecipientAccount())) {
            throw new ValidationException("Recipient not found");
        }

        // If both users exist, pass control to the next handler in the chain
        super.validate(transactionDTO);
    }
}
