package com.example.backendhealth.dto;

import com.example.backendhealth.entities.RendezVous.StatutRendezVous;
import com.example.backendhealth.entities.RendezVous.TypeIntervenant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RendezVousDTO {
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateHeure;

    private StatutRendezVous statut;
    private TypeIntervenant typeIntervenant;
    private String motif;
    private String notes;
    private Integer dureeMinutes;

    private String userId;
    private String nutritionnisteId;
    private String coachId;

    // ✅ Noms alignés avec ce que le Service appelle
    private String nutritionnistNom;
    private String nutritionnistPrenom;
    private String coachNom;
    private String coachPrenom;
    private String patientNom;

    // ✅ Plus de setters manuels — Lombok s'en occupe
}