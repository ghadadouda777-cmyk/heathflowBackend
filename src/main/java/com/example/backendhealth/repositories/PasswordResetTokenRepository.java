package com.example.backendhealth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.backendhealth.entities.PasswordResetToken;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByEmailAndCodeAndUsedFalse(String email, String code);

    void deleteByEmail(String email);
}