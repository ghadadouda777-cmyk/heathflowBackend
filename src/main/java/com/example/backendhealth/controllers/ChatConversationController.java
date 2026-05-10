package com.example.backendhealth.controllers;

import com.example.backendhealth.dto.ChatConversationDTO;
import com.example.backendhealth.dto.ChatMessageDTO;
import com.example.backendhealth.services.ChatConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ChatConversationController {

    private final ChatConversationService chatService;

    /** GET /api/chat/nutritionist/{userId} — conversations of type PATIENT_NUTRITIONIST */
    @GetMapping("/nutritionist/{userId}")
    public ResponseEntity<List<ChatConversationDTO>> getByNutritionist(@PathVariable String userId) {
        return ResponseEntity.ok(chatService.getByParticipantAndType(userId, "PATIENT_NUTRITIONIST"));
    }

    /** GET /api/chat/coach/{userId} — conversations of type CLIENT_COACH */
    @GetMapping("/coach/{userId}")
    public ResponseEntity<List<ChatConversationDTO>> getByCoach(@PathVariable String userId) {
        return ResponseEntity.ok(chatService.getByParticipantAndType(userId, "CLIENT_COACH"));
    }

    /** GET /api/chat/patient/{userId} — all conversations where userId is participant */
    @GetMapping("/patient/{userId}")
    public ResponseEntity<List<ChatConversationDTO>> getByPatient(@PathVariable String userId) {
        return ResponseEntity.ok(chatService.getByParticipant(userId));
    }

    /** GET /api/chat/{conversationId}/messages — message history */
    @GetMapping("/{conversationId}/messages")
    public ResponseEntity<List<ChatMessageDTO>> getMessages(@PathVariable Long conversationId) {
        return ResponseEntity.ok(chatService.getMessages(conversationId));
    }

    /** POST /api/chat — create or get conversation */
    @PostMapping
    public ResponseEntity<ChatConversationDTO> createOrGet(@RequestBody Map<String, String> body) {
        String p1   = body.get("participant1Id");
        String p2   = body.get("participant2Id");
        String type = body.get("type");
        if (p1 == null || p2 == null || type == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(chatService.getOrCreate(p1, p2, type));
    }

    /** PATCH /api/chat/{conversationId}/read/{userId} — mark messages as read */
    @PatchMapping("/{conversationId}/read/{userId}")
    public ResponseEntity<Void> markAsRead(
            @PathVariable Long conversationId,
            @PathVariable String userId) {
        chatService.markAsRead(conversationId, userId);
        return ResponseEntity.noContent().build();
    }

    /** POST /api/chat/message — save a message (HTTP fallback when WebSocket unavailable) */
    @PostMapping("/message")
    public ResponseEntity<ChatMessageDTO> saveMessage(@RequestBody ChatMessageDTO dto) {
        return ResponseEntity.ok(chatService.saveMessage(dto));
    }
}
