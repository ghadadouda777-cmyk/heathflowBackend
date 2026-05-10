package com.example.backendhealth.repositories;

import com.example.backendhealth.entities.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    Optional<Consultation> findByRendezVousId(Long rendezVousId);
    List<Consultation> findByUserId(Long userId);
    List<Consultation> findByNutritionnisteId(Long nutritionnisteId);
    List<Consultation> findByCoachId(Long coachId);   // ← was missing
}