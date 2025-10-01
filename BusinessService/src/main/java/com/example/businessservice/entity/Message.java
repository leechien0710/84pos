package com.example.businessservice.entity;

import jakarta.persistence.*;
import lombok.*;

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
    @Column(columnDefinition = "TIMESTAMP(3)")
    private LocalDateTime createdTime;
}
