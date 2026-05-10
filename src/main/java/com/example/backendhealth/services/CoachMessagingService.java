package com.example.backendhealth.services;

import com.example.backendhealth.dto.CoachConversationDTO;
import com.example.backendhealth.dto.CoachMessageDTO;
import com.example.backendhealth.entities.CoachConversation;
import com.example.backendhealth.entities.CoachConversation.ConversationStatus;
import com.example.backendhealth.entities.CoachMessage;
import com.example.backendhealth.entities.user;
import com.example.backendhealth.repositories.CoachConversationRepository;
import com.example.backendhealth.repositories.CoachMessageRepository;
import com.example.backendhealth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CoachMessagingService {

    private final CoachConversationRepository conversationRepository;
    private final CoachMessageRepository messageRepository;
    private final UserRepository userRepository;

    public CoachConversationDTO createConversation(CoachConversationDTO dto) {
        conversationRepository.findByCoachIdAndClientIdAndStatus(dto.getCoachId(), dto.getClientId(), ConversationStatus.ACTIVE)
                .ifPresent(c -> { throw new RuntimeException("Conversation déjà active pour ce coach/client"); });

        CoachConversation conversation = conversationRepository.save(
                CoachConversation.builder()
                        .coachId(dto.getCoachId())
                        .clientId(dto.getClientId())
                        .status(ConversationStatus.ACTIVE)
                        .build()
        );
        return toConversationDTO(conversation, false);
    }

    @Transactional(readOnly = true)
    public List<CoachConversationDTO> getConversationsByCoach(String coachId) {
        return conversationRepository.findByCoachId(coachId).stream()
                .map(c -> toConversationDTO(c, false))
                .sorted(Comparator.comparing(CoachConversationDTO::getLastMessageAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CoachMessageDTO> getMessages(Long conversationId) {
        return messageRepository.findByConversationIdOrderBySentAtAsc(conversationId)
                .stream()
                .map(this::toMessageDTO)
                .collect(Collectors.toList());
    }

    public CoachMessageDTO sendMessage(CoachMessageDTO dto) {
        CoachConversation conversation = conversationRepository.findById(dto.getConversationId())
                .orElseThrow(() -> new RuntimeException("Conversation introuvable"));
        if (conversation.getStatus() != ConversationStatus.ACTIVE) {
            throw new RuntimeException("Conversation fermée");
        }

        CoachMessage saved = messageRepository.save(
                CoachMessage.builder()
                        .conversation(conversation)
                        .senderId(dto.getSenderId())
                        .senderRole(dto.getSenderRole())
                        .content(dto.getContent())
                        .isRead(false)
                        .build()
        );
        return toMessageDTO(saved);
    }

    public void markConversationAsRead(Long conversationId, CoachMessage.SenderRole readerRole) {
        messageRepository.markAllAsRead(conversationId, readerRole);
    }

    public long countUnread(Long conversationId) {
        return messageRepository.countByConversationIdAndIsReadFalse(conversationId);
    }

    public CoachConversationDTO closeConversation(Long conversationId) {
        CoachConversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation introuvable"));
        conversation.setStatus(ConversationStatus.CLOSED);
        return toConversationDTO(conversationRepository.save(conversation), false);
    }

    private CoachConversationDTO toConversationDTO(CoachConversation conversation, boolean includeMessages) {
        List<CoachMessageDTO> messages = includeMessages
                ? conversation.getMessages().stream().map(this::toMessageDTO).collect(Collectors.toList())
                : List.of();

        CoachMessage last = conversation.getMessages().stream()
                .max(Comparator.comparing(CoachMessage::getSentAt))
                .orElse(null);

        user client = userRepository.findById(conversation.getClientId()).orElse(null);
        String clientName = client == null ? "Unknown Client" : client.getPrenom() + " " + client.getNom();

        return CoachConversationDTO.builder()
                .id(conversation.getId())
                .coachId(conversation.getCoachId())
                .clientId(conversation.getClientId())
                .status(conversation.getStatus())
                .createdAt(conversation.getCreatedAt())
                .updatedAt(conversation.getUpdatedAt())
                .messages(messages)
                .clientName(clientName)
                .lastMessagePreview(last == null ? "" : last.getContent())
                .lastMessageAt(last == null ? null : last.getSentAt())
                .unreadCount(messageRepository.countByConversationIdAndIsReadFalse(conversation.getId()))
                .build();
    }

    private CoachMessageDTO toMessageDTO(CoachMessage message) {
        return CoachMessageDTO.builder()
                .id(message.getId())
                .conversationId(message.getConversation().getId())
                .senderId(message.getSenderId())
                .senderRole(message.getSenderRole())
                .content(message.getContent())
                .isRead(message.isRead())
                .sentAt(message.getSentAt())
                .build();
    }
}
