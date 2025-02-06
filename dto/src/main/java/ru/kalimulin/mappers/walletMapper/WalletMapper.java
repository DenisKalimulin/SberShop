package ru.kalimulin.mappers.walletMapper;

import ru.kalimulin.entity_dto.walletDTO.WalletResponseDTO;
import ru.kalimulin.models.Wallet;

public interface WalletMapper {
    WalletResponseDTO toWalletResponseDTO(Wallet wallet);
}
