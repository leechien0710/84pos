package com.example.facebookinteration.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddAddressResponseDTO {
    @NotNull
    private Long id;
    @NotNull
    private String senderId;
    @NotNull
    private String address;
}
