package com.example.businessservice.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
public class GetConversationResponseDTO {

    @NotNull
    private String conversationId;

    @NotNull
    private String senderName;

    @NotNull
    private String senderAvatar;

    @NotNull
    private String finallyMessage;

    @NotNull
    private LocalDateTime messageCreatedDate;
}
