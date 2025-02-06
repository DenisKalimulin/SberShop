package ru.kalimulin.service;

import jakarta.servlet.http.HttpSession;
import ru.kalimulin.entity_dto.userDTO.*;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserResponseDTO registerUser(UserRegistrationDTO userRegistrationDTO);

    LoginResponseDTO authenticateUser(LoginRequestDTO loginRequestDTO);

    UserResponseDTO addAdminRole(String email);

    UserResponseDTO updateUser(HttpSession session, UserUpdateDTO userUpdateDTO);

    UserResponseDTO getUserById(Long id);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUserByEmail(HttpSession session);

    void deleteUserByEmail(HttpSession session);

}