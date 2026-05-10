package com.example.backendhealth.dto;

public class RegisterDTO {

    // Communs
    private String nom;
    private String prenom;
    private String email;
    private String pwd;
    private String tel;
    private String role;
    private String typeAbonnement;

    // NUTRITIONIST
    private String specialite;
    private String localisation;

    // COACH
    private String coachSpecialite;
    private String certifications;

    // BLOOMER
    private Integer age;
    private Double height;
    private Double weight;
    private String goal;
    private String lifestyleLevel;

    // ===== GETTERS & SETTERS =====

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

    public String getTypeAbonnement() { return typeAbonnement; }
    public void setTypeAbonnement(String typeAbonnement) { this.typeAbonnement = typeAbonnement; }

    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }

    public String getLocalisation() { return localisation; }
    public void setLocalisation(String localisation) { this.localisation = localisation; }

    public String getCoachSpecialite() { return coachSpecialite; }
    public void setCoachSpecialite(String coachSpecialite) { this.coachSpecialite = coachSpecialite; }

    public String getCertifications() { return certifications; }
    public void setCertifications(String certifications) { this.certifications = certifications; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public Double getHeight() { return height; }
    public void setHeight(Double height) { this.height = height; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    public String getGoal() { return goal; }
    public void setGoal(String goal) { this.goal = goal; }

    public String getLifestyleLevel() { return lifestyleLevel; }
    public void setLifestyleLevel(String lifestyleLevel) { this.lifestyleLevel = lifestyleLevel; }
}