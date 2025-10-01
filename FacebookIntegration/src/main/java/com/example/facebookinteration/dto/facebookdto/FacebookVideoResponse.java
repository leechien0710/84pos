package com.example.facebookinteration.dto.facebookdto;

import com.example.facebookinteration.dto.Paging;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.util.List;

@Data
public class FacebookVideoResponse {
    private Videos videos;

    @Data
    public static class Videos {
        private List<VideoData> data;
        private Paging paging;
    }

    @Data
    public static class VideoData {
        @JsonAlias("post_id")
        private String postId;

        @JsonAlias("embed_html")
        private String embedHtml;

        @JsonAlias("live_status")
        private String liveStatus;

        private String id;
    }
}
