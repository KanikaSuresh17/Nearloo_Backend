package com.nearloo.nearloo_backend.controller;

import com.nearloo.nearloo_backend.repository.UserRepository;
import com.nearloo.nearloo_backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserRepository userRepo;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAll(Authentication auth) {
        UUID adminId = userRepo.findByEmail(auth.getName())
                .orElseThrow().getId();
        return ResponseEntity.ok(
                notificationService.getAdminNotifications(adminId));
    }

    @PutMapping("/{id}/read")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> markRead(@PathVariable UUID id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok("Marked as read");
    }
}
