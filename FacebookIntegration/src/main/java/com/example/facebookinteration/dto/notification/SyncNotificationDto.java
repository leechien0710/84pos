package com.example.facebookinteration.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SyncNotificationDto {
    private Integer status; // 1=ACTIVE, 2=INACTIVE, 3=SYNCING, 4=FAILED
    private String message;
}
