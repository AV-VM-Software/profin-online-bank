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

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

//    private final TransactionProducer transactionProducer;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
//        this.transactionProducer = transactionProducer;
    }

    // Получение списка пользователей
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserDTO> userDTOs = users.stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

    // Получение пользователя по ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return new ResponseEntity<>(userMapper.toDTO(user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Создание нового пользователя
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(userMapper.toDTO(createdUser), HttpStatus.CREATED);
    }

    // Обновление данных пользователя
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        User updatedUser = userService.updateUser(id, user);
        if (updatedUser != null) {
            return new ResponseEntity<>(userMapper.toDTO(updatedUser), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Удаление пользователя
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean isDeleted = userService.deleteUser(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
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