package ru.kalimulin.custum_exceptions.chatAndMessageException;

public class ChatNotFoundException extends RuntimeException {
    public ChatNotFoundException(String message) {
        super(message);
    }
}
