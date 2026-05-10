package com.example.backendhealth.controllers;

import com.example.backendhealth.dto.ConversationDTO;
import com.example.backendhealth.services.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;

    @PostMapping
    public ResponseEntity<ConversationDTO> create(@RequestBody ConversationDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(conversationService.createConversation(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConversationDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(conversationService.getConversationById(id));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<ConversationDTO>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(conversationService.getConversationsByPatient(patientId));
    }

    @GetMapping("/nutritionist/{nutritionistId}")
    public ResponseEntity<List<ConversationDTO>> getByNutritionist(@PathVariable Long nutritionistId) {
        return ResponseEntity.ok(conversationService.getConversationsByNutritionist(nutritionistId));
    }

    @GetMapping("/coach/{coachId}")
    public ResponseEntity<List<ConversationDTO>> getByCoach(@PathVariable Long coachId) {
        return ResponseEntity.ok(conversationService.getConversationsByCoach(coachId));
    }

    @PatchMapping("/{id}/close")
    public ResponseEntity<ConversationDTO> close(@PathVariable Long id) {
        return ResponseEntity.ok(conversationService.closeConversation(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        conversationService.deleteConversation(id);
        return ResponseEntity.noContent().build();
    }
}