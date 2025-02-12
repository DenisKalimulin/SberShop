package ru.kalimulin.custum_exceptions.reviewException;

public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException(String message) {
        super(message);
    }
}
