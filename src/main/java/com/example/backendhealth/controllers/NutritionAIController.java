package com.example.backendhealth.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api/nutrition")
@CrossOrigin(origins = "http://localhost:4200")
public class NutritionAIController {

    @Value("${groq.api.key}")
    private String apiKey;

    @PostMapping("/analyze")
    public ResponseEntity<?> analyze(@RequestBody Map<String, Object> body) {
        String nom = (String) body.get("nom");
        Integer quantite = (Integer) body.get("quantite");

        String prompt = "Pour " + quantite + "g de \"" + nom + "\", " +
                "donne-moi UNIQUEMENT un JSON avec les calories et protéines totales. " +
                "Format exact : {\"calories\": 0, \"proteines\": 0} " +
                "Pas d'explication, juste le JSON.";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        Map<String, Object> requestBody = Map.of(
                "model", "llama-3.3-70b-versatile",
                "max_tokens", 200,
                "messages", new Object[]{
                        Map.of("role", "user", "content", prompt)
                }
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://api.groq.com/openai/v1/chat/completions",
                request,
                Map.class
        );

        // Extraire le texte de la réponse Groq
        var choices = (java.util.List<?>) response.getBody().get("choices");
        var firstChoice = (Map<?, ?>) choices.get(0);
        var message = (Map<?, ?>) firstChoice.get("message");
        String text = (String) message.get("content");

        return ResponseEntity.ok(Map.of("result", text));
    }
}