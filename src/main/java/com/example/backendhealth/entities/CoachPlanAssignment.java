package com.example.backendhealth.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "coach_plan_assignments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoachPlanAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String coachId;

    @Column(nullable = false)
    private String clientId;

    @Column(nullable = false)
    private Long planExerciceId;

    @Column(nullable = false)
    @Builder.Default
    private String progressStatus = "On Track";

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
