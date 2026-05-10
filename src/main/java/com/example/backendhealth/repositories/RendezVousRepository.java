package com.example.backendhealth.repositories;

import com.example.backendhealth.entities.RendezVous;
import com.example.backendhealth.entities.RendezVous.StatutRendezVous;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {

    // ✅ String car UUID
    List<RendezVous> findByUserId(String userId);
    List<RendezVous> findByNutritionnisteId(String nutritionnisteId);
    List<RendezVous> findByCoachId(String coachId);
    List<RendezVous> findByStatut(StatutRendezVous statut);
    List<RendezVous> findByUserIdAndStatut(String userId, StatutRendezVous statut);
    List<RendezVous> findByDateHeureBetween(LocalDateTime debut, LocalDateTime fin);
    List<RendezVous> findByNutritionnisteIdAndDateHeureBetween(String nutritionnisteId, LocalDateTime debut, LocalDateTime fin);
    List<RendezVous> findByCoachIdAndDateHeureBetween(String coachId, LocalDateTime debut, LocalDateTime fin);
}