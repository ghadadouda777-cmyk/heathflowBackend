package com.example.backendhealth.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoachNotificationDTO {
    private String type;
    private String text;
}
