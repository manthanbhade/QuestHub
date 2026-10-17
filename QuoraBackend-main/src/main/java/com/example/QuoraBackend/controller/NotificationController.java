package com.example.QuoraBackend.controller;

import com.example.QuoraBackend.dto.response.NotificationResponse;
import com.example.QuoraBackend.dto.response.UnreadCountResponse;
import com.example.QuoraBackend.model.Notification;
import com.example.QuoraBackend.model.User;
import com.example.QuoraBackend.repository.NotificationRepository;
import com.example.QuoraBackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final UserService userService;

    @GetMapping
    public List<NotificationResponse> getMyNotifications() {

        User currentUser = userService.getCurrentAuthenticatedUser();

        return notificationRepository
                .findByUserOrderByCreatedAtDesc(currentUser)
                .stream()
                .map(notification -> new NotificationResponse(
                        notification.getId(),
                        notification.getMessage(),
                        notification.getType(),
                        notification.isRead(),
                        notification.getCreatedAt()
                ))
                .toList();
    }

    @GetMapping("/unread-count")
    public UnreadCountResponse getUnreadCount() {

        User currentUser = userService.getCurrentAuthenticatedUser();

        long count = notificationRepository
                .countByUserAndIsReadFalse(currentUser);

        return new UnreadCountResponse(count);
    }

    // mark single notification read
    @PutMapping("/{id}/read")
    public void markAsRead(@PathVariable Long id) {

        User currentUser = userService.getCurrentAuthenticatedUser();

        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        // Security check → user can only mark their own notification
        if (!notification.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new RuntimeException("Unauthorized access");
        }

        notification.setRead(true);
        notificationRepository.save(notification);
    }
// mark all notification read
@PutMapping("/read-all")
public void markAllAsRead() {

    User currentUser = userService.getCurrentAuthenticatedUser();

    List<Notification> notifications =
            notificationRepository.findByUserOrderByCreatedAtDesc(currentUser);

    notifications.forEach(notification -> notification.setRead(true));

    notificationRepository.saveAll(notifications);
}
}
