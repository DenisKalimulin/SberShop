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
import ru.kalimulin.entity_dto.walletDTO.WalletResponseDTO;
import ru.kalimulin.entity_dto.walletDTO.WalletUpdateDTO;
import ru.kalimulin.service.UserService;
import ru.kalimulin.service.WalletService;
import ru.kalimulin.util.SessionUtils;


@RestController
@RequestMapping("shop/users")
public class UserController {
    private final UserService userService;
    private final WalletService walletService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService, WalletService walletService) {
        this.userService = userService;
        this.walletService = walletService;
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

        UserResponseDTO updatedUser = userService.updateUser(session, userUpdateDTO);
        logger.info("Профиль пользователя с email {} был успешно обновлен", updatedUser.getEmail());

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

        userService.deleteUserByEmail(session);
        session.invalidate();
        logger.info("Пользователь с email {} был успешно удален из системы", SessionUtils.getUserEmail(session));

        return ResponseEntity.ok("Профиль удален");
    }

    @Operation(summary = "Просмотр кошелька пользователя", description = "Позволяет пользователю увидеть баланс кошелька")
    @GetMapping("/me/balance")
    public ResponseEntity<WalletResponseDTO> getUserBalance(HttpSession session) {
        WalletResponseDTO balance = walletService.getUserWallet(session);
        logger.info("Просмотр баланса пользователем: {}", SessionUtils.getUserEmail(session));
        return ResponseEntity.ok(balance);
    }

    @PostMapping("/me/deposit")
    public ResponseEntity<String> depositBalance(HttpSession session, @RequestBody WalletUpdateDTO walletUpdateDTO) {
        walletService.deposit(session, walletUpdateDTO);
        logger.info("Пользователь {} пополнил баланс на сумму {}", SessionUtils.getUserEmail(session), walletUpdateDTO.getAmount());
        return ResponseEntity.ok("Баланс успешно пополнен!");
    }


}