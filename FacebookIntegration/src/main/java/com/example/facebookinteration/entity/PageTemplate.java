package com.example.facebookinteration.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "page_templates")
@Getter
@Setter
public class PageTemplate {
    @Id
    @Column(name = "page_id")
    private String pageId;

    @Column(name = "template_content", columnDefinition = "TEXT")
    private String templateContent;
} 