package com.example.backendhealth.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "plan_exercice")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanExercice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    private String description;
    private Integer dureeSemaines;
    private Integer seancesParSemaine;
    private Boolean actif;

    private String coachId;

    @Column(nullable = false)
    private LocalDate dateDebut;

    private LocalDate dateFin;

    @OneToMany(mappedBy = "planExercice", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Exercice> exercices = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}