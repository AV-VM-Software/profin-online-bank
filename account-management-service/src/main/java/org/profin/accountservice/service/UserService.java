package org.profin.accountservice.service;

import org.profin.accountservice.exception.ValidationException;
import org.profin.accountservice.model.BankAccount;
import org.profin.accountservice.model.User;
import org.profin.accountservice.repository.BankAccountRepository;
import org.profin.accountservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;

    private final TransactionService transactionService;

    @Autowired
    public UserService(UserRepository userRepository, BankAccountRepository bankAccountRepository, TransactionService transactionService) {
        this.userRepository = userRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.transactionService = transactionService;
    }

    public User createUser(User user) {
        // Логика для создания пользователя
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        return userRepository.save(user);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User updateUser(Long userId, User user) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());

        try {
            return userRepository.save(existingUser);
        } catch (Exception e) {
            // Обработка других возможных ошибок, например, ошибки сохранения в базе данных
            throw new RuntimeException("Error while updating user: " + e.getMessage(), e);
        }
    }
    public User getUserById(Long id) {
        User user = null;
        try {
            user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        } catch (RuntimeException e) {
//           // LOGGER
        }

        return user;
    }

    public boolean deleteUser(Long id) {
        User user = getUserById(id);

        if (user != null) {
            userRepository.delete(user);
            return true;
        }

        return false;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean existsById(Long userId) {
        return userRepository.existsById(userId);
    }

    public BankAccount getBankAccountById(Long accountId) {
        // Поиск аккаунта по идентификатору с использованием репозитория
        return bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Bank account not found with id: " + accountId));
    }




}