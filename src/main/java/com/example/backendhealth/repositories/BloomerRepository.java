// BloomerRepository
package com.example.backendhealth.repositories;

import com.example.backendhealth.entities.Bloomer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BloomerRepository extends JpaRepository<Bloomer, String> {
    Optional<Bloomer> findByEmail(String email);

}