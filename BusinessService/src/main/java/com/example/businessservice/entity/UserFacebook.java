package com.example.businessservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "UserFacebook")
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

