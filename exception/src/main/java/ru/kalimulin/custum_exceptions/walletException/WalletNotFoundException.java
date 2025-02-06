package ru.kalimulin.custum_exceptions.walletException;

public class WalletNotFoundException extends RuntimeException {
    public WalletNotFoundException (String message) {
        super(message);
    }
}
