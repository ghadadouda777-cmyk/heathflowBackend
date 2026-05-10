package com.example.backendhealth.repositories;

import com.example.backendhealth.entities.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByConversationIdOrderBySentAtAsc(Long conversationId);

    long countByConversationIdAndReceiverIdAndIsReadFalse(Long conversationId, String receiverId);

    @Modifying
    @Query("UPDATE ChatMessage m SET m.isRead = true WHERE m.conversationId = :convId AND m.receiverId = :userId")
    void markAsRead(@Param("convId") Long conversationId, @Param("userId") String userId);
}
