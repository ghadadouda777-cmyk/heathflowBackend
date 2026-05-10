package com.example.backendhealth.dto;

import com.example.backendhealth.entities.Abonnement;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class AbonnementDTO {

    public static class PaymentRequest {
        private String nomCarte;
        private String numeroCarte;
        private String dateExpiration;
        private String cvv;
        private String typeAbonnement;

        public String getNomCarte() { return nomCarte; }
        public void setNomCarte(String nomCarte) { this.nomCarte = nomCarte; }

        public String getNumeroCarte() { return numeroCarte; }
        public void setNumeroCarte(String numeroCarte) { this.numeroCarte = numeroCarte; }

        public String getDateExpiration() { return dateExpiration; }
        public void setDateExpiration(String dateExpiration) { this.dateExpiration = dateExpiration; }

        public String getCvv() { return cvv; }
        public void setCvv(String cvv) { this.cvv = cvv; }

        public String getTypeAbonnement() { return typeAbonnement; }
        public void setTypeAbonnement(String typeAbonnement) { this.typeAbonnement = typeAbonnement; }
    }

    // ─── REQUEST ANCIEN ───────────────────────────────────────────────────────
    public static class CreatePaymentRequest {
        private String typeAbonnement;

        public String getTypeAbonnement() { return typeAbonnement; }
        public void setTypeAbonnement(String t) { this.typeAbonnement = t; }
    }

    public static class PaymentResponse {
        private boolean success;
        private String message;
        private String sessionId;
        private String typeAbonnement;
        private Double montant;

        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public String getSessionId() { return sessionId; }
        public void setSessionId(String sessionId) { this.sessionId = sessionId; }

        public String getTypeAbonnement() { return typeAbonnement; }
        public void setTypeAbonnement(String t) { this.typeAbonnement = t; }

        public Double getMontant() { return montant; }
        public void setMontant(Double montant) { this.montant = montant; }
    }

    public static class AbonnementResponse {
        private String id;
        private String typeAbonnement;
        private Double montant;
        private LocalDate dateDebut;
        private LocalDate dateFin;
        private String statut;
        private boolean actif;
        private long joursRestants;

        public static AbonnementResponse from(Abonnement a) {
            AbonnementResponse r = new AbonnementResponse();
            r.id = a.getId_abonnement();
            r.typeAbonnement = a.getType_abonnement().name();
            r.montant = a.getMontant();
            r.dateDebut = a.getDateDebut();
            r.dateFin = a.getDateFin();
            r.statut = a.getStatut().name();
            r.actif = a.estActif();
            if (a.getDateFin() != null) {
                r.joursRestants = ChronoUnit.DAYS.between(LocalDate.now(), a.getDateFin());
            }
            return r;
        }

        public String getId() { return id; }
        public String getTypeAbonnement() { return typeAbonnement; }
        public Double getMontant() { return montant; }
        public LocalDate getDateDebut() { return dateDebut; }
        public LocalDate getDateFin() { return dateFin; }
        public String getStatut() { return statut; }
        public boolean isActif() { return actif; }
        public long getJoursRestants() { return joursRestants; }
    }

    public static class StatutAbonnementResponse {
        private boolean hasActiveSubscription;
        private String typeAbonnement;
        private LocalDate dateFin;
        private long joursRestants;
        private String message;

        public boolean isHasActiveSubscription() { return hasActiveSubscription; }
        public void setHasActiveSubscription(boolean h) { this.hasActiveSubscription = h; }

        public String getTypeAbonnement() { return typeAbonnement; }
        public void setTypeAbonnement(String t) { this.typeAbonnement = t; }

        public LocalDate getDateFin() { return dateFin; }
        public void setDateFin(LocalDate d) { this.dateFin = d; }

        public long getJoursRestants() { return joursRestants; }
        public void setJoursRestants(long j) { this.joursRestants = j; }

        public String getMessage() { return message; }
        public void setMessage(String m) { this.message = m; }
    }
}