package ru.kalimulin.service;

import jakarta.servlet.http.HttpSession;
import ru.kalimulin.entity_dto.listingDTO.ListingResponseDTO;

import java.util.List;

public interface FavoriteService {
    void addToFavorite(Long listingId, HttpSession session);

    void removeFromFavorites(Long listingId, HttpSession session);

    List<ListingResponseDTO> getFavorites(HttpSession session);
}
