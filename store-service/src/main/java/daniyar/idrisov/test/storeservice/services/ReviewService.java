package daniyar.idrisov.test.storeservice.services;

import daniyar.idrisov.test.storeservice.constants.NumericalConstants;
import daniyar.idrisov.test.storeservice.exceptions.*;
import daniyar.idrisov.test.storeservice.models.dto.ReviewCreateDTO;
import daniyar.idrisov.test.storeservice.models.dto.ReviewDTO;
import daniyar.idrisov.test.storeservice.models.dto.ReviewUpdateDTO;
import daniyar.idrisov.test.storeservice.models.dto.ReviewWithChildsDTO;
import daniyar.idrisov.test.storeservice.models.enumerated.Role;
import daniyar.idrisov.test.storeservice.models.jpa.Product;
import daniyar.idrisov.test.storeservice.models.jpa.Purchase;
import daniyar.idrisov.test.storeservice.models.jpa.Review;
import daniyar.idrisov.test.storeservice.models.jpa.User;
import daniyar.idrisov.test.storeservice.models.mappers.ReviewMapper;
import daniyar.idrisov.test.storeservice.repositories.ProductRepository;
import daniyar.idrisov.test.storeservice.repositories.PurchaseRepository;
import daniyar.idrisov.test.storeservice.repositories.ReviewRepository;
import daniyar.idrisov.test.storeservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final UserService userService;
    private final ProductRepository productRepository;
    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReviewDTO createReview(ReviewCreateDTO dto, Long productId) {
        checkEvaluation(dto.getEvaluation());
        Product product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
        User user = userService.getCurrentUser();
        checkForAvailability(user.getId(), product.getId());
        Review newReview = Review.builder()
                .evaluation(dto.getEvaluation())
                .comment(dto.getComment())
                .product(product)
                .createdBy(user)
                .build();
        Review review = reviewRepository.save(newReview);
        return reviewMapper.toReviewDTO(review);
    }

    private void checkEvaluation(Double evaluation) {
        if (evaluation <= NumericalConstants.MIN_EVALUATION
                || evaluation > NumericalConstants.MAX_EVALUATION) {
            throw new EvaluationIsIncorrectException();
        }
    }

    private void checkForAvailability(Long userId, Long productId) {
        if (reviewRepository.findByCreatedByIdAndProductId(userId, productId).isPresent()) {
            throw new ReviewAlreadyExistsException();
        }
        List<Purchase> purchases = purchaseRepository.findAllByUserIdAndProductId(userId, productId);
        if (purchases == null || purchases.isEmpty()) {
            throw new ProductNotPurchasedException();
        }
    }

    @Transactional
    public ReviewDTO updateReview(ReviewUpdateDTO dto, Long reviewId) {
        Review updateReview = reviewRepository.findById(reviewId)
                .orElseThrow(ReviewNotFoundException::new);
        checkEvaluation(dto.getEvaluation());
        checkForAttachment(updateReview);
        if (dto.getEvaluation() != null) {
            updateReview.setEvaluation(dto.getEvaluation());
        }
        if (StringUtils.isNotBlank(dto.getComment())) {
            updateReview.setComment(dto.getComment());
        }
        Review review = reviewRepository.save(updateReview);
        return reviewMapper.toReviewDTO(review);
    }

    @Transactional
    public ReviewDTO deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ReviewNotFoundException::new);
        checkForAttachment(review);
        reviewRepository.delete(review);
        return reviewMapper.toReviewDTO(review);
    }

    private void checkForAttachment(Review review) {
        User user = userService.getCurrentUser();
        if (!user.getId().equals(review.getCreatedBy().getId())
                && !user.getRole().equals(Role.ADMIN)) {
            throw new NoAttachmentException();
        }
    }


    @Transactional
    public List<ReviewWithChildsDTO> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(reviewMapper::toReviewWithChildsDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReviewWithChildsDTO getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ReviewNotFoundException::new);
        return reviewMapper.toReviewWithChildsDTO(review);
    }

    @Transactional
    public List<ReviewWithChildsDTO> getAllReviewsByUserId(Long userId) {
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return reviewRepository.findAllByCreatedById(userId).stream().
                map(reviewMapper::toReviewWithChildsDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ReviewWithChildsDTO> getAllReviewsByProductId(Long productId) {
        productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        return reviewRepository.findAllByProductId(productId).stream()
                .map(reviewMapper::toReviewWithChildsDTO)
                .collect(Collectors.toList());
    }

}
