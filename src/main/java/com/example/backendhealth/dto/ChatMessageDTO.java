package com.example.backendhealth.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDTO {
    private Long id;
    private Long conversationId;
    private String senderId;
    private String receiverId;
    private String content;
    private LocalDateTime sentAt;
    private boolean isRead;
}
