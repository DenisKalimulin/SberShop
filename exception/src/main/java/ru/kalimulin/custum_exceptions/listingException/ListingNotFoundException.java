package ru.kalimulin.custum_exceptions.listingException;

public class ListingNotFoundException extends RuntimeException {
    public ListingNotFoundException(String messege) {
        super(messege);
    }
}
