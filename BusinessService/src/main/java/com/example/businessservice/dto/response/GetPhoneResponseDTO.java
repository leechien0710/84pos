package com.example.businessservice.dto.response;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor

public class GetPhoneResponseDTO {
    @NotNull
    private String phoneNumber;
}
