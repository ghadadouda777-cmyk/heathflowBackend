package com.example.backendhealth.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_conversations",
       uniqueConstraints = @UniqueConstraint(columnNames = {"participant1_id", "participant2_id", "type"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatConversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "participant1_id", nullable = false)
    private String participant1Id;

    @Column(name = "participant2_id", nullable = false)
    private String participant2Id;

    /** "PATIENT_NUTRITIONIST" or "CLIENT_COACH" */
    @Column(nullable = false)
    private String type;

    @Column(columnDefinition = "TEXT")
    private String lastMessage;

    private LocalDateTime lastMessageAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
