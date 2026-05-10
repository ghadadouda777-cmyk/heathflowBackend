package com.example.backendhealth.repositories;

import com.example.backendhealth.entities.Conversation;
import com.example.backendhealth.entities.Conversation.ConversationStatus;
import com.example.backendhealth.entities.Conversation.ConversationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    List<Conversation> findByPatientId(Long patientId);

    List<Conversation> findByNutritionistId(Long nutritionistId);

    List<Conversation> findByCoachId(Long coachId);

    Optional<Conversation> findByPatientIdAndNutritionistIdAndStatus(
            Long patientId, Long nutritionistId, ConversationStatus status);

    Optional<Conversation> findByPatientIdAndCoachIdAndStatus(
            Long patientId, Long coachId, ConversationStatus status);
}