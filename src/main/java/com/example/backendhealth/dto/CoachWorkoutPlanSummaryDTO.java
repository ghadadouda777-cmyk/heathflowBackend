package com.example.backendhealth.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoachWorkoutPlanSummaryDTO {
    private Long id;
    private String nom;
    private Integer dureeSemaines;
    private Integer seancesParSemaine;
    private long assignedClientsCount;
    private int exercisesCount;
}
