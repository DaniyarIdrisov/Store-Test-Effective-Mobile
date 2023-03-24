package daniyar.idrisov.test.storeservice.service;

import daniyar.idrisov.test.storeservice.exceptions.EvaluationIsIncorrectException;
import daniyar.idrisov.test.storeservice.exceptions.ProductNotFoundException;
import daniyar.idrisov.test.storeservice.exceptions.ProductNotPurchasedException;
import daniyar.idrisov.test.storeservice.exceptions.ReviewAlreadyExistsException;
import daniyar.idrisov.test.storeservice.models.dto.ReviewCreateDTO;
import daniyar.idrisov.test.storeservice.models.dto.ReviewDTO;
import daniyar.idrisov.test.storeservice.models.dto.ReviewWithChildsDTO;
import daniyar.idrisov.test.storeservice.models.jpa.Product;
import daniyar.idrisov.test.storeservice.models.jpa.Purchase;
import daniyar.idrisov.test.storeservice.models.jpa.Review;
import daniyar.idrisov.test.storeservice.models.jpa.User;
import daniyar.idrisov.test.storeservice.models.mappers.ReviewMapper;
import daniyar.idrisov.test.storeservice.models.mappers.UserMapper;
import daniyar.idrisov.test.storeservice.repositories.ProductRepository;
import daniyar.idrisov.test.storeservice.repositories.PurchaseRepository;
import daniyar.idrisov.test.storeservice.repositories.ReviewRepository;
import daniyar.idrisov.test.storeservice.services.ReviewService;
import daniyar.idrisov.test.storeservice.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    private static final Long ID = 1L;
    private static Product PRODUCT;
    private static Review REVIEW;
    private static User USER;
    private static Purchase PURCHASE;
    private static final ReviewCreateDTO REVIEW_CREATE_DTO_1 = new ReviewCreateDTO();
    private static final ReviewCreateDTO REVIEW_CREATE_DTO_2 = new ReviewCreateDTO();

    @InjectMocks
    ReviewService reviewService;

    @Mock
    ReviewRepository reviewRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    UserService userService;

    @Mock
    PurchaseRepository purchaseRepository;

    @Spy
    ReviewMapper reviewMapper = Mappers.getMapper(ReviewMapper.class);

    @Spy
    UserMapper userMapper = Mappers.getMapper(UserMapper.class);


    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(
                reviewMapper,
                "userMapper",
                userMapper);
        PRODUCT = Product.builder()
                .id(ID)
                .name("Nike")
                .description("CCC")
                .build();
        REVIEW = Review.builder()
                .id(ID)
                .evaluation(5.0)
                .comment("AAA")
                .build();
        USER = User.builder()
                .id(ID)
                .email("A")
                .build();
        PURCHASE = Purchase.builder()
                .id(ID)
                .totalCost(525.4)
                .build();
        REVIEW_CREATE_DTO_1.setEvaluation(6.6);
        REVIEW_CREATE_DTO_1.setComment("AAA");
        REVIEW_CREATE_DTO_2.setEvaluation(5.0);
        REVIEW_CREATE_DTO_2.setComment("AAA");
    }

    @Test
    public void createReviewWithEvaluationIsIncorrectException() {
        assertThrows(EvaluationIsIncorrectException.class,
                () -> reviewService.createReview(REVIEW_CREATE_DTO_1, ID));
    }

    @Test
    public void createReviewWithProductNotFoundException() {
        when(productRepository.findById(ID))
                .thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class,
                () -> reviewService.createReview(REVIEW_CREATE_DTO_2, ID));
    }

    @Test
    public void createReviewWithReviewAlreadyExistsException() {
        when(productRepository.findById(ID))
                .thenReturn(Optional.of(PRODUCT));
        when(userService.getCurrentUser())
                .thenReturn(USER);
        when(reviewRepository.findByCreatedByIdAndProductId(ID, ID))
                .thenReturn(Optional.of(REVIEW));
        assertThrows(ReviewAlreadyExistsException.class,
                () -> reviewService.createReview(REVIEW_CREATE_DTO_2, ID));
    }

    @Test
    public void createReviewWithProductNotPurchasedException() {
        when(productRepository.findById(ID))
                .thenReturn(Optional.of(PRODUCT));
        when(userService.getCurrentUser())
                .thenReturn(USER);
        when(reviewRepository.findByCreatedByIdAndProductId(ID, ID))
                .thenReturn(Optional.empty());
        when(purchaseRepository.findAllByUserIdAndProductId(ID, ID))
                .thenReturn(Collections.emptyList());
        assertThrows(ProductNotPurchasedException.class,
                () -> reviewService.createReview(REVIEW_CREATE_DTO_2, ID));
    }

    @Test
    public void createReview() {
        when(productRepository.findById(ID))
                .thenReturn(Optional.of(PRODUCT));
        when(userService.getCurrentUser())
                .thenReturn(USER);
        when(reviewRepository.findByCreatedByIdAndProductId(ID, ID))
                .thenReturn(Optional.empty());
        when(purchaseRepository.findAllByUserIdAndProductId(ID, ID))
                .thenReturn(Collections.singletonList(PURCHASE));
        reviewService.createReview(REVIEW_CREATE_DTO_2, ID);
        ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository, times(1)).save(captor.capture());
        Review actual = captor.getValue();
        assertEquals(REVIEW_CREATE_DTO_2.getEvaluation(), actual.getEvaluation());
        assertEquals(REVIEW_CREATE_DTO_2.getComment(), actual.getComment());
    }

    @Test
    public void getAllReviews() {
        when(reviewRepository.findAll())
                .thenReturn(Collections.singletonList(REVIEW));
        List<ReviewWithChildsDTO> actual = reviewService.getAllReviews();
        assertEquals(actual.get(0).getId(), REVIEW.getId());
        assertEquals(actual.get(0).getEvaluation(), REVIEW.getEvaluation());
        assertEquals(actual.get(0).getComment(), REVIEW.getComment());
    }

}
