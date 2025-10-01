package com.example.facebookinteration.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageRes {
    private String pageId;
    private String pageName;
    private String pageAvatarUrl;
}
