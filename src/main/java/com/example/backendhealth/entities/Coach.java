package com.example.backendhealth.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "coaches")
@PrimaryKeyJoinColumn(name = "user_id")
public class Coach extends user {

    private String coachSpecialite;
    private String certifications;

    public String getCoachSpecialite() { return coachSpecialite; }
    public void setCoachSpecialite(String coachSpecialite) { this.coachSpecialite = coachSpecialite; }

    public String getCertifications() { return certifications; }
    public void setCertifications(String certifications) { this.certifications = certifications; }
}