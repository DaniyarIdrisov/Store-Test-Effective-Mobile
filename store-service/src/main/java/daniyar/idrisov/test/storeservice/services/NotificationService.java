package daniyar.idrisov.test.storeservice.services;

import daniyar.idrisov.test.storeservice.exceptions.NotificationNotFoundException;
import daniyar.idrisov.test.storeservice.exceptions.UserNotFoundException;
import daniyar.idrisov.test.storeservice.models.dto.NotificationCreateDTO;
import daniyar.idrisov.test.storeservice.models.dto.NotificationDTO;
import daniyar.idrisov.test.storeservice.models.dto.NotificationUpdateDTO;
import daniyar.idrisov.test.storeservice.models.dto.NotificationWithChildsDTO;
import daniyar.idrisov.test.storeservice.models.jpa.Notification;
import daniyar.idrisov.test.storeservice.models.jpa.User;
import daniyar.idrisov.test.storeservice.models.mappers.NotificationMapper;
import daniyar.idrisov.test.storeservice.repositories.NotificationRepository;
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
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final NotificationMapper notificationMapper;

    @Transactional
    public NotificationDTO createNotification(NotificationCreateDTO dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        Notification newNotification = Notification.builder()
                .header(dto.getHeader())
                .text(dto.getText())
                .user(user)
                .createdBy(userService.getCurrentUser())
                .build();
        Notification notification = notificationRepository.save(newNotification);
        return notificationMapper.toNotificationDTO(notification);
    }

    @Transactional
    public NotificationDTO updateNotification(NotificationUpdateDTO dto, Long notificationId) {
        Notification updateNotification = notificationRepository.findById(notificationId)
                .orElseThrow(NotificationNotFoundException::new);
        if (StringUtils.isNotBlank(dto.getHeader())) {
            updateNotification.setHeader(dto.getHeader());
        }
        if (StringUtils.isNotBlank(dto.getText())) {
            updateNotification.setText(dto.getText());
        }
        Notification notification = notificationRepository.save(updateNotification);
        return notificationMapper.toNotificationDTO(notification);
    }

    @Transactional
    public NotificationDTO deleteNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(NotificationNotFoundException::new);
        notificationRepository.delete(notification);
        return notificationMapper.toNotificationDTO(notification);
    }

    @Transactional
    public List<NotificationWithChildsDTO> getAllNotifications() {
        return notificationRepository.findAll().stream()
                .map(notificationMapper::toNotificationWithChildsDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public NotificationWithChildsDTO getNotificationById(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(NotificationNotFoundException::new);
        return notificationMapper.toNotificationWithChildsDTO(notification);
    }

    @Transactional
    public List<NotificationWithChildsDTO> getCurrentNotifications() {
        User currentUser = userService.getCurrentUser();
        return notificationRepository.findAllByUserId(currentUser.getId()).stream()
                .map(notificationMapper::toNotificationWithChildsDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<NotificationWithChildsDTO> getAllNotificationsByUserId(Long userId) {
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return notificationRepository.findAllByUserId(userId).stream()
                .map(notificationMapper::toNotificationWithChildsDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<NotificationWithChildsDTO> getAllNotificationsByCreatedByUserId(Long createdById) {
        userRepository.findById(createdById).orElseThrow(UserNotFoundException::new);
        return notificationRepository.findAllByCreatedById(createdById).stream()
                .map(notificationMapper::toNotificationWithChildsDTO)
                .collect(Collectors.toList());
    }

}
