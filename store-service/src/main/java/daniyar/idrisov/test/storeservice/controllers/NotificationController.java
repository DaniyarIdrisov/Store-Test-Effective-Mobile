package daniyar.idrisov.test.storeservice.controllers;

import daniyar.idrisov.test.storeservice.models.dto.NotificationCreateDTO;
import daniyar.idrisov.test.storeservice.models.dto.NotificationDTO;
import daniyar.idrisov.test.storeservice.models.dto.NotificationUpdateDTO;
import daniyar.idrisov.test.storeservice.models.dto.NotificationWithChildsDTO;
import daniyar.idrisov.test.storeservice.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService service;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public NotificationDTO createNotification(@Valid @RequestBody NotificationCreateDTO dto,
                                              @RequestParam("user_id") Long userId) {
        return service.createNotification(dto, userId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public NotificationDTO updateNotification(@RequestBody NotificationUpdateDTO dto,
                                              @PathVariable("id") Long notificationId) {
        return service.updateNotification(dto, notificationId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public NotificationDTO deleteNotification(@PathVariable("id") Long notificationId) {
        return service.deleteNotification(notificationId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public List<NotificationWithChildsDTO> getAllNotifications() {
        return service.getAllNotifications();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public NotificationWithChildsDTO getNotificationById(@PathVariable("id") Long notificationId) {
        return service.getNotificationById(notificationId);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/current")
    public List<NotificationWithChildsDTO> getCurrentNotifications() {
        return service.getCurrentNotifications();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}/user")
    public List<NotificationWithChildsDTO> getAllNotificationsByUserId(@PathVariable("id") Long userId) {
        return service.getAllNotificationsByUserId(userId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}/createdBy")
    public List<NotificationWithChildsDTO> getAllNotificationsByCreatedById(@PathVariable("id") Long createdById) {
        return service.getAllNotificationsByCreatedByUserId(createdById);
    }

}
