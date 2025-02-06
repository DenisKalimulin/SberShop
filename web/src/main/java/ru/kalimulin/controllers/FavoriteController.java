package ru.kalimulin.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kalimulin.entity_dto.listingDTO.ListingResponseDTO;
import ru.kalimulin.service.FavoriteService;

import java.util.List;

@RestController
@RequestMapping("/shop/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    @Autowired
    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/{listingId}")
    public ResponseEntity<String> addToFavorite(@PathVariable Long listingId, HttpSession session) {
        try {
            favoriteService.addToFavorite(listingId, session);
            return ResponseEntity.status(HttpStatus.CREATED).body("Объявление добавлено в избранное");
        } catch (IllegalStateException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Объявление уже избранном");
        }
    }

    @DeleteMapping("/{listingId}")
    public ResponseEntity<String> removeFromFavorites(@PathVariable Long listingId, HttpSession session) {
        favoriteService.removeFromFavorites(listingId, session);
        return ResponseEntity.ok("Объявление удалено из избранного");
    }

    @GetMapping
    public ResponseEntity<List<ListingResponseDTO>> getFavorites(HttpSession session) {
        List<ListingResponseDTO> favorites = favoriteService.getFavorites(session);
        return ResponseEntity.ok(favorites);
    }
}
