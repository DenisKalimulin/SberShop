package ru.kalimulin.mappers.favoriteMapper;

import org.springframework.stereotype.Component;
import ru.kalimulin.entity_dto.favoriteDTO.FavoriteCreateDTO;
import ru.kalimulin.entity_dto.favoriteDTO.FavoriteResponseDTO;
import ru.kalimulin.models.Favorite;
import ru.kalimulin.models.Listing;
import ru.kalimulin.models.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FavoriteMapperImpl implements FavoriteMapper {

    @Override
    public FavoriteResponseDTO toFavoriteResponseDTO(Favorite favorite) {
        if (favorite == null) {
            return null;
        }

        List<Long> listingIds = favorite.getListings()
                .stream()
                .map(Listing::getId)
                .collect(Collectors.toList());

        return FavoriteResponseDTO
                .builder()
                .id(favorite.getId())
                .email(favorite.getUser().getEmail())
                .listingIds(listingIds)
                .build();
    }

    @Override
    public Favorite toFavorite(FavoriteCreateDTO favoriteCreateDTO, User user, List<Listing> listings) {
        if (favoriteCreateDTO == null || user == null || listings == null) {
            return null;
        }


        return Favorite.builder()
                .user(user)
                .listings(listings)
                .build();
    }
}
