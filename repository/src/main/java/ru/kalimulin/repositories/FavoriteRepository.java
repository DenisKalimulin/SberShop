package ru.kalimulin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kalimulin.models.Favorite;
import ru.kalimulin.models.Listing;
import ru.kalimulin.models.User;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Optional<Favorite> findByUser(User user);

    boolean existsByUserAndListingsContaining(User user, Listing listing);

    void deleteByUserAndListingsContaining(User user, Listing listing);
}
