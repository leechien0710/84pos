package com.example.facebookinteration.dto.facebookdto;

import com.example.facebookinteration.dto.Paging;
import lombok.Data;

import java.util.List;

@Data
public class PostSync {
    private Posts posts;

    @Data
    public static class Posts {
        private List<Post> data;
        private Paging paging;
    }

    @Data
    public static class Post {
        private String id;
        private String message;
        private String created_time;
        private String full_picture;
        private Attachments attachments;
        private String updated_time;
        private Comments comments;
        private Likes likes;
        private String status_type;
        private Boolean is_published;

        @Data
        public static class Attachments {
            private List<AttachmentData> data;

            @Data
            public static class AttachmentData {
                private String title;
            }
        }

        @Data
        public static class Comments {
            private Summary summary;
            @Data
            public static class Summary {
                private Long total_count;
            }
        }
        @Data
        public static class Likes {
            private Summary summary;
            @Data
            public static class Summary {
                private Long total_count;
            }
        }
    }
}

