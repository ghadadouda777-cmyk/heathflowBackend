package com.example.backendhealth.controllers;

import com.example.backendhealth.dto.PlanAlimentaireDTO;
import com.example.backendhealth.services.PlanAlimentaireService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plans-alimentaires")
@CrossOrigin(origins = "http://localhost:4200")
public class PlanAlimentaireController {

    private final PlanAlimentaireService planAlimentaireService;

    public PlanAlimentaireController(PlanAlimentaireService planAlimentaireService) {
        this.planAlimentaireService = planAlimentaireService;
    }

    @GetMapping
    public ResponseEntity<List<PlanAlimentaireDTO>> getAll() {
        return ResponseEntity.ok(planAlimentaireService.getAllPlans());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanAlimentaireDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(planAlimentaireService.getPlanById(id));
    }

    // ← yraja3 404 ila mafamach plan lel consultation
    @GetMapping("/consultation/{consultationId}")
    public ResponseEntity<PlanAlimentaireDTO> getByConsultation(@PathVariable Long consultationId) {
        return planAlimentaireService.getPlanByConsultationId(consultationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PlanAlimentaireDTO>> getByUser(@PathVariable String userId) {
        return ResponseEntity.ok(planAlimentaireService.getPlansByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<PlanAlimentaireDTO> create(@RequestBody PlanAlimentaireDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(planAlimentaireService.createPlan(dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlanAlimentaireDTO> update(@PathVariable Long id,
                                                     @RequestBody PlanAlimentaireDTO dto) {
        return ResponseEntity.ok(planAlimentaireService.updatePlan(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        planAlimentaireService.deletePlan(id);
        return ResponseEntity.noContent().build();
    }
}