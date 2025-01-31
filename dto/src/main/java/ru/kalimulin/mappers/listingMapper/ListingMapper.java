package ru.kalimulin.mappers.listingMapper;

import ru.kalimulin.entity_dto.listingDTO.ListingCreateDTO;
import ru.kalimulin.entity_dto.listingDTO.ListingResponseDTO;
import ru.kalimulin.models.Listing;

import java.util.List;

public interface ListingMapper {
    ListingResponseDTO toListingResponseDTO(Listing listing);

    Listing toListing(ListingCreateDTO listingCreateDTO);

    List<ListingResponseDTO> toListingResponseDTOList(List<Listing> listing);
}
