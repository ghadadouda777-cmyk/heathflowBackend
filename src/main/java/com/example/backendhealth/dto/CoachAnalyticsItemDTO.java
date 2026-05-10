package com.example.backendhealth.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoachAnalyticsItemDTO {
    private String label;
    private String value;
    private String trend;
    private int fillPercent;
}
