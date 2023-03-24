package daniyar.idrisov.test.storeservice.controllers;

import daniyar.idrisov.test.storeservice.models.dto.ReviewCreateDTO;
import daniyar.idrisov.test.storeservice.models.dto.ReviewDTO;
import daniyar.idrisov.test.storeservice.models.dto.ReviewUpdateDTO;
import daniyar.idrisov.test.storeservice.models.dto.ReviewWithChildsDTO;
import daniyar.idrisov.test.storeservice.services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService service;

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ReviewDTO createReview(@RequestBody ReviewCreateDTO dto,
                                  @RequestParam("product_id") Long productId) {
        return service.createReview(dto, productId);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ReviewDTO updateReview(@RequestBody ReviewUpdateDTO dto,
                                  @PathVariable("id") Long reviewId) {
        return service.updateReview(dto, reviewId);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ReviewDTO deleteReview(@PathVariable("id") Long reviewId) {
        return service.deleteReview(reviewId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public List<ReviewWithChildsDTO> getAllReviews() {
        return service.getAllReviews();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ReviewWithChildsDTO getReviewById(@PathVariable("id") Long reviewId) {
        return service.getReviewById(reviewId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}/user")
    public List<ReviewWithChildsDTO> getAllReviewsByUserId(@PathVariable("id") Long userId) {
        return service.getAllReviewsByUserId(userId);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/product")
    public List<ReviewWithChildsDTO> getAllReviewsByProductId(@PathVariable("id") Long productId) {
        return service.getAllReviewsByProductId(productId);
    }

}
