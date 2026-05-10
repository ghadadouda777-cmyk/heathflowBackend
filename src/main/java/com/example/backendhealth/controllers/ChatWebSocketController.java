package com.example.backendhealth.controllers;

import com.example.backendhealth.dto.ChatMessageDTO;
import com.example.backendhealth.services.ChatConversationService;
import com.example.backendhealth.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatConversationService chatService;

    @Autowired(required = false)
    private NotificationService notificationService;

    /**
     * Client sends to /app/chat.send
     * Server saves to DB, pushes to receiver's message queue,
     * and sends a notification to the receiver.
     */
    @MessageMapping("/chat.send")
    public void sendMessage(ChatMessageDTO dto) {
        ChatMessageDTO saved = chatService.saveMessage(dto);

        // Push message to receiver
        messagingTemplate.convertAndSendToUser(
                saved.getReceiverId(),
                "/queue/messages",
                saved
        );

        // Notify receiver
        if (notificationService != null) {
            try {
                notificationService.send(
                    saved.getReceiverId(),
                    "NEW_MESSAGE",
                    "Nouveau message",
                    "Vous avez reçu un nouveau message.",
                    String.valueOf(saved.getConversationId())
                );
            } catch (Exception e) { /* non-fatal */ }
        }
    }
}
