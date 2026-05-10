package com.example.backendhealth.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "repas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Repas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_repas")
    private String typeRepas;

    @Column
    private String nom;

    @Column(columnDefinition = "TEXT")
    private String aliments;

    private Integer calories;
    private Double proteines;
    private Double glucides;
    private Double lipides;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_alimentaire_id")
    private PlanAlimentaire planAlimentaire;
}