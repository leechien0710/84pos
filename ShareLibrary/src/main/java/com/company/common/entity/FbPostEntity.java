package com.company.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "fb_post")
@Data
public class FbPostEntity {

    @Id
    private String id;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(columnDefinition = "TEXT")
    private String fullPictureUrl;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    private Long commentCount;

    private Long likeCount;

    private String statusType;

    private String pageId;

    @Column(name = "is_published")
    private Boolean isPublished;
}

