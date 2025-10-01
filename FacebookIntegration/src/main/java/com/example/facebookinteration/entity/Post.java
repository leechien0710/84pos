package com.example.facebookinteration.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Post {
    @Id
    private String postId;
    private String message;
    private String pageId;
    private int published;
    private String createdUser;
    private Long createdTime;
}
