package com.example.backendhealth.repositories;

import com.example.backendhealth.entities.Repas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepasRepository extends JpaRepository<Repas, Long> {

    @Query("SELECT r FROM Repas r WHERE r.planAlimentaire.id = :planAlimentaireId")
    List<Repas> findByPlanAlimentaireId(@Param("planAlimentaireId") Long planAlimentaireId);
}