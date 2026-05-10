package com.example.backendhealth.services;

import com.example.backendhealth.dto.MessageDTO;
import com.example.backendhealth.entities.Conversation;
import com.example.backendhealth.entities.Conversation.ConversationStatus;
import com.example.backendhealth.entities.Message;
import com.example.backendhealth.entities.Message.SenderRole;
import com.example.backendhealth.repositories.ConversationRepository;
import com.example.backendhealth.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;

    public MessageDTO sendMessage(MessageDTO dto) {
        Conversation conv = conversationRepository.findById(dto.getConversationId())
                .orElseThrow(() -> new RuntimeException("Conversation introuvable"));

        if (conv.getStatus() != ConversationStatus.ACTIVE) {
            throw new RuntimeException("Impossible d'envoyer un message dans une conversation fermée");
        }

        Message msg = Message.builder()
                .conversation(conv)
                .senderId(dto.getSenderId())
                .senderRole(dto.getSenderRole())
                .content(dto.getContent())
                .isRead(false)
                .build();

        return toDTO(messageRepository.save(msg));
    }

    @Transactional(readOnly = true)
    public List<MessageDTO> getMessagesByConversation(Long conversationId) {
        return messageRepository.findByConversationIdOrderBySentAtAsc(conversationId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public void markAsRead(Long conversationId, SenderRole readerRole) {
        messageRepository.markAllAsRead(conversationId, readerRole);
    }

    public long countUnread(Long conversationId) {
        return messageRepository.countByConversationIdAndIsReadFalse(conversationId);
    }

    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }

    private MessageDTO toDTO(Message m) {
        return MessageDTO.builder()
                .id(m.getId())
                .conversationId(m.getConversation().getId())
                .senderId(m.getSenderId())
                .senderRole(m.getSenderRole())
                .content(m.getContent())
                .isRead(m.isRead())
                .sentAt(m.getSentAt())
                .build();
    }
}