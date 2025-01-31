package ru.kalimulin.custum_exceptions;

public class AdminRoleNotFoundException extends RuntimeException {
    public AdminRoleNotFoundException(String message) {
        super(message);
    }
}
