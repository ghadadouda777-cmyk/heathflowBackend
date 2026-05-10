package com.example.backendhealth.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

    public enum SenderRole {
        PATIENT, NUTRITIONIST
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @Column(nullable = false)
    private Long senderId;

    @Enumerated(EnumType.STRING)
    private SenderRole senderRole;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private boolean isRead;

    @CreationTimestamp
    private LocalDateTime sentAt;
}