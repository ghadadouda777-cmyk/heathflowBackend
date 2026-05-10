package com.example.backendhealth.controllers;

import com.example.backendhealth.dto.ExerciceDto;
import com.example.backendhealth.services.ExerciceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/exercices")
@RequiredArgsConstructor
public class ExerciceController {

    private final ExerciceService exerciceService;

    @GetMapping
    public ResponseEntity<List<ExerciceDto>> getAll(@RequestParam(required = false) String coachId) {
        if (coachId != null && !coachId.isBlank()) {
            return ResponseEntity.ok(exerciceService.getByCoachId(coachId));
        }
        return ResponseEntity.ok(exerciceService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExerciceDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(exerciceService.getById(id));
    }

    @GetMapping("/plan/{planId}")
    public ResponseEntity<List<ExerciceDto>> getByPlan(@PathVariable Long planId) {
        return ResponseEntity.ok(exerciceService.getByPlanId(planId));
    }

    @PostMapping
    public ResponseEntity<ExerciceDto> create(@RequestBody ExerciceDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(exerciceService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExerciceDto> update(@PathVariable Long id, @RequestBody ExerciceDto dto) {
        return ResponseEntity.ok(exerciceService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        exerciceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}