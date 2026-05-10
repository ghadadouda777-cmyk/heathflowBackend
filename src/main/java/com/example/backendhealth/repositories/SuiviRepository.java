package com.example.backendhealth.repositories;

import com.example.backendhealth.entities.Suivi_quotidien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SuiviRepository extends JpaRepository<Suivi_quotidien,Integer> {
    Optional<Suivi_quotidien> findByUserIdAndDate(String userId, LocalDate date);
    List<Suivi_quotidien> findByUserIdAndDateBetween(String userId, LocalDate start, LocalDate end);
}
