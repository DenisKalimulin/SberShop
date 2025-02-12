package ru.kalimulin.mappers.reviewMapper;

import org.springframework.stereotype.Component;
import ru.kalimulin.entity_dto.reviewDTO.ReviewCreateDTO;
import ru.kalimulin.entity_dto.reviewDTO.ReviewResponseDTO;
import ru.kalimulin.models.Review;
import ru.kalimulin.models.User;

import java.time.LocalDateTime;

@Component
public class ReviewMapperImpl implements ReviewMapper {

    @Override
    public ReviewResponseDTO toReviewResponseDTO(Review review) {

        if(review == null) {
            return null;
        }

        return ReviewResponseDTO.builder()
                .id(review.getId())
                .sellerEmail(review.getSeller().getEmail())
                .buyerEmail(review.getBuyer().getEmail())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }

    @Override
    public Review toReview(ReviewCreateDTO reviewCreateDTO, User seller, User buyer) {

        if(reviewCreateDTO == null || seller == null || buyer == null) {
            return null;
        }

        return Review.builder()
                .seller(seller)
                .buyer(buyer)
                .rating(reviewCreateDTO.getRating())
                .comment(reviewCreateDTO.getComment())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
