package com.example.backendhealth.repositories;

import com.example.backendhealth.entities.CoachPlanAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CoachPlanAssignmentRepository extends JpaRepository<CoachPlanAssignment, Long> {
    List<CoachPlanAssignment> findByCoachId(String coachId);
    List<CoachPlanAssignment> findByCoachIdAndClientId(String coachId, String clientId);
    long countByCoachId(String coachId);
    long countByCoachIdAndPlanExerciceId(String coachId, Long planExerciceId);
    Optional<CoachPlanAssignment> findByCoachIdAndClientIdAndPlanExerciceId(String coachId, String clientId, Long planExerciceId);
    void deleteByPlanExerciceId(Long planExerciceId);
    List<CoachPlanAssignment> findByClientId(String clientId);
}
