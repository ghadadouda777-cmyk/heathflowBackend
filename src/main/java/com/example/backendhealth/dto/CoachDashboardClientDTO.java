package com.example.backendhealth.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoachDashboardClientDTO {
    private String id;
    private String name;
    private String email;
    private List<String> assignedPlans;
    private String progressStatus;
}
