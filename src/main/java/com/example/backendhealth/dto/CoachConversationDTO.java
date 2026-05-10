package com.example.backendhealth.dto;

import com.example.backendhealth.entities.CoachConversation.ConversationStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoachConversationDTO {
    private Long id;
    private String coachId;
    private String clientId;
    private ConversationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CoachMessageDTO> messages;
    private String clientName;
    private String lastMessagePreview;
    private LocalDateTime lastMessageAt;
    private long unreadCount;
}
