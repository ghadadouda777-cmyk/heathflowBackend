package com.example.backendhealth.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "nutritionists")
@PrimaryKeyJoinColumn(name = "user_id")
public class Nutritionist extends user {

    private String specialite;
    private String localisation;

    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }

    public String getLocalisation() { return localisation; }
    public void setLocalisation(String localisation) { this.localisation = localisation; }

    public void setUserEntity(user newUser) {
    }
}