package ru.kalimulin.service_Impl;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kalimulin.custum_exceptions.reviewException.ReviewAlreadyExistsException;
import ru.kalimulin.custum_exceptions.reviewException.ReviewNotFoundException;
import ru.kalimulin.custum_exceptions.userException.UnauthorizedException;
import ru.kalimulin.custum_exceptions.userException.UserNotFoundException;
import ru.kalimulin.entity_dto.reviewDTO.ReviewCreateDTO;
import ru.kalimulin.entity_dto.reviewDTO.ReviewResponseDTO;
import ru.kalimulin.mappers.reviewMapper.ReviewMapper;
import ru.kalimulin.models.Review;
import ru.kalimulin.models.User;
import ru.kalimulin.repositories.ListingRepository;
import ru.kalimulin.repositories.ReviewRepository;
import ru.kalimulin.repositories.UserRepository;
import ru.kalimulin.service.ReviewService;
import ru.kalimulin.util.ListingStatus;
import ru.kalimulin.util.SessionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ListingRepository listingRepository;
    private final ReviewMapper reviewMapper;

    public ReviewServiceImpl(ReviewRepository reviewRepository, UserRepository userRepository,
                             ListingRepository listingRepository, ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.listingRepository = listingRepository;
        this.reviewMapper = reviewMapper;
    }

    @Transactional
    @Override
    public ReviewResponseDTO leaveReview(Long sellerId, ReviewCreateDTO reviewCreateDTO, HttpSession session) {
        String buyerEmail = SessionUtils.getUserEmail(session);
        User buyer = userRepository.findByEmail(buyerEmail).get();

        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new UserNotFoundException("Продавец не найден"));

        boolean hasPurchased = listingRepository.existsBySellerAndStatusAndBuyer(seller, ListingStatus.SOLD, buyer);

        if (!hasPurchased) {
            throw new UnauthorizedException("Вы можете оставить отзыв только после покупки товара у этого продавца");
        }

        if (reviewRepository.existsByBuyerAndSeller(buyer, seller)) {
            throw new ReviewAlreadyExistsException("Вы уже оставили отзыв этому продавцу");
        }

        Review review = reviewMapper.toReview(reviewCreateDTO, seller, buyer);
        Review savedReview = reviewRepository.save(review);
        return reviewMapper.toReviewResponseDTO(savedReview);
    }

    @Override
    public List<ReviewResponseDTO> getSellerReviews(Long sellerId) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new UserNotFoundException("Продавец не найден"));

        return reviewRepository.findBySeller(seller).stream()
                .map(reviewMapper::toReviewResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public double getSellerAverageRating(Long sellerId) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new UserNotFoundException("Продавец не найден"));

        List<Review> reviews = reviewRepository.findBySeller(seller);
        if(reviews.isEmpty()) {
            return 0.0;
        }

        return reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }

    @Override
    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Отзыв не найден"));

        reviewRepository.delete(review);
    }
}
