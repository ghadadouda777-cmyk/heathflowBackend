package com.example.backendhealth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciceDto {

    private Long id;
    private String nom;
    private String description;

    private Integer series;
    private Integer repetitions;

    private Integer dureeSecondes;
    private Integer tempsReposSecondes;

    private Double poidsKg;

    private String coachId;

    private Long planExerciceId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}