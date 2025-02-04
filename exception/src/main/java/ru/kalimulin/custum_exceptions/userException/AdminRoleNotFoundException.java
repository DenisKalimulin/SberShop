package ru.kalimulin.custum_exceptions.userException;

public class AdminRoleNotFoundException extends RuntimeException {
    public AdminRoleNotFoundException(String message) {
        super(message);
    }
}
