package ru.kalimulin.service;

import ru.kalimulin.entity_dto.userDTO.*;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserResponseDTO registerUser(UserRegistrationDTO userRegistrationDTO);

    LoginResponseDTO authenticateUser(LoginRequestDTO loginRequestDTO);

    UserResponseDTO addAdminRole(String email);

    UserResponseDTO updateUser(String email, UserUpdateDTO userUpdateDTO);

    UserResponseDTO getUserById(Long id);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUserByEmail(String email);

    void deleteUserByEmail(String email);

    UserResponseDTO upgradeToPremium(String email);
}