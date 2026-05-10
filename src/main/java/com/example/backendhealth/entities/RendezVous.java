package com.example.backendhealth.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "rendez_vous")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RendezVous {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // RDV garde Long pour son propre ID

    @Column(name = "date_heure", nullable = false)
    private LocalDateTime dateHeure;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutRendezVous statut = StatutRendezVous.EN_ATTENTE;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_intervenant")
    private TypeIntervenant typeIntervenant;

    private String motif;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "duree_minutes")
    private Integer dureeMinutes = 60;

    // ✅ String car user utilise UUID
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "nutritionniste_id")
    private String nutritionnisteId;

    @Column(name = "coach_id")
    private String coachId;

    public enum StatutRendezVous {
        EN_ATTENTE, CONFIRME, REFUSE, ANNULE, TERMINE
    }

    public enum TypeIntervenant {
        NUTRITIONNISTE, COACH, LES_DEUX
    }
}