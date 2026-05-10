package com.example.backendhealth.services;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendResetCode(String toEmail, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Réinitialisation de votre mot de passe");
            message.setText(
                    "Bonjour,\n\n" +
                            "Votre code de réinitialisation est : " + code + "\n\n" +
                            "Ce code expire dans 10 minutes.\n\n" +
                            "Si vous n'avez pas demandé cette réinitialisation, ignorez cet email.\n\n" +
                            "Cordialement,\nL'équipe HealthFlow"
            );
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println("⚠️ Email reset non envoyé : " + e.getMessage());
        }
    }

    public void sendSubscriptionConfirmation(String toEmail, String typeAbonnement) {
        try {
            String libelle = switch (typeAbonnement) {
                case "MOIS_1" -> "1 Mois - 300 DT";
                case "MOIS_3" -> "3 Mois - 750 DT";
                case "MOIS_6" -> "6 Mois - 1320 DT";
                case "AN_1"   -> "1 An - 2160 DT";
                default -> typeAbonnement;
            };

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Abonnement HealthFlow confirmé !");
            message.setText(
                    "Bonjour,\n\n" +
                            "Votre abonnement " + libelle + " est maintenant actif.\n\n" +
                            "Vous avez accès à toutes les fonctionnalités de HealthFlow.\n\n" +
                            "Cordialement,\nL'équipe HealthFlow"
            );
            mailSender.send(message);
        } catch (Exception e) {
            // ✅ Ne bloque pas le paiement si email échoue
            System.out.println("⚠️ Email confirmation non envoyé : " + e.getMessage());
        }
    }
}