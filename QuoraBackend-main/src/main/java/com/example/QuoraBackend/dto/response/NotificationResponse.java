package com.example.QuoraBackend.dto.response;

import com.example.QuoraBackend.model.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class NotificationResponse {

    private Long id;
    private String message;
    private NotificationType type;
    private boolean isRead;
    private LocalDateTime createdAt;
}
