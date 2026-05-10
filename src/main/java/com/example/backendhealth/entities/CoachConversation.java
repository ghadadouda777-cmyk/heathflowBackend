package com.example.backendhealth.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "coach_conversations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoachConversation {

    public enum ConversationStatus {
        ACTIVE, CLOSED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String coachId;

    @Column(nullable = false)
    private String clientId;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ConversationStatus status = ConversationStatus.ACTIVE;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("sentAt ASC")
    @Builder.Default
    private List<CoachMessage> messages = new ArrayList<>();
}
