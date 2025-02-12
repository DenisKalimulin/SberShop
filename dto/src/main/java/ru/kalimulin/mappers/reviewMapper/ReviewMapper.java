package ru.kalimulin.mappers.reviewMapper;

import ru.kalimulin.entity_dto.reviewDTO.ReviewCreateDTO;
import ru.kalimulin.entity_dto.reviewDTO.ReviewResponseDTO;
import ru.kalimulin.models.Review;
import ru.kalimulin.models.User;

public interface ReviewMapper {
    ReviewResponseDTO toReviewResponseDTO(Review review);
    Review toReview(ReviewCreateDTO reviewCreateDTO, User seller, User buyer);
}
