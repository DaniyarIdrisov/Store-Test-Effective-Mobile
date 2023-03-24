package daniyar.idrisov.test.storeservice.repositories;

import daniyar.idrisov.test.storeservice.models.jpa.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {

    @Query("SELECT d FROM Discount as d LEFT JOIN d.products as p WHERE p.id = :product_id")
    List<Discount> findAllByProductId(@Param("product_id") Long productId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "DELETE FROM {h-schema}discount_products AS dp WHERE dp.discount_id = :discountId",
            nativeQuery = true)
    void deleteDiscountProducts(Long discountId);

}
