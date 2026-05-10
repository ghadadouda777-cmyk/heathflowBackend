package com.example.backendhealth.controllers;

import com.example.backendhealth.entities.Coach;
import com.example.backendhealth.entities.user;
import com.example.backendhealth.repositories.CoachRepository;
import com.example.backendhealth.repositories.UserRepository;
import com.example.backendhealth.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/coaches")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CoachController {

    private final CoachRepository coachRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * Resolves a Coach using multiple strategies:
     * 1. coachRepository.findById(id)  — works if JPA JOINED inheritance is correct
     * 2. userRepository.findById(id) → coachRepository.findByEmail — fallback for UUID mismatch
     * 3. JWT token email → coachRepository.findByEmail — ultimate fallback
     */
    private Optional<Coach> resolveCoach(String id, HttpServletRequest request) {
        // Strategy 1: direct by ID
        Optional<Coach> byId = coachRepository.findById(id);
        if (byId.isPresent()) return byId;

        // Strategy 2: user by ID → coach by email
        Optional<user> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            Optional<Coach> byEmail = coachRepository.findByEmail(userOpt.get().getEmail());
            if (byEmail.isPresent()) return byEmail;
        }

        // Strategy 3: extract email from JWT token
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = authHeader.substring(7);
                String email = jwtUtil.extractEmail(token);
                if (email != null) {
                    return coachRepository.findByEmail(email);
                }
            } catch (Exception ignored) {}
        }

        return Optional.empty();
    }

    /**
     * GET /api/coaches/by-email?email=...
     * Most reliable endpoint — finds coach directly by email
     */
    @GetMapping("/by-email")
    public ResponseEntity<?> getCoachByEmail(@RequestParam String email) {
        Optional<Coach> opt = coachRepository.findByEmail(email);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        Coach c = opt.get();
        Map<String, Object> result = new HashMap<>();
        result.put("id",              c.getId());
        result.put("nom",             c.getNom());
        result.put("prenom",          c.getPrenom());
        result.put("email",           c.getEmail());
        result.put("telephone",       c.getTel());
        result.put("role",            c.getRole());
        result.put("typeAbonnement",  c.getTypeAbonnement());
        result.put("coachSpecialite", c.getCoachSpecialite());
        result.put("certifications",  c.getCertifications());
        result.put("active",          c.isActive());
        return ResponseEntity.ok(result);
    }

    /**
     * PUT /api/coaches/by-email?email=...
     */
    @PutMapping("/by-email")
    public ResponseEntity<?> updateCoachByEmail(
            @RequestParam String email,
            @RequestBody Map<String, Object> body) {

        Optional<Coach> opt = coachRepository.findByEmail(email);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        Coach c = opt.get();
        if (body.containsKey("nom"))             c.setNom((String) body.get("nom"));
        if (body.containsKey("prenom"))          c.setPrenom((String) body.get("prenom"));
        if (body.containsKey("telephone"))       c.setTel((String) body.get("telephone"));
        if (body.containsKey("coachSpecialite")) c.setCoachSpecialite((String) body.get("coachSpecialite"));
        if (body.containsKey("certifications"))  c.setCertifications((String) body.get("certifications"));

        coachRepository.save(c);
        return ResponseEntity.ok(Map.of("message", "Profile updated successfully"));
    }

    /**
     * POST /api/coaches/by-email/change-password?email=...
     */
    @PostMapping("/by-email/change-password")
    public ResponseEntity<?> changePasswordByEmail(
            @RequestParam String email,
            @RequestBody Map<String, String> body) {

        Optional<Coach> opt = coachRepository.findByEmail(email);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        String newPassword = body.get("newPassword");
        if (newPassword == null || newPassword.length() < 8) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Password must be at least 8 characters"));
        }

        Coach c = opt.get();
        c.setPwd(passwordEncoder.encode(newPassword));
        coachRepository.save(c);
        return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
    }

    /**
     * GET /api/coaches/{id}
     * Also accepts ?email=... as fallback
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getCoachById(
            @PathVariable String id,
            @RequestParam(required = false) String email,
            HttpServletRequest request) {

        // Try all strategies to find the coach
        Coach c = findCoach(id, email, request);
        if (c == null) return ResponseEntity.notFound().build();

        Map<String, Object> result = new HashMap<>();
        result.put("id",              c.getId());
        result.put("nom",             c.getNom());
        result.put("prenom",          c.getPrenom());
        result.put("email",           c.getEmail());
        result.put("telephone",       c.getTel());
        result.put("role",            c.getRole());
        result.put("typeAbonnement",  c.getTypeAbonnement());
        result.put("coachSpecialite", c.getCoachSpecialite());
        result.put("certifications",  c.getCertifications());
        result.put("active",          c.isActive());
        return ResponseEntity.ok(result);
    }

    /**
     * PUT /api/coaches/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCoach(
            @PathVariable String id,
            @RequestParam(required = false) String email,
            @RequestBody Map<String, Object> body,
            HttpServletRequest request) {

        Coach c = findCoach(id, email, request);
        if (c == null) return ResponseEntity.notFound().build();

        if (body.containsKey("nom"))             c.setNom((String) body.get("nom"));
        if (body.containsKey("prenom"))          c.setPrenom((String) body.get("prenom"));
        if (body.containsKey("telephone"))       c.setTel((String) body.get("telephone"));
        if (body.containsKey("coachSpecialite")) c.setCoachSpecialite((String) body.get("coachSpecialite"));
        if (body.containsKey("certifications"))  c.setCertifications((String) body.get("certifications"));

        coachRepository.save(c);
        return ResponseEntity.ok(Map.of("message", "Profile updated successfully"));
    }

    /**
     * POST /api/coaches/{id}/change-password
     */
    @PostMapping("/{id}/change-password")
    public ResponseEntity<?> changePassword(
            @PathVariable String id,
            @RequestParam(required = false) String email,
            @RequestBody Map<String, String> body,
            HttpServletRequest request) {

        Coach c = findCoach(id, email, request);
        if (c == null) return ResponseEntity.notFound().build();

        String newPassword = body.get("newPassword");
        if (newPassword == null || newPassword.length() < 8) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Password must be at least 8 characters"));
        }

        c.setPwd(passwordEncoder.encode(newPassword));
        coachRepository.save(c);
        return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
    }

    /**
     * Finds a coach using multiple strategies:
     * 1. Direct by ID (JPA JOINED)
     * 2. User by ID → coach by email
     * 3. Email query param → coach by email
     * 4. JWT token email → coach by email
     */
    private Coach findCoach(String id, String emailParam, HttpServletRequest request) {
        // 1. Direct by ID
        Optional<Coach> byId = coachRepository.findById(id);
        if (byId.isPresent()) return byId.get();

        // 2. User by ID → coach by email
        Optional<user> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            Optional<Coach> byEmail = coachRepository.findByEmail(userOpt.get().getEmail());
            if (byEmail.isPresent()) return byEmail.get();
        }

        // 3. Email query param
        if (emailParam != null && !emailParam.isBlank()) {
            Optional<Coach> byEmailParam = coachRepository.findByEmail(emailParam);
            if (byEmailParam.isPresent()) return byEmailParam.get();
        }

        // 4. JWT token email (best effort)
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = authHeader.substring(7);
                String emailFromToken = jwtUtil.extractEmail(token);
                if (emailFromToken != null) {
                    Optional<Coach> byTokenEmail = coachRepository.findByEmail(emailFromToken);
                    if (byTokenEmail.isPresent()) return byTokenEmail.get();
                }
            } catch (Exception ignored) {}
        }

        return null;
    }
}
