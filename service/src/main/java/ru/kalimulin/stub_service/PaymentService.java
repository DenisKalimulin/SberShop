package ru.kalimulin.stub_service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kalimulin.custum_exceptions.userException.UserNotFoundException;
import ru.kalimulin.custum_exceptions.walletException.InsufficientFundsException;
import ru.kalimulin.custum_exceptions.walletException.WalletNotFoundException;
import ru.kalimulin.models.User;
import ru.kalimulin.models.Wallet;
import ru.kalimulin.repositories.UserRepository;
import ru.kalimulin.repositories.WalletRepository;

import java.math.BigDecimal;
import java.util.Random;

@Service
public class PaymentService {
    private final Random random = new Random();
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    @Autowired
    public PaymentService(WalletRepository walletRepository, UserRepository userRepository) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
    }

    public boolean processPayment(String userEmail, BigDecimal amount) {
        logger.info("Обработка платежа для пользователя {} на сумму {}", userEmail, amount);

        boolean payment = random.nextBoolean();

        if (payment) {
            logger.info("Платеж для {} на сумму {} успешно обработан", userEmail, amount);
            return true;
        } else {
            logger.error("Ошибка обработки платежа для пользователя {}", userEmail);
            return false;
        }
    }

    @Transactional
    public void withdrawFunds(String userEmail, BigDecimal amount) {
        logger.info("Попытка списания {} с кошелька пользователя {}", amount, userEmail);

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с email " + userEmail + " не найден"));

        Wallet wallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new WalletNotFoundException("Кошелек пользователя не найден"));

        if (wallet.getBalance().compareTo(amount) < 0) {
            logger.error("Ошибка: Недостаточно средств у пользователя {}", userEmail);
            throw new InsufficientFundsException("Недостаточно средств на балансе");
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepository.save(wallet);

        logger.info("Успешное списание {} с кошелька пользователя {}", amount, userEmail);
    }

}
