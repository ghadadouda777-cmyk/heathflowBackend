package com.example.backendhealth.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
public class Suivi_quotidien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_suivi;

    private LocalDate date;
    private Integer nb_coupes_bues;
    private Integer nb_exercices_faites;
    private Integer nb_heures_sommeil;
    private Integer calories_consommes;
    private Integer proteines_consommes;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private user user;
    public Suivi_quotidien() {
    }
    public user getUser() { return user; }
    public void setUser(user user) { this.user = user; }
    public Integer getId_suivi() {
        return id_suivi;
    }

    public void setId_suivi(Integer id_suivi) {
        this.id_suivi = id_suivi;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getNb_coupes_bues() {
        return nb_coupes_bues;
    }

    public void setNb_coupes_bues(Integer nb_coupes_bues) {
        this.nb_coupes_bues = nb_coupes_bues;
    }

    public Integer getNb_exercices_faites() {
        return nb_exercices_faites;
    }

    public void setNb_exercices_faites(Integer nb_exercices_faites) {
        this.nb_exercices_faites = nb_exercices_faites;
    }

    public Integer getNb_heures_sommeil() {
        return nb_heures_sommeil;
    }

    public void setNb_heures_sommeil(Integer nb_heures_sommeil) {
        this.nb_heures_sommeil = nb_heures_sommeil;
    }

    public Integer getProteines_consommes() {
        return proteines_consommes;
    }

    public void setProteines_consommes(Integer proteines_consommes) {
        this.proteines_consommes = proteines_consommes;
    }

    public Integer getCalories_consommes() {
        return calories_consommes;
    }

    public void setCalories_consommes(Integer calories_consommes) {
        this.calories_consommes = calories_consommes;
    }
}
