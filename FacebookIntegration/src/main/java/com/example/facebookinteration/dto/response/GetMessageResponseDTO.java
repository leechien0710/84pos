package com.example.facebookinteration.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class GetMessageResponseDTO {
    @NotNull
    private String senderId;
    @NotNull
    private String senderName;
    @NotNull
    private String senderAvatar;
    @NotNull
    private String content;
    @NotNull
    private LocalDateTime createdDate;
}
