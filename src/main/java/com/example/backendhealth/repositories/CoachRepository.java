package com.example.backendhealth.repositories;

import com.example.backendhealth.entities.Coach;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CoachRepository extends JpaRepository<Coach, String> {
    Optional<Coach> findByEmail(String email);
    List<Coach> findByNomContainingIgnoreCase(String nom);
    List<Coach> findByPrenomContainingIgnoreCase(String prenom);
}