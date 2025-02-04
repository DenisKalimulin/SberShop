package ru.kalimulin.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ru.kalimulin.custum_exceptions.userException.UnauthorizedException;
import ru.kalimulin.entity_dto.userDTO.LoginRequestDTO;
import ru.kalimulin.entity_dto.userDTO.LoginResponseDTO;
import ru.kalimulin.entity_dto.userDTO.UserRegistrationDTO;
import ru.kalimulin.entity_dto.userDTO.UserResponseDTO;
import ru.kalimulin.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/shop/auth")
public class AuthController {
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Регистрация нового пользователя", description = "Создает нового пользователя в системе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно зарегистрирован"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации данных"),
            @ApiResponse(responseCode = "409", description = "Пользователь с таким email уже существует")
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody
                                                        UserRegistrationDTO registrationDTO) {
        UserResponseDTO userResponseDTO = userService.registerUser(registrationDTO);

        logger.info("Новый пользователь зарегистрирован: {}", userResponseDTO.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDTO);
    }

    @Operation(summary = "Авторизация пользователя", description = "Вход в систему с email и паролем")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный вход"),
            @ApiResponse(responseCode = "401", description = "Неверный email или пароль"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации данных")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@Valid @RequestBody
                                                      LoginRequestDTO loginRequestDTO,
                                                      HttpSession session) {
        LoginResponseDTO responseDTO = userService.authenticateUser(loginRequestDTO);
        session.setAttribute("userEmail", loginRequestDTO.getEmail());

        logger.info("Пользователь вошел в систему: {}", loginRequestDTO.getEmail());

        return ResponseEntity.ok(responseDTO);
    }

    @Operation(summary = "Выход пользователя из системы", description = "Завершает сессию пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный выход"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    })
    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(HttpSession session) {
        String email = (String) session.getAttribute("userEmail");

        if (email == null) {
            throw new UnauthorizedException("Пользователь не авторизован. Войдите в систему.");
        }

        session.invalidate();

        logger.info("Пользователь вышел из системы: {}", email);

        return ResponseEntity.ok("Успешный выход!");
    }

    @Operation(summary = "Получение профиля пользователя", description = "Возвращает данные профиля текущего пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Профиль пользователя найден"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    })
    @GetMapping("/profile")
    public ResponseEntity<UserResponseDTO> getUserProfile(HttpSession session) {
        String email = (String) session.getAttribute("userEmail");
        if (email == null) {
            logger.warn("Неавторизованный доступ к профилю");
            throw new UnauthorizedException("Пользователь не авторизован. Войдите в систему.");
        }

        UserResponseDTO userResponseDTO = userService.getUserByEmail(email);
        return ResponseEntity.ok(userResponseDTO);
    }
}