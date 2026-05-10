package com.example.backendhealth.dto;

import com.example.backendhealth.entities.CoachMessage.SenderRole;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoachMessageDTO {
    private Long id;
    private Long conversationId;
    private String senderId;
    private SenderRole senderRole;
    private String content;
    private boolean isRead;
    private LocalDateTime sentAt;
}
