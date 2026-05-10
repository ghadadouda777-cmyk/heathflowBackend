package com.example.backendhealth.controllers;

import com.example.backendhealth.dto.ChatMessageDTO;
import com.example.backendhealth.services.ChatConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatConversationService chatService;

    /**
     * Client sends to /app/chat.send
     * Server saves to DB, then pushes to /user/{receiverId}/queue/messages
     */
    @MessageMapping("/chat.send")
    public void sendMessage(ChatMessageDTO dto) {
        // Persist message and update conversation
        ChatMessageDTO saved = chatService.saveMessage(dto);

        // Push to receiver's personal queue
        messagingTemplate.convertAndSendToUser(
                saved.getReceiverId(),
                "/queue/messages",
                saved
        );
    }
}
