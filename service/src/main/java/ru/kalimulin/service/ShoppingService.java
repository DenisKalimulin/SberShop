package ru.kalimulin.service;

import jakarta.servlet.http.HttpSession;

public interface ShoppingService {
    void buyListing(Long listingId, HttpSession session);
}
