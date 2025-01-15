package org.profin.dto;

import org.profin.model.BankAccount;
import org.profin.model.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Mapper class to convert between User entity and UserDTO.
 * This class provides methods to map a User object to a UserDTO and vice versa.
 */
@Component
public class UserMapper {

    /**
     * Converts a User entity to a UserDTO.
     * This method maps the fields from User to UserDTO, including additional fields like
     * bank account balances.
     *
     * @param user the User entity to be converted
     * @return UserDTO the mapped UserDTO object
     */
    public UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();

        // Mapping basic fields
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUpdatedAt(user.getUpdatedAt());

        // Creating a list of balances from user's bank accounts
        List<BigDecimal> balances = user.getBankAccounts().stream()
                .map(BankAccount::getBalance)
                .collect(Collectors.toList());

        userDTO.setBankAccountBalances(balances);

        return userDTO;
    }

    /**
     * Converts a UserDTO to a User entity.
     * This method maps the fields from UserDTO to User. Note that it doesn't include
     * the bank account balances since they are not part of the User entity.
     *
     * @param userDTO the UserDTO to be converted
     * @return User the mapped User entity
     */
    public User toEntity(UserDTO userDTO) {
        User user = new User();

        // Mapping basic fields
        user.setId(userDTO.getId());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());

        return user;
    }
}