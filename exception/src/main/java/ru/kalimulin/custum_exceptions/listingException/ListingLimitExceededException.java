package ru.kalimulin.custum_exceptions.listingException;

public class ListingLimitExceededException extends RuntimeException {
    public ListingLimitExceededException(String message) {
        super(message);
    }
}
