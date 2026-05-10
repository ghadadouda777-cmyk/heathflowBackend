package com.example.backendhealth.repositories;

import com.example.backendhealth.entities.PlanAlimentaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlanAlimentaireRepository extends JpaRepository<PlanAlimentaire, Long> {

    List<PlanAlimentaire> findByUserId(String userId);

    List<PlanAlimentaire> findByRegimeId(Long regimeId);

    // ← @Query obligatoire car "consultation" est une relation @OneToOne, pas un champ simple
    @Query("SELECT p FROM PlanAlimentaire p WHERE p.consultation.id = :consultationId")
    Optional<PlanAlimentaire> findByConsultationId(@Param("consultationId") Long consultationId);
}