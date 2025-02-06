package ru.kalimulin.custum_exceptions.listingException;

public class ListingUnavailableException extends RuntimeException {
    public ListingUnavailableException(String message) {
        super(message);
    }
}
