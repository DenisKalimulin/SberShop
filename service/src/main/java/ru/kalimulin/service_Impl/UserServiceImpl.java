package ru.kalimulin.service_Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kalimulin.custum_exceptions.*;
import ru.kalimulin.entity_dto.userDTO.*;
import ru.kalimulin.mappers.userMapper.UserMapper;
import ru.kalimulin.models.Role;
import ru.kalimulin.models.User;
import ru.kalimulin.repositories.RoleRepository;
import ru.kalimulin.repositories.UserRepository;
import ru.kalimulin.service.UserService;
import ru.kalimulin.util.RoleName;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           UserMapper userMapper,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    @Override
    public UserResponseDTO registerUser(UserRegistrationDTO userRegistrationDTO) {
        if (userRepository.findByEmail(userRegistrationDTO.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Пользователь с таким email уже существует");
        }

        Set<Role> defaultRoles = new HashSet<>();
        RoleName[] defaultRoleNames = {RoleName.BUYER, RoleName.SELLER};
        for (RoleName roleName : defaultRoleNames) {
            Role role = roleRepository.findByRoleName(roleName)
                    .orElseThrow(() -> new RoleNotFoundException("Роль " + roleName + " не найдена"));
            defaultRoles.add(role);
        }

        User user = userMapper.toUser(userRegistrationDTO);
        user.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        user.setRoles(defaultRoles);

        User savedUser = userRepository.save(user);

        return userMapper.toUserResponseDTO(savedUser);

    }

    @Override
    public LoginResponseDTO authenticateUser(LoginRequestDTO loginRequestDTO) {
        Optional<User> userOptional = userRepository.findByEmail(loginRequestDTO.getEmail());
        if (userOptional.isEmpty()) {
            throw new InvalidEmailOrPasswordException("Неверный email или пароль");
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new InvalidEmailOrPasswordException("Неверный email или пароль");
        }

        return LoginResponseDTO.builder()
                .message("Успешный вход!")
                .build();
    }

    @Transactional
    @Override
    public UserResponseDTO addAdminRole(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            throw new UserNotFoundException("Пользователь с таким email " + email + " не найден");
        }

        User user = optionalUser.get();

        boolean hasAdminRole = user.getRoles().stream()
                .anyMatch(role -> role.getRoleName() == RoleName.ADMIN);

        if (hasAdminRole) {
            throw new UserAlreadyHasAdminRoleException("У пользователя уже есть роль администратора");
        }

        Role adminRole = roleRepository.findByRoleName(RoleName.ADMIN)
                .orElseThrow(() -> new AdminRoleNotFoundException("Роль администратора не найдена"));

        user.getRoles().add(adminRole);

        userRepository.save(user);

        return userMapper.toUserResponseDTO(user);
    }

    @Transactional
    @Override
    public UserResponseDTO updateUser(String email, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с таким email " + email + " не найден"));

        if (userUpdateDTO.getUserName() != null) {
            user.setUserName(userUpdateDTO.getUserName());
        }

        if (userUpdateDTO.getEmail() != null) {
            if(userRepository.findByEmail(userUpdateDTO.getEmail()).isPresent()) {
                throw new UserAlreadyExistsException("Email уже используется");
            }
            user.setEmail(userUpdateDTO.getEmail());
        }

        if (userUpdateDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));
        }

        userRepository.save(user);

        return userMapper.toUserResponseDTO(user);
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return userMapper.toUserResponseDTO(optionalUser.get());
        } else {
            throw new UserNotFoundException("Пользователь с таким id " + id + " не найден");
        }
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toUserResponseDTOList(users);
    }

    @Override
    public UserResponseDTO getUserByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            return userMapper.toUserResponseDTO(optionalUser.get());
        } else {
            throw new UserNotFoundException("Пользователь с таким email " + email + " не найден");
        }
    }

    @Override
    public void deleteUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с таким email " + email + " не найден"));

        userRepository.delete(user);
    }
}