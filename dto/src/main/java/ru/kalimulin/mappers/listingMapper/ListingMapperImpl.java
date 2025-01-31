package ru.kalimulin.mappers.listingMapper;

import ru.kalimulin.entity_dto.listingDTO.ListingCreateDTO;
import ru.kalimulin.entity_dto.listingDTO.ListingResponseDTO;
import ru.kalimulin.models.Listing;

import java.util.List;
import java.util.stream.Collectors;

public class ListingMapperImpl implements ListingMapper {
    @Override
    public ListingResponseDTO toListingResponseDTO(Listing listing) {
        if (listing == null) {
            return null;
        }

        return ListingResponseDTO.builder()
                .id(listing.getId())
                .title(listing.getTitle())
                .description(listing.getDescription())
                .price(listing.getPrice())
                .createdAt(listing.getCreatedAt())
                .status(listing.getStatus())
                .brand(listing.getBrand())
                .imageUrl(listing.getImageUrl())
                .sellerName(listing.getSeller().getUserName())
                .categoryName(listing.getCategory().getName())
                .build();
    }

    @Override
    public Listing toListing(ListingCreateDTO listingCreateDTO) {
        if (listingCreateDTO == null) {
            return null;
        }

        return Listing.builder()
                .title(listingCreateDTO.getTitle())
                .description(listingCreateDTO.getDescription())
                .price(listingCreateDTO.getPrice())
                .status(listingCreateDTO.getStatus())
                .brand(listingCreateDTO.getBrand())
                .imageUrl(listingCreateDTO.getImageUrl())
                .build();
    }

    @Override
    public List<ListingResponseDTO> toListingResponseDTOList(List<Listing> listing) {
        if (listing == null || listing.isEmpty()) {
            return List.of();
        }

        return listing.stream()
                .map(this::toListingResponseDTO)
                .collect(Collectors.toList());
    }
}
