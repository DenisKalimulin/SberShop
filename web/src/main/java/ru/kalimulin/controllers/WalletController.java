package ru.kalimulin.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kalimulin.entity_dto.walletDTO.WalletResponseDTO;
import ru.kalimulin.service.WalletService;

@RestController
@RequestMapping("/wallet")
public class WalletController {
    private final WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/create")
    public ResponseEntity<WalletResponseDTO> createWallet(HttpSession session) {
        WalletResponseDTO walletResponseDTO = walletService.createWallet(session);
        return ResponseEntity.status(HttpStatus.CREATED).body(walletResponseDTO);
    }
}
