package com.example.backendhealth.repositories;

import com.example.backendhealth.entities.CoachMessage;
import com.example.backendhealth.entities.CoachMessage.SenderRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CoachMessageRepository extends JpaRepository<CoachMessage, Long> {
    List<CoachMessage> findByConversationIdOrderBySentAtAsc(Long conversationId);
    long countByConversationIdAndIsReadFalse(Long conversationId);
    long countByConversationCoachIdAndIsReadFalseAndSenderRole(String coachId, SenderRole senderRole);

    @Modifying
    @Query("UPDATE CoachMessage m SET m.isRead = true WHERE m.conversation.id = :convId AND m.senderRole != :readerRole")
    void markAllAsRead(@Param("convId") Long conversationId, @Param("readerRole") SenderRole readerRole);
}
