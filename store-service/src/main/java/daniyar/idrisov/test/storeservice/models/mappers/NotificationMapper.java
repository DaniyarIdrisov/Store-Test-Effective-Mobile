package daniyar.idrisov.test.storeservice.models.mappers;

import daniyar.idrisov.test.storeservice.models.dto.NotificationDTO;
import daniyar.idrisov.test.storeservice.models.dto.NotificationWithChildsDTO;
import daniyar.idrisov.test.storeservice.models.jpa.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {UserMapper.class})
public interface NotificationMapper {

    NotificationDTO toNotificationDTO(Notification notification);

    NotificationWithChildsDTO toNotificationWithChildsDTO(Notification notification);

}
