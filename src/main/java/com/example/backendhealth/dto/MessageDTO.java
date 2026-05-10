package com.example.backendhealth.dto;

import com.example.backendhealth.entities.Message.SenderRole;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDTO {
    private Long id;
    private Long conversationId;
    private Long senderId;
    private SenderRole senderRole;
    private String content;
    private boolean isRead;
    private LocalDateTime sentAt;
}