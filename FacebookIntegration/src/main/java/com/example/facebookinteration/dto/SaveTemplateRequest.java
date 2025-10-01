package com.example.facebookinteration.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class SaveTemplateRequest {
    @NotBlank(message = "template_content: must not be blank")
    @Size(max = 1000, message = "template_content: size must be between 0 and 1000")
    private String template_content;
} 