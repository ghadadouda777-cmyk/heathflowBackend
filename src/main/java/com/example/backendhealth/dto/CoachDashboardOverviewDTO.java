package com.example.backendhealth.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoachDashboardOverviewDTO {
    private long totalClients;
    private long activeWorkoutPlans;
    private long totalExercises;
    private long unreadMessages;
    private String subscriptionStatus;
    private long subscriptionRemainingDays;
}
