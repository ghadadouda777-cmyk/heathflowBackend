package com.example.backendhealth.repositories;

import com.example.backendhealth.entities.Message;
import com.example.backendhealth.entities.Message.SenderRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByConversationIdOrderBySentAtAsc(Long conversationId);

    long countByConversationIdAndIsReadFalse(Long conversationId);

    @Modifying
    @Query("UPDATE Message m SET m.isRead = true WHERE m.conversation.id = :convId AND m.senderRole != :role")
    void markAllAsRead(@Param("convId") Long conversationId, @Param("role") SenderRole readerRole);
}