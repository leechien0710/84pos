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
public class UserFacebook {
    @Id
    private String userId;
    @Column(columnDefinition = "TEXT")
    private String userAccessToken;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String avatar;
    private Long userAppId;
    private Long expiresAt;
}
