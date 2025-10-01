package com.company.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import jakarta.persistence.Column;
import lombok.Data;

@Entity
@Table(name = "facebook_video")
@Data
public class FacebookVideoEntity {

    @Id
    private String id;

    @Column(name = "post_id")
    private String postId;

    @Column(name = "embed_html", columnDefinition = "TEXT") // Nếu embedHtml dài thì dùng TEXT
    private String embedHtml;

    @Column(name = "live_status")
    private String liveStatus;

}

