package org.profin.service;

import org.profin.model.BankAccount;
import org.profin.model.User;
import org.profin.repository.BankAccountRepository;
import org.profin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing users and their associated bank accounts.
 * This service provides methods for creating, updating, retrieving, and deleting users.
 * It also includes functionality for managing user-related bank accounts.
 */
@Service
public class UserService {

    private final UserRepository userRepository;  // Repository for managing user data
    private final BankAccountRepository bankAccountRepository;  // Repository for managing bank accounts
    private final TransactionService transactionService;  // Service for processing transactions (if needed)

    /**
     * Constructor to initialize the UserService with the necessary repositories and services.
     *
     * @param userRepository repository for accessing user data
     * @param bankAccountRepository repository for accessing bank account data
     * @param transactionService service for processing transactions (optional)
     */
    @Autowired
    public UserService(UserRepository userRepository, BankAccountRepository bankAccountRepository, TransactionService transactionService) {
        this.userRepository = userRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.transactionService = transactionService;
    }

    /**
     * Creates a new user and saves it to the database.
     * Sets the created and updated timestamps for the user.
     *
     * @param user the User entity to be created
     * @return the created User entity
     */
    public User createUser(User user) {
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        return userRepository.save(user);
    }

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email address of the user
     * @return an Optional containing the User if found, or an empty Optional if not found
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Updates an existing user's details.
     * This method updates the email and password of the user.
     *
     * @param userId the ID of the user to update
     * @param user the User entity containing the updated details
     * @return the updated User entity
     * @throws RuntimeException if the user is not found or if there is an error during saving
     */
    public User updateUser(Long userId, User user) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());

        try {
            return userRepository.save(existingUser);
        } catch (Exception e) {
            // Handle other possible errors, e.g., database save errors
            throw new RuntimeException("Error while updating user: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user
     * @return the User entity if found
     * @throws RuntimeException if the user is not found
     */
    public User getUserById(Long id) {
        try {
            return userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        } catch (RuntimeException e) {
            // LOGGER (could be added to log the exception)
            throw e;
        }
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to delete
     * @return true if the user was successfully deleted, false otherwise
     */
    public boolean deleteUser(Long id) {
        User user = getUserById(id);

        if (user != null) {
            userRepository.delete(user);
            return true;
        }

        return false;
    }

    /**
     * Retrieves all users from the database.
     *
     * @return a list of all User entities
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Checks whether a user exists by their ID.
     *
     * @param userId the ID of the user to check
     * @return true if the user exists, false otherwise
     */
    public boolean existsById(Long userId) {
        return userRepository.existsById(userId);
    }

    /**
     * Retrieves a bank account by its ID.
     *
     * @param accountId the ID of the bank account to retrieve
     * @return the BankAccount entity if found
     * @throws RuntimeException if the bank account is not found
     */
    public BankAccount getBankAccountById(Long accountId) {
        return bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Bank account not found with id: " + accountId));
    }
}