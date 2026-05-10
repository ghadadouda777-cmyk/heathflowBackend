package com.example.backendhealth.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "consultations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "plansAlimentaires")
public class Consultation {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rendez_vous_id", unique = true, nullable = false)
    private Long rendezVousId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "nutritionniste_id")
    private Long nutritionnisteId;

    @Column(name = "coach_id")
    private Long coachId;

    private Double poids;
    private Double taille;
    private Double imc;
    private String objectif;

    @Column(columnDefinition = "TEXT")
    private String diagnostic;

    @Column(columnDefinition = "TEXT")
    private String recommandations;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "consultation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PlanAlimentaire> plansAlimentaires;

    @Column(name = "date_consultation")
    private LocalDateTime dateConsultation;

    @Column(name = "prochain_rdv")
    private LocalDateTime prochainRdv;

    @PrePersist
    public void prePersist() {
        if (this.dateConsultation == null) {
            this.dateConsultation = LocalDateTime.now();
        }
        calculerImc();
    }

    @PreUpdate
    public void preUpdate() {
        calculerImc();
    }

    private void calculerImc() {
        if (this.poids != null && this.taille != null && this.taille > 0) {
            double tailleM = this.taille / 100.0;
            this.imc = Math.round((this.poids / (tailleM * tailleM)) * 100.0) / 100.0;
        }
    }
}