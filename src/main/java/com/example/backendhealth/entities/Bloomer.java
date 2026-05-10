package com.example.backendhealth.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "bloomers")
@PrimaryKeyJoinColumn(name = "user_id")
public class Bloomer extends user {

    private Integer age;
    private Double height;
    private Double weight;
    private String goal;
    private String lifestyleLevel;

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

    public void setUserEntity(user newUser) {
    }
}