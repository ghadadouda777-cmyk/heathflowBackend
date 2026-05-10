package com.example.backendhealth.controllers;

import com.example.backendhealth.dto.RendezVousDTO;
import com.example.backendhealth.entities.Coach;
import com.example.backendhealth.entities.Nutritionist;
import com.example.backendhealth.entities.RendezVous.StatutRendezVous;
import com.example.backendhealth.services.RendezVousService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/rendez-vous")
@CrossOrigin(origins = "*")
public class RendezVousController {

    private final RendezVousService rdvService;

    public RendezVousController(RendezVousService rdvService) {
        this.rdvService = rdvService;
    }

    // ── GET ALL NUTRITIONNISTES ────────────────────────────────────
    @GetMapping("/nutritionnistes")
    public ResponseEntity<List<Nutritionist>> getAllNutritionnistes() {
        return ResponseEntity.ok(rdvService.rechercherNutritionnisteParNom(""));
    }

    // ── GET ALL COACHS ─────────────────────────────────────────────
    @GetMapping("/coachs")
    public ResponseEntity<List<Coach>> getAllCoachs() {
        return ResponseEntity.ok(rdvService.getAllCoaches());
    }

    // ── Patient cherche nutritionniste par nom ─────────────────────
    @GetMapping("/nutritionnistes/search")
    public ResponseEntity<List<Nutritionist>> searchNutritionniste(@RequestParam String nom) {
        return ResponseEntity.ok(rdvService.rechercherNutritionnisteParNom(nom));
    }

    // ── Patient cherche coach par nom ──────────────────────────────
    @GetMapping("/coachs/search")
    public ResponseEntity<List<Coach>> searchCoach(@RequestParam String nom) {
        return ResponseEntity.ok(rdvService.rechercherCoachParNom(nom));
    }

    @GetMapping
    public ResponseEntity<List<RendezVousDTO>> getAll() {
        return ResponseEntity.ok(rdvService.getAllRendezVous());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RendezVousDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(rdvService.getRendezVousById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RendezVousDTO>> getByUser(@PathVariable String userId) {
        return ResponseEntity.ok(rdvService.getRendezVousByUserId(userId));
    }

    @GetMapping("/nutritionniste/{nutritionnisteId}")
    public ResponseEntity<List<RendezVousDTO>> getByNutritionniste(@PathVariable String nutritionnisteId) {
        return ResponseEntity.ok(rdvService.getRendezVousByNutritionnisteId(nutritionnisteId));
    }

    @GetMapping("/coach/{coachId}")
    public ResponseEntity<List<RendezVousDTO>> getByCoach(@PathVariable String coachId) {
        return ResponseEntity.ok(rdvService.getRendezVousByCoachId(coachId));
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<RendezVousDTO>> getByStatut(@PathVariable StatutRendezVous statut) {
        return ResponseEntity.ok(rdvService.getRendezVousByStatut(statut));
    }

    @GetMapping("/user/{userId}/statut/{statut}")
    public ResponseEntity<List<RendezVousDTO>> getByUserAndStatut(
            @PathVariable String userId, @PathVariable StatutRendezVous statut) {
        return ResponseEntity.ok(rdvService.getRendezVousByUserIdAndStatut(userId, statut));
    }

    @GetMapping("/calendrier/nutritionniste/{nutritionnisteId}")
    public ResponseEntity<List<RendezVousDTO>> getCalendrierNutritionniste(
            @PathVariable String nutritionnisteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(rdvService.getCalendrierNutritionniste(nutritionnisteId, debut, fin));
    }

    @GetMapping("/calendrier/coach/{coachId}")
    public ResponseEntity<List<RendezVousDTO>> getCalendrierCoach(
            @PathVariable String coachId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(rdvService.getCalendrierCoach(coachId, debut, fin));
    }

    @PostMapping
    public ResponseEntity<RendezVousDTO> create(@RequestBody RendezVousDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rdvService.createRendezVous(dto));
    }

    @PatchMapping("/{id}/accepter")
    public ResponseEntity<RendezVousDTO> accepter(@PathVariable Long id) {
        return ResponseEntity.ok(rdvService.accepterRendezVous(id));
    }

    @PatchMapping("/{id}/refuser")
    public ResponseEntity<RendezVousDTO> refuser(@PathVariable Long id) {
        return ResponseEntity.ok(rdvService.refuserRendezVous(id));
    }

    @PatchMapping("/{id}/terminer")
    public ResponseEntity<RendezVousDTO> terminer(@PathVariable Long id) {
        return ResponseEntity.ok(rdvService.terminerRendezVous(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RendezVousDTO> update(@PathVariable Long id, @RequestBody RendezVousDTO dto) {
        return ResponseEntity.ok(rdvService.updateRendezVous(id, dto));
    }

    @PatchMapping("/{id}/statut/{statut}")
    public ResponseEntity<RendezVousDTO> updateStatut(
            @PathVariable Long id, @PathVariable StatutRendezVous statut) {
        return ResponseEntity.ok(rdvService.updateStatut(id, statut));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rdvService.deleteRendezVous(id);
        return ResponseEntity.noContent().build();
    }
}