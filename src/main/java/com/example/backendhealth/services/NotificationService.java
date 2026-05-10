package com.example.backendhealth.services;

import com.example.backendhealth.dto.NotificationDTO;
import com.example.backendhealth.entities.Notification;
import com.example.backendhealth.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notifRepo;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Save a notification to DB and push it in real-time via WebSocket.
     */
    @Transactional
    public NotificationDTO send(String userId, String type, String title, String message, String relatedId) {
        Notification notif = Notification.builder()
                .userId(userId)
                .type(type)
                .title(title)
                .message(message)
                .relatedId(relatedId)
                .read(false)
                .build();

        Notification saved = notifRepo.save(notif);
        NotificationDTO dto = toDTO(saved);

        // Push real-time to user's private queue
        try {
            messagingTemplate.convertAndSendToUser(userId, "/queue/notifications", dto);
        } catch (Exception e) {
            // WebSocket push failure is non-fatal — notification is already persisted
        }

        return dto;
    }

    @Transactional(readOnly = true)
    public List<NotificationDTO> getAll(String userId) {
        return notifRepo.findByUserIdOrderByCreatedAtDesc(userId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long getUnreadCount(String userId) {
        return notifRepo.countByUserIdAndReadFalse(userId);
    }

    @Transactional
    public void markAsRead(Long notifId) {
        notifRepo.findById(notifId).ifPresent(n -> {
            n.setRead(true);
            notifRepo.save(n);
        });
    }

    @Transactional
    public void markAllAsRead(String userId) {
        notifRepo.markAllAsRead(userId);
    }

    private NotificationDTO toDTO(Notification n) {
        return NotificationDTO.builder()
                .id(n.getId())
                .userId(n.getUserId())
                .type(n.getType())
                .title(n.getTitle())
                .message(n.getMessage())
                .read(n.isRead())
                .createdAt(n.getCreatedAt())
                .relatedId(n.getRelatedId())
                .build();
    }
}
