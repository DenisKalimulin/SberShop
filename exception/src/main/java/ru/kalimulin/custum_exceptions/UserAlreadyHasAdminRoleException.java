package ru.kalimulin.custum_exceptions;

public class UserAlreadyHasAdminRoleException extends RuntimeException {
    public UserAlreadyHasAdminRoleException(String message) {
        super(message);
    }
}
