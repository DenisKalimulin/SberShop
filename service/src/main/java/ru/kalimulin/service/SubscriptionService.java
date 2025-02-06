package ru.kalimulin.service;

import jakarta.servlet.http.HttpSession;
import ru.kalimulin.entity_dto.userDTO.UserResponseDTO;
import ru.kalimulin.models.User;

public interface SubscriptionService {
    UserResponseDTO upgradeToPremium(HttpSession session);
    boolean isSubscriptionActive(HttpSession session);
}
