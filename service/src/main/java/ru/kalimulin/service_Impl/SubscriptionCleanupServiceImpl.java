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
import java.util.stream.Collectors;

@Service
public class SubscriptionCleanupServiceImpl implements SubscriptionCleanupService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private static final Logger logger = LoggerFactory.getLogger(SubscriptionCleanupService.class);

    @Autowired
    public SubscriptionCleanupServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }


    @Scheduled(cron = "0 0 0 * * ?") //Запуск каждый день в 00:00
    @Transactional
    @Override
    public void removeExpiredPremiumO() {
        List<User> premiumUsers = userRepository.findAll().stream()
                .filter(User::isPremiumActive)
                .collect(Collectors.toList());

        Role premiumRole = roleRepository.findByRoleName(RoleName.PREMIUM)
                .orElseThrow(() -> new RuntimeException("Роль PREMIUM не найдена"));

        for (User user : premiumUsers) {
            if (user.getSubscriptionExpiration().isBefore(LocalDateTime.now())) {
                user.getRoles().remove(premiumRole);
                userRepository.save(user);
                logger.info("Пользователь {} потерял PREMIUM статус", user.getEmail());
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void removeExpiredSubscriptions() {
        List<User> expiredUsers = userRepository.findAllBySubscriptionExpirationBefore(LocalDateTime.now());

        for (User user : expiredUsers) {
            user.getRoles().removeIf(role -> role.getRoleName() == RoleName.PREMIUM);
            user.setSubscriptionExpiration(null);
        }

        userRepository.saveAll(expiredUsers);
        logger.info("Удалены подписки у {} пользователей", expiredUsers.size());
    }
}
