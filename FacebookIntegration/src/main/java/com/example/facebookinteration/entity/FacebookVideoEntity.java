package com.example.facebookinteration.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.example.facebookinteration.dto.res.LiveVideoRes;
import com.example.facebookinteration.utils.DataUtils;

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

    public static FacebookVideoEntity fromLiveVideoRes(LiveVideoRes res, String pageId) {
        FacebookVideoEntity entity = new FacebookVideoEntity();
        entity.setId(res.getVideo().getId());
        entity.setEmbedHtml(res.getEmbedHtml());
        entity.setLiveStatus(res.getStatus());
        entity.setPostId(DataUtils.buildPostId(pageId, res.getVideo().getId()));
        return entity;
    }
}

