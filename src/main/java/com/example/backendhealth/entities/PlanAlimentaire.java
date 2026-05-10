package com.example.backendhealth.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "plans_alimentaires")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanAlimentaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "calories_journalieres")
    private Integer caloriesJournalieres;

    private String objectif;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "nutritionniste_id")
    private String nutritionnisteId;

    @Column(name = "regime_id")
    private Long regimeId;

    // ← ManyToOne badel OneToOne → plusieurs plans par consultation possible
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultation_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Consultation consultation;

    @OneToMany(mappedBy = "planAlimentaire", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Repas> repas = new ArrayList<>();
}