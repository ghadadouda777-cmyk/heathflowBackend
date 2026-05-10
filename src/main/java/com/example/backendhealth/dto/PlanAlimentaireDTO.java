package com.example.backendhealth.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class PlanAlimentaireDTO {
    private Long id;
    private String nom;
    private String description;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Integer caloriesJournalieres;
    private String objectif;

    private String userId;           // ✅ String UUID (pas Long)
    private String nutritionnisteId; // ✅ String UUID (pas Long)

    private Long regimeId;
    private Long consultationId;
    private Long rendezVousId;       // ✅ ajouté pour le lien RDV

    private List<RepasDTO> repas;
}