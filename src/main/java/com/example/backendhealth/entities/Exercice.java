package com.example.backendhealth.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "exercice")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exercice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    private String description;
    private Integer series;
    private Integer repetitions;
    private Integer dureeSecondes;
    private Integer tempsReposSecondes;
    private Double poidsKg;

    private String coachId;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "plan_exercice_id", nullable = true)
    private PlanExercice planExercice;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}