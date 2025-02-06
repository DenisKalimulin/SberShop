package ru.kalimulin.custum_exceptions.favoriteException;

public class FavoriteNotFoundException extends RuntimeException {
    public FavoriteNotFoundException(String message) {
        super(message);
    }
}
