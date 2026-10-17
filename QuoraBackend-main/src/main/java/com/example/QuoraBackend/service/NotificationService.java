package com.example.QuoraBackend.service;

import com.example.QuoraBackend.model.Notification;
import com.example.QuoraBackend.model.NotificationType;
import com.example.QuoraBackend.model.User;
import com.example.QuoraBackend.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void createNotification(User receiver, String message, NotificationType type) {

        Notification notification = new Notification();
        notification.setUser(receiver);
        notification.setMessage(message);
        notification.setType(type);

        notificationRepository.save(notification);
    }
}
