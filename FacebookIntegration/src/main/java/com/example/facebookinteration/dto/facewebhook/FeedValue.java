package com.example.facebookinteration.dto.facewebhook;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedValue {
    private From from;
    private String message;
    @JsonAlias("post_id")
    private String postId;
    @JsonAlias("created_time")
    private long createdTime;
    private String item;
    private int published;
    private String verb;
    @JsonAlias("comment_id")
    private String commentId;
    @JsonAlias("parent_id")
    private String parentId;
    private Map<String,Object> post;
}
