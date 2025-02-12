package ru.kalimulin.service;

import jakarta.servlet.http.HttpSession;
import ru.kalimulin.entity_dto.reviewDTO.ReviewCreateDTO;
import ru.kalimulin.entity_dto.reviewDTO.ReviewResponseDTO;

import java.util.List;

public interface ReviewService {
    ReviewResponseDTO leaveReview(Long sellerId, ReviewCreateDTO reviewCreateDTO, HttpSession session);

    List<ReviewResponseDTO> getSellerReviews(Long sellerId);

    double getSellerAverageRating(Long sellerId);

    void deleteReview(Long id);
}
