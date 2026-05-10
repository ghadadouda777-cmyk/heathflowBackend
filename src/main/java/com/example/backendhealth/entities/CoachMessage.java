package com.example.backendhealth.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "coach_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoachMessage {

    public enum SenderRole {
        COACH, CLIENT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    private CoachConversation conversation;

    @Column(nullable = false)
    private String senderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SenderRole senderRole;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Builder.Default
    private boolean isRead = false;

    @CreationTimestamp
    private LocalDateTime sentAt;
}
