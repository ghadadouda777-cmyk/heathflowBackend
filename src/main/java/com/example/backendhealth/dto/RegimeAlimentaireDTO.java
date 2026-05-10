package com.example.backendhealth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegimeAlimentaireDTO {

    private Long id;

    private String nom;

    private String description;

    private String type;

    private Integer caloriesRecommandees;

    private String alimentsAutorises;

    private String alimentsInterdits;

    private Long userId;
}
