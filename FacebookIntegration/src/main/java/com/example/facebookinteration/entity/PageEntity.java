package com.example.facebookinteration.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Integer status;
}
