package ru.kalimulin.custum_exceptions.roleException;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String message) {
        super(message);
    }
}
