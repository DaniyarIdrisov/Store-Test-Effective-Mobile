package daniyar.idrisov.test.storeservice.repositories;

import daniyar.idrisov.test.storeservice.models.jpa.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByCreatedByIdAndProductId(Long createdById, Long productId);

    List<Review> findAllByCreatedById(Long createdById);

    List<Review> findAllByProductId(Long productId);

}
