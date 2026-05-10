package com.example.backendhealth.repositories;

import com.example.backendhealth.entities.PlanExercice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PlanExerciceRepository extends JpaRepository<PlanExercice, Long> {

    List<PlanExercice> findByActif(Boolean actif);

    List<PlanExercice> findByCoachId(String coachId);

    List<PlanExercice> findByCoachIdAndActif(String coachId, Boolean actif);
}