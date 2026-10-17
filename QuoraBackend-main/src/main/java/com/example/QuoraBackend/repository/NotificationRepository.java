package com.example.QuoraBackend.repository;

import com.example.QuoraBackend.model.Notification;
import com.example.QuoraBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserOrderByCreatedAtDesc(User user);

    long countByUserAndIsReadFalse(User user);
}
