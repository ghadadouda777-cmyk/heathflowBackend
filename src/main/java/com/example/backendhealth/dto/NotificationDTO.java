package com.example.backendhealth.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDTO {
    private Long id;
    private String userId;
    private String type;
    private String title;
    private String message;
    private boolean read;
    private LocalDateTime createdAt;
    private String relatedId;
}
