package daniyar.idrisov.test.storeservice.repositories;

import daniyar.idrisov.test.storeservice.models.enumerated.OrganizationState;
import daniyar.idrisov.test.storeservice.models.jpa.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    List<Organization> findAllByState(OrganizationState state);

    Optional<Organization> findByName(String name);

    List<Organization> findAllByCreatedById(Long createdById);

}
