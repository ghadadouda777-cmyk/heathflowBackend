// UserRepository
package com.example.backendhealth.repositories;

import com.example.backendhealth.entities.user;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository extends JpaRepository<user, String> {
    boolean existsByEmail(String email);
    Optional<user> findByEmail(String email);

    Collection<? extends user> findByRole(String bloomer);
}