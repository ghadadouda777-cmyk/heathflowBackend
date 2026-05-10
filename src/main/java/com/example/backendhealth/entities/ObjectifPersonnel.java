package com.example.backendhealth.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "objectif_personnel")

public class ObjectifPersonnel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer objectif_coupes_eau;
    private Integer objectif_heures_sommeil;
    private Integer objectif_exercices_semaine;
    private Integer objectif_calories;
    private Integer objectif_proteines;
    @OneToOne
    @JoinColumn(name = "user_id")
    private user user;


    public user getUser() { return user; }
    public void setUser(user user) { this.user = user; }
    public ObjectifPersonnel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getObjectif_coupes_eau() {
        return objectif_coupes_eau;
    }

    public void setObjectif_coupes_eau(Integer objectif_coupes_eau) {
        this.objectif_coupes_eau = objectif_coupes_eau;
    }

    public Integer getObjectif_heures_sommeil() {
        return objectif_heures_sommeil;
    }

    public void setObjectif_heures_sommeil(Integer objectif_heures_sommeil) {
        this.objectif_heures_sommeil = objectif_heures_sommeil;
    }

    public Integer getObjectif_exercices_semaine() {
        return objectif_exercices_semaine;
    }

    public void setObjectif_exercices_semaine(Integer objectif_exercices_semaine) {
        this.objectif_exercices_semaine = objectif_exercices_semaine;
    }

    public Integer getObjectif_calories() {
        return objectif_calories;
    }

    public void setObjectif_calories(Integer objectif_calories) {
        this.objectif_calories = objectif_calories;
    }

    public Integer getObjectif_proteines() {
        return objectif_proteines;
    }

    public void setObjectif_proteines(Integer objectif_proteines) {
        this.objectif_proteines = objectif_proteines;
    }
}
