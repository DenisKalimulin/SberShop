package ru.kalimulin.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kalimulin.custum_exceptions.userException.UnauthorizedException;
import ru.kalimulin.entity_dto.userDTO.UserResponseDTO;
import ru.kalimulin.entity_dto.userDTO.UserUpdateDTO;
import ru.kalimulin.service.UserService;


@RestController
@RequestMapping("shop/users")
public class UserController {
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Обновить профиль пользователя", description = "Позволяет обновить данные текущего пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Профиль успешно обновлен"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации")
    })
    @PutMapping("/me/update")
    public ResponseEntity<UserResponseDTO> updateUser(@Valid @RequestBody UserUpdateDTO userUpdateDTO,
                                                      HttpSession session) {
        String email = (String) session.getAttribute("userEmail");

        if (email == null) {
            logger.warn("Неавторизованный доступ к редактированию профиля");
            throw new UnauthorizedException("Пользователь не авторизован. Войдите в систему.");
        }

        UserResponseDTO updatedUser = userService.updateUser(email, userUpdateDTO);
        logger.info("Профиль пользователя с email {} был успешно обновлен", updatedUser.getEmail());

        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Купить PREMIUM", description = "Позволяет пользователю получить PREMIUM статус (заглушка)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статус PREMIUM успешно назначен"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "400", description = "Пользователь уже имеет PREMIUM статус"),
            @ApiResponse(responseCode = "500", description = "Ошибка при обработке платежа")
    })
    @PostMapping("/me/upgrade")
    public ResponseEntity<UserResponseDTO> upgradeToPremium(HttpSession session) {
        String email = (String) session.getAttribute("userEmail");
        if (email == null) {
            throw new UnauthorizedException("Вы не авторизованы. Войдите в систему!");
        }

        logger.info("Пользователь {} запрашивает PREMIUM статус", email);
        UserResponseDTO updatedUser = userService.upgradeToPremium(email);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Удалить профиль пользователя", description = "Позволяет пользователю удалить свой аккаунт")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Профиль успешно удален"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @DeleteMapping("/me/delete")
    public ResponseEntity<String> deleteUser(HttpSession session) {
        String email = (String) session.getAttribute("userEmail");

        if(email == null) {
            logger.warn("Неавторизованный доступ к удалению профиля");
            throw new UnauthorizedException("Пользователь не авторизован. Войдите в систему.");
        }

        userService.deleteUserByEmail(email);
        session.invalidate();
        logger.info("Пользователь с email {} был успешно удален из системы", email);

        return ResponseEntity.ok("Профиль удален");
    }
}