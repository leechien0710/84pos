package com.example.facebookinteration.dto;

import lombok.Data;

@Data
public class Paging {
    private Cursors cursors;
    private String next;

    @Data
    public static class Cursors {
        private String before;
        private String after;
    }
}
