package com.example.backendhealth.controllers;

import com.example.backendhealth.dto.RegimeAlimentaireDTO;
import com.example.backendhealth.services.RegimeAlimentaireService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/regimes-alimentaires")
@CrossOrigin(origins = "*")
public class RegimeAlimentaireController {

    private final RegimeAlimentaireService regimeService;

    public RegimeAlimentaireController(RegimeAlimentaireService regimeService) {
        this.regimeService = regimeService;
    }


    @GetMapping
    public ResponseEntity<List<RegimeAlimentaireDTO>> getAllRegimes() {
        return ResponseEntity.ok(regimeService.getAllRegimes());
    }


    @GetMapping("/{id}")
    public ResponseEntity<RegimeAlimentaireDTO> getRegimeById(@PathVariable Long id) {
        return ResponseEntity.ok(regimeService.getRegimeById(id));
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RegimeAlimentaireDTO>> getRegimesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(regimeService.getRegimesByUserId(userId));
    }


    @GetMapping("/type/{type}")
    public ResponseEntity<List<RegimeAlimentaireDTO>> getRegimesByType(@PathVariable String type) {
        return ResponseEntity.ok(regimeService.getRegimesByType(type));
    }

    @GetMapping("/search")
    public ResponseEntity<List<RegimeAlimentaireDTO>> searchByNom(@RequestParam String nom) {
        return ResponseEntity.ok(regimeService.searchByNom(nom));
    }


    @PostMapping
    public ResponseEntity<RegimeAlimentaireDTO> createRegime(@RequestBody RegimeAlimentaireDTO dto) {
        RegimeAlimentaireDTO created = regimeService.createRegime(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegimeAlimentaireDTO> updateRegime(
            @PathVariable Long id,
            @RequestBody RegimeAlimentaireDTO dto) {
        return ResponseEntity.ok(regimeService.updateRegime(id, dto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegime(@PathVariable Long id) {
        regimeService.deleteRegime(id);
        return ResponseEntity.noContent().build();
    }
}
