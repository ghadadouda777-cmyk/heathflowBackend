package com.example.backendhealth.controllers;

import com.example.backendhealth.dto.NotificationDTO;
import com.example.backendhealth.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService notifService;

    /** GET /api/notifications/{userId} — all notifications for a user */
    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationDTO>> getAll(@PathVariable String userId) {
        return ResponseEntity.ok(notifService.getAll(userId));
    }

    /** GET /api/notifications/{userId}/unread-count */
    @GetMapping("/{userId}/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(@PathVariable String userId) {
        return ResponseEntity.ok(Map.of("count", notifService.getUnreadCount(userId)));
    }

    /** PATCH /api/notifications/{id}/read — mark one as read */
    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notifService.markAsRead(id);
        return ResponseEntity.noContent().build();
    }

    /** PATCH /api/notifications/{userId}/read-all — mark all as read */
    @PatchMapping("/{userId}/read-all")
    public ResponseEntity<Void> markAllAsRead(@PathVariable String userId) {
        notifService.markAllAsRead(userId);
        return ResponseEntity.noContent().build();
    }
}
