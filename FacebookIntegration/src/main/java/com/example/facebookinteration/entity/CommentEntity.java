package com.example.facebookinteration.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "facebook_comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentEntity {

    @Id
    private String id;

    @Column(length = 10000)
    private String message;

    @Column(columnDefinition = "TEXT")
    private String fromUser;

    @Column(columnDefinition = "TEXT")
    private String attachment;

    private String postId;

    private LocalDateTime createdTime;

    private Boolean hasPhone;

    private Boolean isHidden;

}
