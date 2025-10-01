package com.company.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    @Id
    private String messageId;
    private String senderId;
    private String conversationId;
    @Column(columnDefinition = "TEXT")
    private String content;
    private String type;
    private LocalDateTime createdTime;
}
