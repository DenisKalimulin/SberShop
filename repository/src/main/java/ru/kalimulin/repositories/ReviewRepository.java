package ru.kalimulin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kalimulin.models.Review;
import ru.kalimulin.models.User;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findBySeller(User seller);
    boolean existsByBuyerAndSeller(User buyer, User seller);
}
