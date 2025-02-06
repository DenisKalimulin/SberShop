package ru.kalimulin.service;

import jakarta.servlet.http.HttpSession;
import ru.kalimulin.entity_dto.listingDTO.ListingCreateDTO;
import ru.kalimulin.entity_dto.listingDTO.ListingResponseDTO;
import ru.kalimulin.entity_dto.listingDTO.ListingUpdateDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.kalimulin.util.ListingStatus;

import java.math.BigDecimal;

public interface ListingService {
    ListingResponseDTO findById(Long id);

    Page<ListingResponseDTO> findAllListings(Pageable pageable);

    Page<ListingResponseDTO> searchListings(String title, String category, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    Page<ListingResponseDTO> findAllBySeller(String sellerEmail, Pageable pageable);

    ListingResponseDTO createListing(ListingCreateDTO listingCreateDTO, HttpSession session);

    ListingResponseDTO updateListing(Long id, ListingUpdateDTO listingUpdateDTO, HttpSession session);

    ListingResponseDTO changeListingStatus(Long id, ListingStatus newStatus, HttpSession session);

    void deleteListing(Long id, HttpSession session);
}
