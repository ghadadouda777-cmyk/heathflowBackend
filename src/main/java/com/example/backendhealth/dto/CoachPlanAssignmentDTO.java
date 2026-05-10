package com.example.backendhealth.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoachPlanAssignmentDTO {
    private Long id;
    private String coachId;
    private String clientId;
    private Long planExerciceId;
    private String progressStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
