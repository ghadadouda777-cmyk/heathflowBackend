package com.example.backendhealth.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ID of the user who receives this notification */
    @Column(nullable = false)
    private String userId;

    /** RDV_NEW | RDV_CONFIRMED | RDV_REFUSED | RDV_TERMINATED | NEW_MESSAGE */
    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Builder.Default
    @Column(nullable = false)
    private boolean read = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    /** Optional: ID of the related RDV or conversation for navigation */
    private String relatedId;
}
