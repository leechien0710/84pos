package com.example.facebookinteration.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
public class GetAddressResponseDTO {
    @NotNull
    private String address;
}
