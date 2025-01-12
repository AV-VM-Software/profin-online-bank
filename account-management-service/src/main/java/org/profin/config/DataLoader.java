package org.profin.config;

import org.profin.model.BankAccount;
import org.profin.model.User;
import org.profin.repository.BankAccountRepository;
import org.profin.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;

@Component  // Spring автоматически обнаружит этот класс и выполнит метод run
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;

    // Конструктор для внедрения зависимостей
    public DataLoader(UserRepository userRepository, BankAccountRepository bankAccountRepository) {
        this.userRepository = userRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Создаем пользователей
        User user1 = new User(null, "user1@example.com", "password1", null, null, null);
        User user2 = new User(null, "user2@example.com", "password2", null, null, null);

        // Создаем аккаунты для пользователей
        BankAccount account1 = new BankAccount(null, BigDecimal.valueOf(1000), user1, null, null);
        BankAccount account2 = new BankAccount(null, BigDecimal.valueOf(2000), user1, null, null);
        BankAccount account3 = new BankAccount(null, BigDecimal.valueOf(1500), user2, null, null);
        BankAccount account4 = new BankAccount(null, BigDecimal.valueOf(2500), user2, null, null);

        // Связываем аккаунты с пользователями
        user1.setBankAccounts(Arrays.asList(account1, account2));
        user2.setBankAccounts(Arrays.asList(account3, account4));

        // Сохраняем пользователей и их аккаунты в базу данных
        userRepository.save(user1);
        userRepository.save(user2);
    }
}