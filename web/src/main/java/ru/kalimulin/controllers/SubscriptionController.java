package ru.kalimulin.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kalimulin.entity_dto.userDTO.UserResponseDTO;
import ru.kalimulin.service.SubscriptionService;


@RestController
@RequestMapping("/shop/subscription")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/upgrade")
    public ResponseEntity<UserResponseDTO> upgradeToPremium(HttpSession session) {
        UserResponseDTO upgradedUser = subscriptionService.upgradeToPremium(session);
        return ResponseEntity.ok(upgradedUser);
    }

    @GetMapping("/is-active")
    public ResponseEntity<String> premiumIsActive(HttpSession session) {
        boolean isActive = subscriptionService.isSubscriptionActive(session);

        if(isActive) {
            return ResponseEntity.ok("Подписка активна");
        } else {
            return ResponseEntity.ok("У вас нет подписки");
        }
    }
}
