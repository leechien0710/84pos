package com.example.facebookinteration.dto.facebookdto;

import com.example.facebookinteration.dto.Paging;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class FacebookCommentResponse {
    private Comments comments;
    private String id;

    @Data
    public static class Comments {
        private List<CommentData> data;
        private Paging paging;
    }

    @Data
    public static class CommentData {
        private String id;
        private String message;
        private String created_time;
        private From from;
        private Attachment attachment;
        private List<CommentData> data;

        @Data
        public static class From {
            private String id;
            private String name;
            
            // Default constructor
            public From() {}
            
            // Constructor with parameters
            @JsonCreator
            public From(@JsonProperty("id") String id, @JsonProperty("name") String name) {
                this.id = id;
                this.name = name;
            }
        }

        @Data
        public static class Attachment {
            private Media media;
            @Data
            public static class Media {
                private Image image;
                @Data
                public static class Image {
                    private String src;
                }
            }
        }
    }
}

