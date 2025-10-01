package com.example.facebookinteration.dto.facewebhook;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Messaging {
    private Sender sender;
    private Recipient recipient;
    private long timestamp;
    private Message message;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Sender {
        private String id;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Recipient {
        private String id;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Message {
        private String mid;
        private String text;

    }
}
