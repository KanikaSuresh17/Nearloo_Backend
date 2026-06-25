package com.nearloo.nearloo_backend.service;

import com.nearloo.nearloo_backend.entity.*;
import com.nearloo.nearloo_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepo;
    private final UserRepository userRepo;

    // Send notification to all admins
    public void notifyAdmin(Toilet toilet, String message) {
        List<User> admins = userRepo.findByRole("admin");
        admins.forEach(admin -> {
            Notification notification = Notification.builder()
                    .user(admin)
                    .toilet(toilet)
                    .message(message)
                    .isRead(false)
                    .build();
            notificationRepo.save(notification);
        });
    }

    public List<Notification> getAdminNotifications(UUID adminId) {
        return notificationRepo.findByUserIdOrderByCreatedAtDesc(adminId);
    }

    public void markAsRead(UUID notificationId) {
        notificationRepo.findById(notificationId).ifPresent(n -> {
            n.setIsRead(true);
            notificationRepo.save(n);
        });
    }
}
