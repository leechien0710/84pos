package com.example.facebookinteration.entity;

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
public class Comment {
    @Id
    private String commentId;
    private String message;
    private String parentId;
    private Long createdTime;
    private String createdBy;
    private Boolean isHidden;
}
