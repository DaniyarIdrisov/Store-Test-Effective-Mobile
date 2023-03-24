package daniyar.idrisov.test.storeservice.repositories;

import daniyar.idrisov.test.storeservice.models.jpa.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    List<Purchase> findAllByUserIdAndProductId(Long createdById, Long productId);

    List<Purchase> findAllByPossibleToReturnIsTrueAndReturnedIsFalseAndCreatedAtBefore(Instant instant);

    List<Purchase> findAllByUserId(Long userId);

    List<Purchase> findAllByProductId(Long productId);

}
