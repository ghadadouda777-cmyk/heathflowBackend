package com.example.backendhealth.services;

import com.example.backendhealth.dto.ChatConversationDTO;
import com.example.backendhealth.dto.ChatMessageDTO;
import com.example.backendhealth.entities.ChatConversation;
import com.example.backendhealth.entities.ChatMessage;
import com.example.backendhealth.repositories.ChatConversationRepository;
import com.example.backendhealth.repositories.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatConversationService {

    private final ChatConversationRepository convRepo;
    private final ChatMessageRepository msgRepo;

    @Autowired(required = false)
    private NotificationService notificationService;

    /** Find existing conversation or create a new one */
    public ChatConversationDTO getOrCreate(String p1, String p2, String type) {
        ChatConversation conv = convRepo.findByParticipantsAndType(p1, p2, type)
                .orElseGet(() -> convRepo.save(
                        ChatConversation.builder()
                                .participant1Id(p1)
                                .participant2Id(p2)
                                .type(type)
                                .build()
                ));
        return toDTO(conv, p1);
    }

    /** All conversations for a user (any type) */
    @Transactional(readOnly = true)
    public List<ChatConversationDTO> getByParticipant(String userId) {
        return convRepo.findByParticipant(userId).stream()
                .map(c -> toDTO(c, userId))
                .collect(Collectors.toList());
    }

    /** Conversations filtered by type */
    @Transactional(readOnly = true)
    public List<ChatConversationDTO> getByParticipantAndType(String userId, String type) {
        return convRepo.findByParticipantAndType(userId, type).stream()
                .map(c -> toDTO(c, userId))
                .collect(Collectors.toList());
    }

    /** Messages for a conversation ordered by sentAt ASC */
    @Transactional(readOnly = true)
    public List<ChatMessageDTO> getMessages(Long conversationId) {
        return msgRepo.findByConversationIdOrderBySentAtAsc(conversationId).stream()
                .map(this::toMsgDTO)
                .collect(Collectors.toList());
    }

    /** Mark all messages in a conversation as read for a given user */
    public void markAsRead(Long conversationId, String userId) {
        msgRepo.markAsRead(conversationId, userId);
    }

    /** Save a message, update conversation preview, and notify the receiver */
    public ChatMessageDTO saveMessage(ChatMessageDTO dto) {
        ChatMessage msg = ChatMessage.builder()
                .conversationId(dto.getConversationId())
                .senderId(dto.getSenderId())
                .receiverId(dto.getReceiverId())
                .content(dto.getContent())
                .isRead(false)
                .build();
        ChatMessage saved = msgRepo.save(msg);

        // Update conversation lastMessage
        convRepo.findById(dto.getConversationId()).ifPresent(conv -> {
            conv.setLastMessage(dto.getContent());
            conv.setLastMessageAt(LocalDateTime.now());
            convRepo.save(conv);
        });

        ChatMessageDTO result = toMsgDTO(saved);

        // Notify the receiver — works for both HTTP and WebSocket paths
        if (notificationService != null && result.getReceiverId() != null) {
            try {
                String preview = result.getContent().length() > 50
                        ? result.getContent().substring(0, 50) + "…"
                        : result.getContent();
                notificationService.send(
                        result.getReceiverId(),
                        "NEW_MESSAGE",
                        "Nouveau message",
                        preview,
                        String.valueOf(result.getConversationId())
                );
            } catch (Exception e) { /* non-fatal */ }
        }

        return result;
    }

    private ChatConversationDTO toDTO(ChatConversation c, String currentUserId) {
        long unread = msgRepo.countByConversationIdAndReceiverIdAndIsReadFalse(c.getId(), currentUserId);
        return ChatConversationDTO.builder()
                .id(c.getId())
                .participant1Id(c.getParticipant1Id())
                .participant2Id(c.getParticipant2Id())
                .type(c.getType())
                .lastMessage(c.getLastMessage())
                .lastMessageAt(c.getLastMessageAt())
                .unreadCount(unread)
                .build();
    }

    private ChatMessageDTO toMsgDTO(ChatMessage m) {
        return ChatMessageDTO.builder()
                .id(m.getId())
                .conversationId(m.getConversationId())
                .senderId(m.getSenderId())
                .receiverId(m.getReceiverId())
                .content(m.getContent())
                .sentAt(m.getSentAt())
                .isRead(m.isRead())
                .build();
    }
}
