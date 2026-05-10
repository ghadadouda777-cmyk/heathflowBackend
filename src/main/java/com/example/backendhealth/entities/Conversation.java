package com.example.backendhealth.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "conversations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conversation {

    public enum ConversationStatus {
        ACTIVE, CLOSED, ARCHIVED
    }

    public enum ConversationType {
        NUTRITIONIST_PATIENT,
        COACH_PATIENT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long patientId;

    // nullable — utilisé seulement si type = NUTRITIONIST_PATIENT
    @Column
    private Long nutritionistId;

    // nullable — utilisé seulement si type = COACH_PATIENT
    @Column
    private Long coachId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ConversationType type = ConversationType.NUTRITIONIST_PATIENT;

    @Enumerated(EnumType.STRING)
    private ConversationStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("sentAt ASC")
    @Builder.Default
    private List<Message> messages = new ArrayList<>();
}