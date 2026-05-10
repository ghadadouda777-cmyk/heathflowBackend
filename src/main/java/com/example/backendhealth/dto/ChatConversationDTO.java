package com.example.backendhealth.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatConversationDTO {
    private Long id;
    private String participant1Id;
    private String participant2Id;
    private String type;
    private String lastMessage;
    private LocalDateTime lastMessageAt;
    private long unreadCount;
}
