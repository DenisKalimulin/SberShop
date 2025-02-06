package ru.kalimulin.service_Impl;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kalimulin.custum_exceptions.walletException.InsufficientFundsException;
import ru.kalimulin.custum_exceptions.walletException.PaymentProcessingException;
import ru.kalimulin.custum_exceptions.walletException.WalletAlreadyExistsException;
import ru.kalimulin.custum_exceptions.walletException.WalletNotFoundException;
import ru.kalimulin.entity_dto.walletDTO.WalletResponseDTO;
import ru.kalimulin.entity_dto.walletDTO.WalletUpdateDTO;
import ru.kalimulin.mappers.walletMapper.WalletMapper;
import ru.kalimulin.models.User;
import ru.kalimulin.models.Wallet;
import ru.kalimulin.repositories.UserRepository;
import ru.kalimulin.repositories.WalletRepository;
import ru.kalimulin.service.WalletService;
import ru.kalimulin.stub_service.PaymentService;
import ru.kalimulin.util.SessionUtils;

import java.math.BigDecimal;

@Service
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final PaymentService paymentService;
    private final WalletMapper walletMapper;

    @Autowired
    public WalletServiceImpl(WalletRepository walletRepository, UserRepository userRepository,
                             PaymentService paymentService, WalletMapper walletMapper) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.paymentService = paymentService;
        this.walletMapper = walletMapper;
    }

    @Override
    public WalletResponseDTO getUserWallet(HttpSession session) {
        String userEmail = SessionUtils.getUserEmail(session);

        User user = userRepository.findByEmail(userEmail).get();

        Wallet wallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new WalletNotFoundException("Кошелек пользователя не найден"));

        return walletMapper.toWalletResponseDTO(wallet);
    }


    @Override
    public void transfer(User buyer, User seller, BigDecimal amount) {
        Wallet buyerWallet = walletRepository.findByUser(buyer)
                .orElseThrow(() -> new WalletNotFoundException("Кошелек покупателя не найден"));

        Wallet sellerWallet = walletRepository.findByUser(seller)
                .orElseThrow(() -> new WalletNotFoundException("Кошелек продавца не найден"));

        if (buyerWallet.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Недостаточно средств на балансе");
        }

        buyerWallet.setBalance(buyerWallet.getBalance().subtract(amount));
        sellerWallet.setBalance(sellerWallet.getBalance().add(amount));

        walletRepository.save(buyerWallet);
        walletRepository.save(sellerWallet);
    }

    @Transactional
    @Override
    public WalletResponseDTO createWallet(HttpSession session) {
        String userEmail = SessionUtils.getUserEmail(session);

        User user = userRepository.findByEmail(userEmail).get();

        if (walletRepository.findByUser(user).isPresent()) {
            throw new WalletAlreadyExistsException("Кошелек уже существует");
        }

        Wallet wallet = Wallet.builder()
                .user(user)
                .balance(BigDecimal.ZERO)
                .build();

        walletRepository.save(wallet);

        return walletMapper.toWalletResponseDTO(wallet);
    }


    @Transactional
    @Override
    public void deposit(HttpSession session, WalletUpdateDTO walletUpdateDTO) {
        String userEmail = SessionUtils.getUserEmail(session);

        User user = userRepository.findByEmail(userEmail).get();

        Wallet wallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new WalletNotFoundException("Кошелек пользователя не найден"));

        BigDecimal amount = walletUpdateDTO.getAmount();

        boolean payment = paymentService.processPayment(userEmail, amount);

        if (!payment) {
            throw new PaymentProcessingException("Ошибка при обработке платежа. Повторите попытку позже");
        }

        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);
    }
}