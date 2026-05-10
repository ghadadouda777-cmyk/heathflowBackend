package com.example.backendhealth.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ConsultationDTO {
    private Long id;
    private Long rendezVousId;
    private Long userId;
    private Long nutritionnisteId;
    private Long coachId;
    private Double poids;
    private Double taille;
    private Double imc;
    private String objectif;
    private Long planAlimentaireId;   // ← ID du plan lié (lecture seule)
    private String diagnostic;
    private String recommandations;
    private String notes;
    private LocalDateTime dateConsultation;
    private LocalDateTime prochainRdv;
}