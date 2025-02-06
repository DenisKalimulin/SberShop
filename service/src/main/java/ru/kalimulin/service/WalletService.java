package ru.kalimulin.service;

import jakarta.servlet.http.HttpSession;
import ru.kalimulin.entity_dto.walletDTO.WalletResponseDTO;
import ru.kalimulin.entity_dto.walletDTO.WalletUpdateDTO;
import ru.kalimulin.models.User;
import ru.kalimulin.models.Wallet;

import java.math.BigDecimal;

public interface WalletService {
    WalletResponseDTO getUserWallet(HttpSession session);

    void transfer(User buyer, User seller, BigDecimal amount);

    WalletResponseDTO createWallet(HttpSession session);

    void deposit(HttpSession session, WalletUpdateDTO walletUpdateDTO);
}
