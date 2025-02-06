package ru.kalimulin.mappers.favoriteMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kalimulin.custum_exceptions.userException.UserNotFoundException;
import ru.kalimulin.entity_dto.favoriteDTO.FavoriteCreateDTO;
import ru.kalimulin.entity_dto.favoriteDTO.FavoriteResponseDTO;
import ru.kalimulin.models.Favorite;
import ru.kalimulin.models.Listing;
import ru.kalimulin.models.User;
import ru.kalimulin.repositories.ListingRepository;
import ru.kalimulin.repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FavoriteMapperImpl implements FavoriteMapper {
    private final UserRepository userRepository;
    private final ListingRepository listingRepository;

    @Autowired
    public FavoriteMapperImpl(UserRepository userRepository, ListingRepository listingRepository) {
        this.userRepository = userRepository;
        this.listingRepository = listingRepository;
    }

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
    public Favorite toFavorite(FavoriteCreateDTO favoriteCreateDTO) {
        if (favoriteCreateDTO == null) {
            return null;
        }

        User user = userRepository.findById(favoriteCreateDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        List<Listing> listings = listingRepository.findAllById(favoriteCreateDTO.getListingsIds());

        return Favorite.builder()
                .user(user)
                .listings(listings)
                .build();
    }
}
