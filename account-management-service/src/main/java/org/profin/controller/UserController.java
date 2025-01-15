package org.profin.controller;


import org.profin.dto.UserDTO;
import org.profin.dto.UserMapper;
import org.profin.dto.TransactionDTO;
import org.profin.model.User;
//import org.profin.service.TransactionProducer;
import org.profin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing user-related operations.
 * Provides endpoints to perform CRUD operations on users.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;  // Service to handle user-related business logic
    private final UserMapper userMapper;    // Mapper to convert between User entities and DTOs

    // Constructor-based injection of dependencies
    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    /**
     * Endpoint to get a list of all users.
     *
     * @return ResponseEntity containing a list of UserDTOs and an HTTP status.
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        // Fetch all users from the service
        List<User> users = userService.getAllUsers();

        // Convert the list of users to UserDTOs
        List<UserDTO> userDTOs = users.stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());

        // Return the list of UserDTOs with HTTP status 200 (OK)
        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

    /**
     * Endpoint to get a user by their ID.
     *
     * @param id the ID of the user to retrieve
     * @return ResponseEntity containing the UserDTO and an HTTP status, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        // Fetch the user by ID from the service
        User user = userService.getUserById(id);

        if (user != null) {
            // Return the found user as a DTO with HTTP status 200 (OK)
            return new ResponseEntity<>(userMapper.toDTO(user), HttpStatus.OK);
        } else {
            // If the user is not found, return HTTP status 404 (Not Found)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint to create a new user.
     *
     * @param userDTO the user data transfer object containing user information
     * @return ResponseEntity containing the created UserDTO and HTTP status 201 (Created)
     */
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        // Convert the UserDTO to a User entity
        User user = userMapper.toEntity(userDTO);

        // Create the user using the service
        User createdUser = userService.createUser(user);

        // Convert the created user back to a UserDTO
        UserDTO createdUserDTO = userMapper.toDTO(createdUser);

        // Return the created UserDTO with HTTP status 201 (Created)
        return new ResponseEntity<>(createdUserDTO, HttpStatus.CREATED);
    }

    /**
     * Endpoint to update an existing user.
     *
     * @param id the ID of the user to update
     * @param userDTO the new user data transfer object
     * @return ResponseEntity containing the updated UserDTO and an HTTP status, or 404 if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        // Convert the UserDTO to a User entity
        User user = userMapper.toEntity(userDTO);

        // Update the user using the service
        User updatedUser = userService.updateUser(id, user);

        if (updatedUser != null) {
            // Return the updated user as a DTO with HTTP status 200 (OK)
            return new ResponseEntity<>(userMapper.toDTO(updatedUser), HttpStatus.OK);
        } else {
            // If the user was not found, return HTTP status 404 (Not Found)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint to delete a user by their ID.
     *
     * @param id the ID of the user to delete
     * @return ResponseEntity with an HTTP status indicating the outcome
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        // Attempt to delete the user using the service
        boolean isDeleted = userService.deleteUser(id);

        if (isDeleted) {
            // If deletion was successful, return HTTP status 204 (No Content)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            // If the user was not found, return HTTP status 404 (Not Found)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    /*dev function for testing
     *  usually it goes from transaction service
     */
//    @PostMapping("/postTransaction")
//    public ResponseEntity<String> postTransaction(@RequestBody TransactionDTO transactionDTO) {
//        try {
//            // Отправляем транзакцию в Kafka через сервис
//            transactionProducer.sendTransactionToKafka(transactionDTO, "transactions.pending");
//            // Возвращаем успешный ответ
//            return ResponseEntity.ok("Transaction sent successfully");
//        } catch (Exception e) {
//            // Логируем ошибку и возвращаем ошибку
//            return ResponseEntity.status(500).body("Failed to send transaction: " + e.getMessage());
//        }
//    }

}