package ru.kalimulin.custum_exceptions.userException;

public class UserAlreadyHasAdminRoleException extends RuntimeException {
    public UserAlreadyHasAdminRoleException(String message) {
        super(message);
    }
}
