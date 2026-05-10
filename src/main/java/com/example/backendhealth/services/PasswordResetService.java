package com.example.backendhealth.services;

import com.example.backendhealth.entities.PasswordResetToken;
import com.example.backendhealth.entities.user;
import com.example.backendhealth.repositories.PasswordResetTokenRepository;
import com.example.backendhealth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    // ─── Étape 1 : envoyer le code par email ───────────────────────────────────

    @Transactional
    public void sendResetCode(String email) {
        // Vérifier que l'utilisateur existe
        userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Aucun compte trouvé avec cet email"));

        // Supprimer les anciens tokens de cet email
        tokenRepository.deleteByEmail(email);

        // Générer un code à 6 chiffres
        String code = generateCode();

        // Créer et sauvegarder le token (expire dans 10 minutes)
        PasswordResetToken token = new PasswordResetToken();
        token.setEmail(email);
        token.setCode(code);
        token.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        token.setUsed(false);
        tokenRepository.save(token);

        // Envoyer le code par email
        emailService.sendResetCode(email, code);
    }

    // ─── Étape 2 : vérifier le code ────────────────────────────────────────────

    public void verifyCode(String email, String code) {
        PasswordResetToken token = tokenRepository
                .findByEmailAndCodeAndUsedFalse(email, code)
                .orElseThrow(() -> new RuntimeException("Code invalide ou déjà utilisé"));

        if (token.isExpired()) {
            throw new RuntimeException("Code expiré. Veuillez en demander un nouveau.");
        }
    }


    @Transactional
    public void resetPassword(String email, String code, String newPassword) {
        PasswordResetToken token = tokenRepository
                .findByEmailAndCodeAndUsedFalse(email, code)
                .orElseThrow(() -> new RuntimeException("Code invalide ou déjà utilisé"));

        if (token.isExpired()) {
            throw new RuntimeException("Code expiré. Veuillez en demander un nouveau.");
        }

        // Mettre à jour le mot de passe — champ "pwd" dans l'entité user
        user existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        existingUser.setPwd(passwordEncoder.encode(newPassword)); // ← "pwd" pas "password"
        userRepository.save(existingUser);

        token.setUsed(true);
        tokenRepository.save(token);
    }


    private String generateCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}