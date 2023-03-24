package daniyar.idrisov.test.storeservice.repositories;

import daniyar.idrisov.test.storeservice.models.enumerated.OrganizationState;
import daniyar.idrisov.test.storeservice.models.enumerated.ProductState;
import daniyar.idrisov.test.storeservice.models.jpa.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByStateAndOrganizationState(ProductState productState,
                                                     OrganizationState organizationState,
                                                     Pageable pageable);

}
