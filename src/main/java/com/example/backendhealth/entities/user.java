package com.example.backendhealth.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class user {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String nom;
    private String prenom;
    private String email;
    private String pwd;
    private String tel;
    private String role;
    private boolean active;
    private String typeAbonnement;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPwd() { return pwd; }
    public void setPwd(String pwd) { this.pwd = pwd; }

    public String getTel() { return tel; }
    public void setTel(String tel) { this.tel = tel; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public String getTypeAbonnement() { return typeAbonnement; }
    public void setTypeAbonnement(String typeAbonnement) { this.typeAbonnement = typeAbonnement; }
}