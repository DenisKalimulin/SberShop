package ru.kalimulin.mappers.listingMapper;

import org.springframework.stereotype.Component;
import ru.kalimulin.entity_dto.listingDTO.ListingCreateDTO;
import ru.kalimulin.entity_dto.listingDTO.ListingResponseDTO;
import ru.kalimulin.models.Category;
import ru.kalimulin.models.Listing;
import ru.kalimulin.models.User;
import ru.kalimulin.util.ListingStatus;

import java.util.List;

@Component
public class ListingMapperImpl implements ListingMapper {

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
                .sellerName(listing.getSeller() != null ? listing.getSeller().getUserName() : null)
                .categoryName(listing.getCategory() != null ? listing.getCategory().getName() : null)
                .build();
    }

    @Override
    public Listing toListing(ListingCreateDTO listingCreateDTO, User user, Category category) {
        return Listing.builder()
                .title(listingCreateDTO.getTitle())
                .description(listingCreateDTO.getDescription())
                .price(listingCreateDTO.getPrice())
                .status(listingCreateDTO.getStatus() != null ? listingCreateDTO.getStatus() : ListingStatus.ACTIVE)
                .brand(listingCreateDTO.getBrand())
                .imageUrl(listingCreateDTO.getImageUrl())
                .seller(user)
                .category(category)
                .build();
    }
}
