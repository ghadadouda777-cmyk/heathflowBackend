package com.example.backendhealth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanExerciceDto {

    private Long id;

    private String nom;
    private String description;

    private Integer dureeSemaines;
    private Integer seancesParSemaine;

    private Boolean actif;

    private String coachId;

    private LocalDate dateDebut;
    private LocalDate dateFin;

    private List<ExerciceDto> exercices;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}