package com.example.backendhealth.controllers;

import com.example.backendhealth.dto.MessageDTO;
import com.example.backendhealth.entities.Message.SenderRole;
import com.example.backendhealth.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<MessageDTO> send(@RequestBody MessageDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(messageService.sendMessage(dto));
    }

    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<List<MessageDTO>> getByConversation(@PathVariable Long conversationId) {
        return ResponseEntity.ok(messageService.getMessagesByConversation(conversationId));
    }

    @PatchMapping("/conversation/{conversationId}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable Long conversationId,
            @RequestParam SenderRole readerRole) {
        messageService.markAsRead(conversationId, readerRole);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/conversation/{conversationId}/unread-count")
    public ResponseEntity<Long> unreadCount(@PathVariable Long conversationId) {
        return ResponseEntity.ok(messageService.countUnread(conversationId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}