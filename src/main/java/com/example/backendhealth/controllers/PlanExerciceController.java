package com.example.backendhealth.controllers;

import com.example.backendhealth.dto.PlanExerciceDto;
import com.example.backendhealth.services.PlanExerciceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/plans-exercices")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PlanExerciceController {

    private final PlanExerciceService planExerciceService;

    @GetMapping
    public ResponseEntity<List<PlanExerciceDto>> getAll() {
        return ResponseEntity.ok(planExerciceService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanExerciceDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(planExerciceService.getById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PlanExerciceDto>> getByUser(@PathVariable String userId) {
        return ResponseEntity.ok(planExerciceService.getPlansByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<PlanExerciceDto> create(@RequestBody PlanExerciceDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(planExerciceService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlanExerciceDto> update(@PathVariable Long id,
                                                  @RequestBody PlanExerciceDto dto) {
        return ResponseEntity.ok(planExerciceService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        planExerciceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}