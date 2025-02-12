package ru.kalimulin.service_Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kalimulin.models.Role;
import ru.kalimulin.models.User;
import ru.kalimulin.repositories.RoleRepository;
import ru.kalimulin.repositories.UserRepository;
import ru.kalimulin.service.SubscriptionCleanupService;
import ru.kalimulin.util.RoleName;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubscriptionCleanupServiceImpl implements SubscriptionCleanupService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private static final Logger logger = LoggerFactory.getLogger(SubscriptionCleanupServiceImpl.class);

    @Autowired
    public SubscriptionCleanupServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?") // Запуск каждый день в 00:00
    @Transactional
    @Override
    public void removeExpiredPremium() {
        List<User> expiredUsers = userRepository.findAllBySubscriptionExpirationBefore(LocalDateTime.now());

        if (expiredUsers.isEmpty()) {
            logger.info("Нет пользователей с истекшей подпиской.");
            return;
        }

        Role premiumRole = roleRepository.findByRoleName(RoleName.PREMIUM)
                .orElseThrow(() -> new RuntimeException("Роль PREMIUM не найдена"));

        for (User user : expiredUsers) {
            user.getRoles().removeIf(role -> role.equals(premiumRole));
            user.setSubscriptionExpiration(null);
            logger.info("Пользователь {} потерял PREMIUM статус.", user.getEmail());
        }

        userRepository.saveAll(expiredUsers);
        logger.info("Удалены PREMIUM подписки у {} пользователей", expiredUsers.size());
    }
}

