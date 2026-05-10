package com.example.backendhealth.repositories;

import com.example.backendhealth.entities.Exercice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExerciceRepository extends JpaRepository<Exercice, Long> {

    List<Exercice> findByPlanExerciceId(Long planId);

    long countByPlanExerciceId(Long planId);

    List<Exercice> findByCoachId(String coachId);

    long countByCoachId(String coachId);
}