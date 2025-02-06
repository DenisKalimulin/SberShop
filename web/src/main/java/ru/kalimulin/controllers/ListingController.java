package ru.kalimulin.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kalimulin.entity_dto.listingDTO.ListingCreateDTO;
import ru.kalimulin.entity_dto.listingDTO.ListingResponseDTO;
import ru.kalimulin.entity_dto.listingDTO.ListingUpdateDTO;
import ru.kalimulin.service.ListingService;
import ru.kalimulin.util.ListingStatus;

import java.math.BigDecimal;


@RestController
@RequestMapping("/shop/listings")
public class ListingController {
    private final ListingService listingService;

    @Autowired
    public ListingController(ListingService listingService) {
        this.listingService = listingService;
    }

    @PostMapping
    public ResponseEntity<ListingResponseDTO> addListing(@RequestBody ListingCreateDTO listingCreateDTO, HttpSession session) {

        ListingResponseDTO listingResponseDTO = listingService.createListing(listingCreateDTO, session);

        return ResponseEntity.status(HttpStatus.CREATED).body(listingResponseDTO);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ListingResponseDTO>> searchListings(
            @RequestParam(required = false, name = "title") String title,
            @RequestParam(required = false, name = "category") String category,
            @RequestParam(required = false, name = "minPrice") BigDecimal minPrice,
            @RequestParam(required = false, name = "maxPrice") BigDecimal maxPrice,
            Pageable pageable
    ) {
        Page<ListingResponseDTO> result = listingService.searchListings(title, category, minPrice, maxPrice, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListingResponseDTO> findListingById(@PathVariable Long id) {
        ListingResponseDTO listingResponseDTO = listingService.findById(id);

        return ResponseEntity.ok(listingResponseDTO);
    }

    @GetMapping
    public ResponseEntity<Page<ListingResponseDTO>> findAllListings(Pageable pageable) {
        Page<ListingResponseDTO> result = listingService.findAllListings(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/seller/{sellerEmail}")
    public ResponseEntity<Page<ListingResponseDTO>> findListingsBySeller(@PathVariable String sellerEmail,
                                                                         Pageable pageable) {

        Page<ListingResponseDTO> result = listingService.findAllBySeller(sellerEmail, pageable);

        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListingResponseDTO> updateListing(
            @PathVariable Long id,
            @RequestBody ListingUpdateDTO listingUpdateDTO,
            HttpSession session) {

        ListingResponseDTO updatedListing = listingService.updateListing(id, listingUpdateDTO, session);
        return ResponseEntity.ok(updatedListing);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ListingResponseDTO> changeStatus(@PathVariable Long id,
                                                           @RequestParam ListingStatus listingStatus,
                                                           HttpSession session) {
        ListingResponseDTO changeListingStatus = listingService.changeListingStatus(id, listingStatus, session);
        return ResponseEntity.ok(changeListingStatus);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteListing(@PathVariable Long id,
                                                HttpSession session) {
        listingService.deleteListing(id, session);
        return ResponseEntity.ok("Объявление успешно удалено");
    }
}