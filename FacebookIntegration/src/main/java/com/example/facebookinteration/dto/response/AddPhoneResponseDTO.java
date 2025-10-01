package com.example.facebookinteration.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter()
@NoArgsConstructor
public class AddPhoneResponseDTO {
    @NotNull
    private Long id;
    @NotNull
    private String senderId;
    @NotNull
    private String phoneNumber;
}
