package org.mypet.notificationserver.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mypet.notificationserver.entity.enums.InboxMessageStatus;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "inbox_message")
public class InboxMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipient;

    private String subject;

    private String message;

    @Enumerated(EnumType.STRING)
    private InboxMessageStatus status;

    @Column(name = "retry_count")
    private Integer retryCount;

    @Column(name = "last_attempt")
    private LocalDateTime lastAttempt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
