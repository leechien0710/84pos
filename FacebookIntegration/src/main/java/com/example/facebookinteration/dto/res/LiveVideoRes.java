package com.example.facebookinteration.dto.res;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LiveVideoRes {

    @JsonAlias("status")
    private String status;

    @JsonAlias("embed_html")
    private String embedHtml;

    private Video video;

    @Getter
    @Setter
    public static class Video {
        private String id;
    }
}
