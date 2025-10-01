package com.example.facebookinteration.service;

import com.example.facebookinteration.entity.PageTemplate;
import com.example.facebookinteration.entity.SystemVariable;
import java.util.List;

public interface TemplateService {
    List<SystemVariable> getAllVariables();
    PageTemplate saveTemplate(String pageId, String templateContent);
} 