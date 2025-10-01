package com.example.facebookinteration.repository;

import java.util.Optional;
import com.example.facebookinteration.entity.PageTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageTemplateRepository extends JpaRepository<PageTemplate, String> {
    Optional<PageTemplate> findByPageId(String pageId);
} 