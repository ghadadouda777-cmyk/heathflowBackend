package com.example.backendhealth.controllers;

import com.example.backendhealth.dto.AbonnementDTO;
import com.example.backendhealth.services.AbonnementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/abonnements")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AbonnementController {

    private final AbonnementService abonnementService;

    @PostMapping("/payer")
    public ResponseEntity<?> payer(
            @RequestBody AbonnementDTO.PaymentRequest request,
            Authentication authentication) {
        try {
            AbonnementDTO.PaymentResponse response =
                    abonnementService.traiterPaiement(request, authentication.getName());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
    @GetMapping("/statut")
    public ResponseEntity<AbonnementDTO.StatutAbonnementResponse> statut(
            Authentication authentication) {
        return ResponseEntity.ok(
                abonnementService.verifierStatut(authentication.getName())
        );
    }

    @GetMapping("/historique")
    public ResponseEntity<List<AbonnementDTO.AbonnementResponse>> historique(
            Authentication authentication) {
        return ResponseEntity.ok(
                abonnementService.getHistorique(authentication.getName())
        );
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> annuler(
            @PathVariable String id,
            Authentication authentication) {
        try {
            abonnementService.annuler(id, authentication.getName());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}