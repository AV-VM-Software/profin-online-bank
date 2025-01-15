package org.profin.config;

import org.profin.model.BankAccount;
import org.profin.model.User;
import org.profin.repository.BankAccountRepository;
import org.profin.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * DataLoader class implements {@link CommandLineRunner} to load sample data into the database on startup.
 * This class is marked as a Spring {@link Component} and will be automatically discovered by Spring Boot.
 */
@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;

    /**
     * Constructor to inject dependencies for {@link UserRepository} and {@link BankAccountRepository}.
     *
     * @param userRepository a repository to interact with the user data.
     * @param bankAccountRepository a repository to interact with the bank account data.
     */
    public DataLoader(UserRepository userRepository, BankAccountRepository bankAccountRepository) {
        this.userRepository = userRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    /**
     * This method will be executed when the Spring Boot application starts. It initializes some
     * sample data by creating users and their associated bank accounts, then saves them to the database.
     *
     * @param args the command line arguments (not used in this method).
     * @throws Exception if any exception occurs during data loading.
     */
    @Override
    public void run(String... args) throws Exception {
        // Create users
        User user1 = new User(null, "user1@example.com", "password1", null, null, null);
        User user2 = new User(null, "user2@example.com", "password2", null, null, null);

        // Create bank accounts for the users
        BankAccount account1 = new BankAccount(null, BigDecimal.valueOf(1000), user1, null, null);
        BankAccount account2 = new BankAccount(null, BigDecimal.valueOf(2000), user1, null, null);
        BankAccount account3 = new BankAccount(null, BigDecimal.valueOf(1500), user2, null, null);
        BankAccount account4 = new BankAccount(null, BigDecimal.valueOf(2500), user2, null, null);

        // Link bank accounts to users
        user1.setBankAccounts(Arrays.asList(account1, account2));
        user2.setBankAccounts(Arrays.asList(account3, account4));

        // Save users and their bank accounts into the database
        userRepository.save(user1);
        userRepository.save(user2);
    }
}
