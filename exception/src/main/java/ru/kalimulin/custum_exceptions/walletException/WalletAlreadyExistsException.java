package ru.kalimulin.custum_exceptions.walletException;

public class WalletAlreadyExistsException extends RuntimeException {
    public WalletAlreadyExistsException(String message){
        super(message);
    }
}
