package com.example.facebookinteration.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "live_video")
public class LiveVideoEntity {
    @Id
    private String liveVideoId;
    private String title;
    private String description;
    private String pageId;
    private String postId;
    private String status;
    private String embedHtml;
    private OffsetDateTime createdTime;
}
