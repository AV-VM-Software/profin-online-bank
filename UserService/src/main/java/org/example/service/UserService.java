package org.example.service;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
                .orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setEmail(user.getEmail());
        existingUser.setUpdatedAt(new Date());
        return userRepository.save(existingUser);
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
}