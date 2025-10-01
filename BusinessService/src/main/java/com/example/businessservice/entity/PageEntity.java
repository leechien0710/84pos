package com.example.businessservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageEntity {
    @Id
    private String pageId;
    private String userId;
    @Column(columnDefinition = "TEXT")
    private String pageAccessToken;

    private Long expiresAt;
}