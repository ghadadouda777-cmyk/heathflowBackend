package com.example.backendhealth.dto;

import lombok.Data;

@Data
public class RepasDTO {
    private Long id;
    private String typeRepas;       // Petit-déjeuner, Déjeuner, Dîner...
    private String nom;
    private String aliments;        // "Poulet grillé, riz, salade"
    private Integer calories;
    private Double proteines;
    private Double glucides;
    private Double lipides;
    private String notes;
    private Long planAlimentaireId;
}