package ru.kalimulin.service_Impl;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kalimulin.custum_exceptions.favoriteException.FavoriteNotFoundException;
import ru.kalimulin.custum_exceptions.listingException.ListingNotFoundException;
import ru.kalimulin.entity_dto.listingDTO.ListingResponseDTO;
import ru.kalimulin.mappers.listingMapper.ListingMapper;
import ru.kalimulin.models.Favorite;
import ru.kalimulin.models.Listing;
import ru.kalimulin.models.User;
import ru.kalimulin.repositories.FavoriteRepository;
import ru.kalimulin.repositories.ListingRepository;
import ru.kalimulin.repositories.UserRepository;
import ru.kalimulin.service.FavoriteService;
import ru.kalimulin.util.SessionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteServiceImpl implements FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final ListingRepository listingRepository;

    private final ListingMapper listingMapper;

    @Autowired
    public FavoriteServiceImpl(FavoriteRepository favoriteRepository,
                               UserRepository userRepository,
                               ListingRepository listingRepository,
                               ListingMapper listingMapper) {

        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.listingRepository = listingRepository;
        this.listingMapper = listingMapper;
    }

    @Override
    public void addToFavorite(Long listingId, HttpSession session) {
        String userEmail = SessionUtils.getUserEmail(session);

        User user = userRepository.findByEmail(userEmail).get();

        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new ListingNotFoundException("Объявление не найдено"));

        Favorite favorite = favoriteRepository.findByUser(user).orElse(Favorite.builder().user(user).listings(new ArrayList<>()).build());

        if (favorite.getListings().contains(listing)) {
            throw new IllegalStateException("Объявление уже в избранном");
        }

        favorite.getListings().add(listing);
        favoriteRepository.save(favorite);
    }

    @Override
    public void removeFromFavorites(Long listingId, HttpSession session) {
        String userEmail = SessionUtils.getUserEmail(session);

        User user = userRepository.findByEmail(userEmail).get();

        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new ListingNotFoundException("Объявление не найдено"));

        Favorite favorite = favoriteRepository.findByUser(user)
                .orElseThrow(() -> new FavoriteNotFoundException("Избранное не найдено"));

        favorite.getListings().remove(listing);
        favoriteRepository.save(favorite);
    }

    @Override
    public List<ListingResponseDTO> getFavorites(HttpSession session) {
        String userEmail = SessionUtils.getUserEmail(session);

        User user = userRepository.findByEmail(userEmail).get();

        Favorite favorite = favoriteRepository.findByUser(user)
                .orElseThrow(() -> new FavoriteNotFoundException("Избранное не найдено"));

        return favorite.getListings().stream()
                .map(listingMapper::toListingResponseDTO)
                .collect(Collectors.toList());
    }
}