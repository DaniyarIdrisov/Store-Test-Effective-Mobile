package daniyar.idrisov.test.storeservice.repositories;

import daniyar.idrisov.test.storeservice.models.jpa.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByUserId(Long userId);

    List<Notification> findAllByCreatedById(Long createdById);

}
