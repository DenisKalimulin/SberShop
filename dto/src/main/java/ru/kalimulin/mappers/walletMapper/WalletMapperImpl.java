package ru.kalimulin.mappers.walletMapper;

import org.springframework.stereotype.Component;
import ru.kalimulin.entity_dto.walletDTO.WalletResponseDTO;
import ru.kalimulin.models.Wallet;

@Component
public class WalletMapperImpl implements WalletMapper {

    @Override
    public WalletResponseDTO toWalletResponseDTO(Wallet wallet) {
        if(wallet == null) {
            return null;
        }

        return WalletResponseDTO.builder()
                .balance(wallet.getBalance())
                .build();
    }
}
