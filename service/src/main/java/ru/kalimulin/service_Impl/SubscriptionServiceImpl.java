package ru.kalimulin.service_Impl;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kalimulin.custum_exceptions.subscriptionException.SubscriptionException;
import ru.kalimulin.custum_exceptions.walletException.InsufficientFundsException;
import ru.kalimulin.custum_exceptions.walletException.PaymentProcessingException;
import ru.kalimulin.custum_exceptions.walletException.WalletNotFoundException;
import ru.kalimulin.entity_dto.userDTO.UserResponseDTO;
import ru.kalimulin.mappers.userMapper.UserMapper;
import ru.kalimulin.models.User;
import ru.kalimulin.repositories.RoleRepository;
import ru.kalimulin.repositories.UserRepository;
import ru.kalimulin.service.SubscriptionService;
import ru.kalimulin.stub_service.PaymentService;
import ru.kalimulin.util.RoleName;
import ru.kalimulin.util.SessionUtils;
import ru.kalimulin.models.Role;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PaymentService paymentService;
    private final UserMapper userMapper;

    private static final BigDecimal PREMIUM_PRICE = BigDecimal.valueOf(299.99);

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionServiceImpl.class);

    @Autowired
    public SubscriptionServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                                   PaymentService paymentService, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.paymentService = paymentService;
        this.userMapper = userMapper;
    }

    @Transactional
    @Override
    public UserResponseDTO upgradeToPremium(HttpSession session) {
        String userEmail = SessionUtils.getUserEmail(session);
        User user = userRepository.findByEmail(userEmail).get();

        if(user.isPremiumActive()) {
            throw new SubscriptionException("У вас уже есть активная PREMIUM подписка!");
        }

        if(user.getWallet() == null) {
            throw new WalletNotFoundException("Нет кошелька!");
        }

        if (user.getWallet().getBalance().compareTo(PREMIUM_PRICE) >= 0) {
            user.getWallet().setBalance(user.getWallet().getBalance().subtract(PREMIUM_PRICE));
        } else {
            throw new InsufficientFundsException("Недостаточно средств на балансе");
        }

        boolean paymentSuccess = paymentService.processPayment(userEmail, PREMIUM_PRICE);

        if(!paymentSuccess) {
            throw new PaymentProcessingException("Ошибка при обработке платежа. Попробуйте снова.");
        }

        Role premiumRole = roleRepository.findByRoleName(RoleName.PREMIUM)
                .orElseThrow(() -> new RuntimeException("Роль PREMIUM не найдена"));

        user.getRoles().add(premiumRole);
        user.setSubscriptionExpiration(LocalDateTime.now().plusMonths(1));

        userRepository.save(user);

        logger.info("Пользователь {} успешно получил PREMIUM статус до {}", userEmail, user.getSubscriptionExpiration());
        return userMapper.toUserResponseDTO(user);
    }

    @Override
    public boolean isSubscriptionActive(HttpSession session) {
        String userEmail = SessionUtils.getUserEmail(session);
        User user = userRepository.findByEmail(userEmail).get();
        return user.isPremiumActive();
    }
}
