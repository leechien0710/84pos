package com.example.facebookinteration.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data // Lombok annotation để tự động sinh getter, setter, toString, equals, hashCode
public class PostRes {

    @JsonProperty("admin_creator")
    private AdminCreator adminCreator;

    private String message;

    @JsonProperty("created_time")
    private String createdTime;

    private String id;

    @Data
    public static class AdminCreator {
        private String name;
        private String id;
    }
}
