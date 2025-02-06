package ru.kalimulin.mappers.favoriteMapper;

import ru.kalimulin.entity_dto.favoriteDTO.FavoriteCreateDTO;
import ru.kalimulin.entity_dto.favoriteDTO.FavoriteResponseDTO;
import ru.kalimulin.models.Favorite;
import ru.kalimulin.models.Listing;

import java.util.List;

public interface FavoriteMapper {
    FavoriteResponseDTO toFavoriteResponseDTO(Favorite favorite);

    Favorite toFavorite(FavoriteCreateDTO favoriteCreateDTO);
}