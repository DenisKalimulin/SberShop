package ru.kalimulin.custum_exceptions.walletException;

public class PaymentProcessingException extends RuntimeException {
    public PaymentProcessingException(String message) {
        super(message);
    }
}
