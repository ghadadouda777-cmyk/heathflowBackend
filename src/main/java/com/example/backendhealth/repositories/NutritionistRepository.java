package com.example.backendhealth.repositories;

import com.example.backendhealth.entities.Nutritionist;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface NutritionistRepository extends JpaRepository<Nutritionist, String> {
    Optional<Nutritionist> findByEmail(String email);
    List<Nutritionist> findByNomContainingIgnoreCase(String nom);
    List<Nutritionist> findByPrenomContainingIgnoreCase(String prenom);
    List<Nutritionist> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom);
}