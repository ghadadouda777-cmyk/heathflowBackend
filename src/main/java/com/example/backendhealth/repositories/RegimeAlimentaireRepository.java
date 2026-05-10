package com.example.backendhealth.repositories;

import com.example.backendhealth.entities.RegimeAlimentaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegimeAlimentaireRepository extends JpaRepository<RegimeAlimentaire, Long> {

    List<RegimeAlimentaire> findByUserId(Long userId);

    List<RegimeAlimentaire> findByType(String type);

    List<RegimeAlimentaire> findByNomContainingIgnoreCase(String nom);
}
