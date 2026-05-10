package com.example.backendhealth.repositories;

import com.example.backendhealth.entities.Abonnement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AbonnementRepository extends JpaRepository<Abonnement, String> {

    Optional<Abonnement> findByStripeSessionId(String sessionId);

    @Query("SELECT a FROM Abonnement a WHERE a.userEntity.id = :userId " +
            "AND a.statut = 'ACTIF' AND a.dateFin > :today ORDER BY a.dateFin DESC")
    Optional<Abonnement> findAbonnementActifByuserId(@Param("userId") String userId,
                                                     @Param("today") LocalDate today);

    List<Abonnement> findAllByuserEntityId(String userId);

    @Query("SELECT a FROM Abonnement a WHERE a.dateFin < :today AND a.statut = 'ACTIF'")
    List<Abonnement> findAbonnementsExpires(@Param("today") LocalDate today);
}
