package com.example.backendhealth.controllers;

import com.example.backendhealth.entities.Bloomer;
import com.example.backendhealth.entities.user;
import com.example.backendhealth.repositories.BloomerRepository;
import com.example.backendhealth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PatientController {

    private final BloomerRepository bloomerRepository;
    private final UserRepository userRepository;

    /**
     * GET /api/patients/{id}
     * Returns patient (Bloomer) profile data by user ID.
     * Tries the Bloomer table first (for age/height/weight/goal),
     * then falls back to the base users table.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable String id) {
        // Try Bloomer-specific data first
        Optional<Bloomer> bloomer = bloomerRepository.findById(id);
        if (bloomer.isPresent()) {
            Bloomer b = bloomer.get();
            Map<String, Object> result = new HashMap<>();
            result.put("id",              b.getId());
            result.put("nom",             b.getNom());
            result.put("prenom",          b.getPrenom());
            result.put("email",           b.getEmail());
            result.put("telephone",       b.getTel());
            result.put("typeAbonnement",  b.getTypeAbonnement());
            result.put("age",             b.getAge());
            result.put("height",          b.getHeight());
            result.put("weight",          b.getWeight());
            result.put("goal",            b.getGoal());
            result.put("lifestyleLevel",  b.getLifestyleLevel());
            return ResponseEntity.ok(result);
        }

        // Fall back to generic user
        Optional<user> u = userRepository.findById(id);
        if (u.isPresent()) {
            user usr = u.get();
            Map<String, Object> result = new HashMap<>();
            result.put("id",             usr.getId());
            result.put("nom",            usr.getNom());
            result.put("prenom",         usr.getPrenom());
            result.put("email",          usr.getEmail());
            result.put("telephone",      usr.getTel());
            result.put("typeAbonnement", usr.getTypeAbonnement());
            return ResponseEntity.ok(result);
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * PUT /api/patients/{id}
     * Updates patient profile data.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatient(
            @PathVariable String id,
            @RequestBody Map<String, Object> body) {

        Optional<Bloomer> opt = bloomerRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Bloomer b = opt.get();

        if (body.containsKey("nom"))            b.setNom((String) body.get("nom"));
        if (body.containsKey("prenom"))         b.setPrenom((String) body.get("prenom"));
        if (body.containsKey("email"))          b.setEmail((String) body.get("email"));
        if (body.containsKey("telephone"))      b.setTel((String) body.get("telephone"));
        if (body.containsKey("goal"))           b.setGoal((String) body.get("goal"));
        if (body.containsKey("lifestyleLevel")) b.setLifestyleLevel((String) body.get("lifestyleLevel"));
        if (body.containsKey("age"))            b.setAge(((Number) body.get("age")).intValue());
        if (body.containsKey("height"))         b.setHeight(((Number) body.get("height")).doubleValue());
        if (body.containsKey("weight"))         b.setWeight(((Number) body.get("weight")).doubleValue());

        bloomerRepository.save(b);

        return ResponseEntity.ok(Map.of("message", "Profil mis à jour avec succès"));
    }
}
