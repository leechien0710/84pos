package com.example.facebookinteration.dto.facewebhook;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacebookWebhookPayload {
    private List<Entry> entry;
    private String object;
}
