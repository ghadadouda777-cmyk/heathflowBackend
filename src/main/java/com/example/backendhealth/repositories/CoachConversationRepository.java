package com.example.backendhealth.repositories;

import com.example.backendhealth.entities.CoachConversation;
import com.example.backendhealth.entities.CoachConversation.ConversationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CoachConversationRepository extends JpaRepository<CoachConversation, Long> {
    List<CoachConversation> findByCoachId(String coachId);
    Optional<CoachConversation> findByCoachIdAndClientIdAndStatus(String coachId, String clientId, ConversationStatus status);
}
