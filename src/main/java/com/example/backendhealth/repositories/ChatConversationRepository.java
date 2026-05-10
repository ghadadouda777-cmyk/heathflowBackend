package com.example.backendhealth.repositories;

import com.example.backendhealth.entities.ChatConversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatConversationRepository extends JpaRepository<ChatConversation, Long> {

    @Query("SELECT c FROM ChatConversation c WHERE " +
           "(c.participant1Id = :p1 AND c.participant2Id = :p2 AND c.type = :type) OR " +
           "(c.participant1Id = :p2 AND c.participant2Id = :p1 AND c.type = :type)")
    Optional<ChatConversation> findByParticipantsAndType(
            @Param("p1") String p1,
            @Param("p2") String p2,
            @Param("type") String type);

    @Query("SELECT c FROM ChatConversation c WHERE " +
           "c.participant1Id = :userId OR c.participant2Id = :userId " +
           "ORDER BY c.lastMessageAt DESC NULLS LAST")
    List<ChatConversation> findByParticipant(@Param("userId") String userId);

    @Query("SELECT c FROM ChatConversation c WHERE " +
           "(c.participant1Id = :userId OR c.participant2Id = :userId) AND c.type = :type " +
           "ORDER BY c.lastMessageAt DESC NULLS LAST")
    List<ChatConversation> findByParticipantAndType(
            @Param("userId") String userId,
            @Param("type") String type);
}
