        package com.example.backendhealth.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "regimes_alimentaires")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegimeAlimentaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String type;

    @Column(name = "calories_recommandees")
    private Integer caloriesRecommandees;

    @Column(name = "aliments_autorises", columnDefinition = "TEXT")
    private String alimentsAutorises;

    @Column(name = "aliments_interdits", columnDefinition = "TEXT")
    private String alimentsInterdits;

    @Column(name = "user_id")
    private Long userId;
}
