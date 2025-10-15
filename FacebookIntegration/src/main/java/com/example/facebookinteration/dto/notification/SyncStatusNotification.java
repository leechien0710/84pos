package com.example.facebookinteration.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SyncStatusNotification {
    private String pageId;
    private String pageName;
    private Integer status;
    private String message;
}
