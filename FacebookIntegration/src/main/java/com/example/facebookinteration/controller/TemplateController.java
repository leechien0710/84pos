package com.example.facebookinteration.controller;

import com.example.facebookinteration.entity.PageTemplate;
import com.example.facebookinteration.service.TemplateService;
import com.example.facebookinteration.dto.SystemVariableDto;
import com.example.facebookinteration.dto.PageTemplateDto;
import com.example.facebookinteration.dto.SaveTemplateRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping()
@RequiredArgsConstructor
@Validated
public class TemplateController {
    private static final Logger log = LoggerFactory.getLogger(TemplateController.class);
    private final TemplateService templateService;

    @GetMapping("/system-variables")
    public ResponseEntity<List<SystemVariableDto>> getVariables() {
        List<SystemVariableDto> dtos = templateService.getAllVariables().stream()
            .map(v -> new SystemVariableDto(v.getVariableKey(), v.getDisplayName()))
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/pages/{pageId}/template")
    public ResponseEntity<?> saveTemplate(
            @PathVariable String pageId,
            @Valid @RequestBody SaveTemplateRequest body
    ) {
        PageTemplate template = templateService.saveTemplate(pageId, body.getTemplate_content());
        log.info("Saved template for pageId {}: {}", pageId, body.getTemplate_content());
        return ResponseEntity.ok(new PageTemplateDto(template.getPageId(), template.getTemplateContent()));
    }
} 