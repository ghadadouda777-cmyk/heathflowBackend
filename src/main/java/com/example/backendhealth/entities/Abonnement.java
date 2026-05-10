package com.example.backendhealth.entities;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "abonnements")
public class Abonnement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id_abonnement;

    @Enumerated(EnumType.STRING)
    private TypeAbonnement type_abonnement;

    private Double montant;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    private StatutAbonnement statut;

    private String stripeSessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private user userEntity;

    // ─── ENUMS ────────────────────────────────────────────────────────────────
    public enum TypeAbonnement {
        MOIS_1(300.0, 1),
        MOIS_3(750.0, 3),
        MOIS_6(1320.0, 6),
        AN_1(2160.0, 12);

        public final double prix;
        public final int dureeEnMois;

        TypeAbonnement(double prix, int dureeEnMois) {
            this.prix = prix;
            this.dureeEnMois = dureeEnMois;
        }
    }

    public enum StatutAbonnement {
        ACTIF, EXPIRE, EN_ATTENTE, ANNULE
    }

    // ─── CONSTRUCTEURS ────────────────────────────────────────────────────────
    public Abonnement() {}

    // ─── GETTERS / SETTERS ────────────────────────────────────────────────────
    public String getId_abonnement() { return id_abonnement; }
    public void setId_abonnement(String id_abonnement) { this.id_abonnement = id_abonnement; }

    public TypeAbonnement getType_abonnement() { return type_abonnement; }
    public void setType_abonnement(TypeAbonnement type_abonnement) { this.type_abonnement = type_abonnement; }

    public Double getMontant() { return montant; }
    public void setMontant(Double montant) { this.montant = montant; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public StatutAbonnement getStatut() { return statut; }
    public void setStatut(StatutAbonnement statut) { this.statut = statut; }

    public String getStripeSessionId() { return stripeSessionId; }
    public void setStripeSessionId(String stripeSessionId) { this.stripeSessionId = stripeSessionId; }

    public user getUserEntity() { return userEntity; }
    public void setUserEntity(user userEntity) { this.userEntity = userEntity; }

    // ─── MÉTHODES MÉTIER ──────────────────────────────────────────────────────
    public boolean estActif() {
        return statut == StatutAbonnement.ACTIF
                && dateFin != null
                && LocalDate.now().isBefore(dateFin);
    }
}