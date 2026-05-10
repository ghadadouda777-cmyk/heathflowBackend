
package com.example.backendhealth.controllers;
import com.example.backendhealth.dto.ConsultationDTO;
import com.example.backendhealth.services.ConsultationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultations")
@CrossOrigin(origins = "*")
public class ConsultationController {

    private final ConsultationService consultationService;

    public ConsultationController(ConsultationService consultationService) {
        this.consultationService = consultationService;
    }

    @GetMapping
    public ResponseEntity<List<ConsultationDTO>> getAll() {
        return ResponseEntity.ok(consultationService.getAllConsultations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultationDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(consultationService.getConsultationById(id));
    }

    @GetMapping("/rendez-vous/{rendezVousId}")
    public ResponseEntity<ConsultationDTO> getByRendezVous(@PathVariable Long rendezVousId) {
        return ResponseEntity.ok(consultationService.getByRendezVousId(rendezVousId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ConsultationDTO>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(consultationService.getByUserId(userId));
    }

    @GetMapping("/nutritionniste/{id}")
    public ResponseEntity<List<ConsultationDTO>> getByNutritionniste(@PathVariable Long id) {
        return ResponseEntity.ok(consultationService.getByNutritionnisteId(id));
    }

    @GetMapping("/coach/{id}")
    public ResponseEntity<List<ConsultationDTO>> getByCoach(@PathVariable Long id) {
        return ResponseEntity.ok(consultationService.getByCoachId(id));
    }

    @PostMapping
    public ResponseEntity<ConsultationDTO> create(@RequestBody ConsultationDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(consultationService.createConsultation(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsultationDTO> update(
            @PathVariable Long id,
            @RequestBody ConsultationDTO dto) {
        return ResponseEntity.ok(consultationService.updateConsultation(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        consultationService.deleteConsultation(id);
        return ResponseEntity.noContent().build();
    }
}
