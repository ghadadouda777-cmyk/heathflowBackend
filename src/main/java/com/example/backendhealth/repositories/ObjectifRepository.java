package com.example.backendhealth.repositories;

import com.example.backendhealth.entities.ObjectifPersonnel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ObjectifRepository extends JpaRepository<ObjectifPersonnel, Integer> {
    Optional<ObjectifPersonnel> findByUserId(String userId);
}
